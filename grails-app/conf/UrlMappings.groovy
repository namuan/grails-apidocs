class UrlMappings {

	static mappings = {
		 "/apidocs"(controller: "apiDocs") { action = [GET: "listApiDocs"] }

		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"/"(view:"/index")
		"500"(view:'/error')
	}
}
