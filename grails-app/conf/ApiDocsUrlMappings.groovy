class ApiDocsUrlMappings {

	static mappings = {
		 "/apidocs"(controller: "apiDocs") { action = [GET: "listApiDocs"] }
	}
}
