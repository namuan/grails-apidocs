<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>API Documentation</title>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'apidocs.css')}" type="text/css">
</head>

<body>
<dl>
    <g:each in="${endpoints}" var="module">
        <dt>${module.key} - (<a href="#">${module.value.href}</a>)</dt>
        <dd>${module.value.description}</dd>
        <g:each in="${module.value.httpVerbs}" var="verb">
            <dd>> ${verb.name} - ${module.value.completeUrl}</dd>
            <dd>${verb.title}</dd><br/>
        </g:each>
    </g:each>
</dl>

</body>
</html>