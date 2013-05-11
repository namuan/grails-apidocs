
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title></title>
</head>
<body>
<ol class="slats">
    <g:each in="${endpoints}" var="module">
        <li class="group">
            <h3>${module.key} - Documentation:<a href="#">${module.value.href}</a></h3>
            <h5>${module.value.description}</h5>

            <g:each in="${module.value.httpVerbs}" var="verb">
                <div>${verb.name} - ${module.value.completeUrl}</div>
                <p>${verb.title}</p><br/>
            </g:each>
        </li>
    </g:each>
</ol>


</body>
</html>