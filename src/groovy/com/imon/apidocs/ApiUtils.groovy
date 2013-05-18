package com.imon.apidocs

import com.imon.apidocs.annotations.Api
import com.imon.apidocs.annotations.ApiOperation
import grails.web.Action
import org.codehaus.groovy.grails.commons.GrailsClassUtils
import org.codehaus.groovy.grails.web.mapping.UrlMapping
import org.codehaus.groovy.grails.web.mapping.UrlMappingsHolder

import java.lang.reflect.Method

class ApiUtils {

    static def IGNORED_PROPERTIES = ['getMetaClass', 'getProperty', 'getProperties', 'getErrors', 'getAll',
    'getCount', 'getValidationErrorsMap', 'getValidationSkipMap', 'getGormDynamicFinders', 'getGormPersistentEntity',
    'getConstraints', 'getId', 'getVersion']

    static def buildApiRegistry(applicationContext, grailsApplication) {
        UrlMappingsHolder urlMappingsHolder = applicationContext.getBean('grailsUrlMappingsHolder')

        urlMappingsHolder.urlMappings.each { UrlMapping u ->
            if (!u.mappingName) return

            def urlMappingInfo = u.match(u.urlData.urlPattern)

            def urlParams = urlMappingInfo.parameters.findAll { it -> !['action', 'controller'].contains(it.key) }

            ApiEndpoint endpoint = findOrCreate(u)

            ApiMapping apiMapping = new ApiMapping()

            apiMapping.controllerName = u.controllerName
            apiMapping.urlPattern = u.urlData.urlPattern
            apiMapping.urlParams = urlParams

            // inject params in url
            def url = apiMapping.urlPattern.replace('(*)', '%')

            apiMapping.completeUrl = apiMapping.urlParams.inject(url) { substitutedUrl, k, v ->
                substitutedUrl.replaceFirst('%', '{' + k + '}')
            }

            // populate registry with annotation data
            def controller = grailsApplication.controllerClasses.find { it.logicalPropertyName == u.controllerName && it.clazz.isAnnotationPresent(Api) }

            if (!controller) return

            Api apiAnnotation = controller.clazz.getAnnotation(Api)
            controller.clazz.isAnnotationPresent(Action)
            apiMapping.module = apiAnnotation.module()
            apiMapping.href = apiAnnotation.href()
            apiMapping.description = apiAnnotation.description()
            apiMapping.controllerFullName = controller.fullName

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
                httpVerb.classProperties = buildClassProperties(httpVerb.responseClass)

                apiMapping.httpVerbs << httpVerb
            }

            endpoint.apiMappings << apiMapping

            ApiRegistry.endpoints.put(u.mappingName, endpoint)
        }
    }

    static def findOrCreate(UrlMapping u) {
        return ApiRegistry.endpoints.get(u.mappingName) ?: new ApiEndpoint()
    }

    static def buildClassProperties(Class clazz) {
        if (clazz == Object.class) return null

        def classProps = [:]

        clazz.properties.declaredMethods.each { Method cm ->
            if (cm.name.startsWith("get") && cm.name.size() > "get".size() && !IGNORED_PROPERTIES.contains(cm.name)) {
                def propertyName =  GrailsClassUtils.getPropertyForGetter(cm.name)
                classProps << [ "${propertyName}": cm.returnType]
            }
        }

        return classProps
    }

}