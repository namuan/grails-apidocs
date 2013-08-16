package com.imon.apidocs

import org.codehaus.groovy.grails.validation.ConstrainedProperty

class ApiMapping {
    String mappingName
    String controllerName
    String module
    String href
    String description
    String urlPattern
    String completeUrl
    String controllerFullName
    ConstrainedProperty[] urlParams
    List<HttpVerb> httpVerbs = []
}
