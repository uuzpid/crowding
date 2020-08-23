<%@ page contentType="text/html;charset=UTF-8" language="java"
    pageEncoding="UTF-8" %>
<html>
  <head>
    <meta charset="UTF-8">
    <title>$Title$</title>
    <%--http://localhost:8080/crowdfunding_admin_webui_war/test/ssm.html--%>
    <%--contextPath前面不能写"/"
        contextPath后面要写"/"--%>
    <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />
  </head>
  <body>
    <%--这里不能斜杠开头，他才会参考base标签--%>
    <a href="test/ssm.html">测试SSM</a>
  </body>
</html>
