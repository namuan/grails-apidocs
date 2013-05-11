import com.imon.apidocs.ApiEndpoint
import com.imon.apidocs.ApiRegistry
import com.imon.apidocs.HttpVerb
import com.imon.apidocs.annotations.Api
import com.imon.apidocs.annotations.ApiOperation
import grails.web.Action

class GrailsApidocsGrailsPlugin {
    // the plugin version
    def version = "0.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.2 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def title = "Grails Apidocs Plugin" // Headline display name of the plugin
    def author = "Nauman Leghari"
    def authorEmail = ""
    def description = '''\
Plugin to generate REST Api documentation.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/grails-apidocs"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
//    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
//    def organization = [ name: "My Company", url: "http://www.my-company.com/" ]

    // Any additional developers beyond the author specified above.
//    def developers = [ [ name: "Joe Bloggs", email: "joe@bloggs.net" ]]

    // Location of the plugin's issue tracker.
//    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPMYPLUGIN" ]

    // Online location of the plugin's browseable source code.
//    def scm = [ url: "http://svn.codehaus.org/grails-plugins/" ]

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before
    }

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { applicationContext ->
        buildApiRegistry(applicationContext, application)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    def onShutdown = { event ->
        // TODO Implement code that is executed when the application shuts down (optional)
    }

        def buildApiRegistry(applicationContext, grailsApplication) {
        def urlMappingsHolder = applicationContext.getBean('grailsUrlMappingsHolder')

        urlMappingsHolder.urlMappings.each { u ->
            if (!u.mappingName) return

            def urlMappingInfo = u.match(u.urlData.urlPattern)

            def urlParams = urlMappingInfo.parameters.findAll { it -> !['action', 'controller'].contains(it.key) }

            ApiEndpoint endpoint = new ApiEndpoint(
                    mappingName: u.mappingName,
                    controllerName: u.controllerName,
                    urlPattern: u.urlData.urlPattern,
                    urlParams: urlParams
            )

            // inject params in url
            def url = endpoint.urlPattern.replace('(*)', '%')

            endpoint.completeUrl = urlParams.inject(url) { substitutedUrl, k, v ->
                substitutedUrl.replaceFirst('%', '{' + k + '}')
            }

            // populate registry with annotation data
            def controller = grailsApplication.controllerClasses.find { it.logicalPropertyName == u.controllerName && it.clazz.isAnnotationPresent(Api) }

            if (!controller) return

            Api apiAnnotation = controller.clazz.getAnnotation(Api)
            controller.clazz.isAnnotationPresent(Action)
            endpoint.module = apiAnnotation.module()
            endpoint.href = apiAnnotation.href()
            endpoint.description = apiAnnotation.description()
            endpoint.controllerFullName = controller.fullName

            // get annotations on controller methods
            controller.clazz.methods.findAll {
                it.getAnnotation(Action) != null && it.getAnnotation(ApiOperation) != null
            }.each { controllerMethod ->

                def action = u.actionName.find { it.value == controllerMethod?.name }

                if (!action) return

                ApiOperation apiOperation = controllerMethod?.getAnnotation(ApiOperation)

                def httpVerb = new HttpVerb()
                httpVerb.name = action.key
                httpVerb.controllerMethod = action.value
                httpVerb.title = apiOperation.value()
                httpVerb.notes = apiOperation.notes()
                httpVerb.responseClass = apiOperation.responseClass()

                endpoint.httpVerbs << httpVerb
            }

            ApiRegistry.endpoints.put(endpoint.mappingName, endpoint)
        }
    }

}
