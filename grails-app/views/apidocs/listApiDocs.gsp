<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>API Documentation</title>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'apidocs.css')}" type="text/css">
</head>

<body>
<div class="container">
    <g:each in="${endpoints}" var="module">
        <div class="module">
            <div class="module_name">${module.key} <a href="#">${module.value.href}</a></div>
            <div class="module_description">${module.value.description}</div>
        </div>

        <div class="module_methods">
            <g:each in="${module.value.httpVerbs}" var="verb">
                <div class="api_heading">
                    <span class="api_method ${verb.name.toLowerCase()}">${verb.name}</span>
                    <span class="api_path">${module.value.completeUrl}</span>
                    <span class="api_description">${verb.title}</span>
                </div>
            </g:each>
        </div>
    </g:each>
</div>
</body>
</html>