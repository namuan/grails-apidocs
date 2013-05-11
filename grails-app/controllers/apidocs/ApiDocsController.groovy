package apidocs

import com.imon.apidocs.ApiRegistry
import grails.converters.JSON

class ApiDocsController {

    def listApiDocs() {
        withFormat {
            json {
                render([endpoints: ApiRegistry.endpoints] as JSON)
            }
            html {
                [endpoints: ApiRegistry.endpoints]
            }
        }
    }
}
