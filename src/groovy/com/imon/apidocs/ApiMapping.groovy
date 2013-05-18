package com.imon.apidocs

class ApiMapping {
    String mappingName
    String controllerName
    String module
    String href
    String description
    String urlPattern
    String completeUrl
    String controllerFullName
    Map urlParams
    List<HttpVerb> httpVerbs = []
}
