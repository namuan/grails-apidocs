package com.imon.apidocs

import com.imon.apidocs.annotations.Api
import com.imon.apidocs.annotations.ApiOperation
import grails.web.Action

class ApiUtils {
    static def buildApiRegistry(applicationContext, grailsApplication) {
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