<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>API Documentation</title>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'apidocs.css')}" type="text/css">
</head>

<body>
<div class="container">
    <g:each in="${endpoints}" var="endpoint">
        <div class="api">
            <g:each in="${endpoint.value.apiMappings}" var="module">
                <div class="module">
                    <div class="module_name">(${endpoint.key}) - ${module.completeUrl} <a href="${module.href}">${module.href}</a></div>

                    <div class="module_description">${module.description}</div>
                </div>

                <div class="module_methods">
                    <g:each in="${module.httpVerbs}" var="verb">
                        <div class="api_heading">
                            <span class="api_method ${verb.name.toLowerCase()}">${verb.name}</span>
                            <span class="api_path">${module.completeUrl}</span>
                            <span class="api_description">${verb.title}</span>
                        </div>

                        <g:if test="${verb.classProperties}">
                            <div class="api_request">
                                <table>
                                    <tr>
                                        <th>Parameter</th>
                                        <th>Type</th>
                                    </tr>
                                    <g:each in="${verb.classProperties}" var="par">
                                        <tr>
                                            <td>${par.key}</td>
                                            <td>${par.value}</td>
                                        </tr>
                                    </g:each>
                                </table>
                            </div>
                        </g:if>
                    </g:each>
                </div>
            </g:each>
        </div>
    </g:each>
</div>
</body>
</html>