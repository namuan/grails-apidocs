import com.imon.apidocs.ApiUtils

class ApidocsGrailsPlugin {
    def version = "0.2"
    def grailsVersion = "2.0 > *"
	 def pluginExcludes = [
        "grails-app/**/README.txt"
    ]
    def title = "Grails Apidocs Plugin"
    def author = "Nauman Leghari"
    def authorEmail = "laghari78@yahoo.com"
    def description = 'Plugin to generate REST Api documentation.'

    def documentation = "http://grails.org/plugin/grails-apidocs"

    def license = "APACHE"

    def issueManagement = [ system: "GITHUB", url: "https://github.com/namuan/grails-apidocs/issues" ]

    def scm = [ url: "https://github.com/namuan/grails-apidocs" ]

    def doWithApplicationContext = { applicationContext ->
        ApiUtils.buildApiRegistry(applicationContext, application)
    }
}
