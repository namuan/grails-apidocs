grails.project.work.dir = "target"

grails.project.dependency.resolution = {

    inherits "global"
    log "warn"

    repositories {
        grailsCentral()
        mavenCentral()
        grailsRepo "http://grails.org/plugins"
    }

    plugins {
        build ":release:2.2.1", ":rest-client-builder:1.0.3", {
            export = false
        }
    }
}
