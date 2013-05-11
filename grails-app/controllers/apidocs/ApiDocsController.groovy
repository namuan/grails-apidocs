package apidocs

import com.imon.apidocs.ApiRegistry
import com.imon.apidocs.ApiUtils
import grails.converters.JSON

class ApiDocsController {

    def listApiDocs() {
        ApiUtils.hello("World")

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
