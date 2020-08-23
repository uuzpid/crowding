<%@ page contentType="text/html;charset=UTF-8" language="java"
    pageEncoding="UTF-8" %>
<html>
  <head>
    <meta charset="UTF-8">
    <title>$Title$</title>
    <%--http://localhost:8080/crowdfunding_admin_webui_war/test/ssm.html--%>
    <%--contextPath前面不能写"/"
        contextPath后面要写"/"--%>
    <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>
    <script type="text/javascript" src="jquery/jquery-3.5.1.min.js"></script>
    <script type="text/javascript" src="layer/layer.js"></script>
    <script type="text/javascript">
      $(function(){
        $("#btn1").click(function () {
          $.ajax({
            "url":"send/array/one.html",
            "type":"post",
            "data":{
              "array":[5,8,12]
            },
            "dataType":"text",
            "success":function (response) {
              console.log(response)
            },
            "error":function (response) {
              console.log(response)
            }
          });
        });
        $("#btn2").click(function () {
          $.ajax({
            "url":"send/array/two.html",
            "type":"post",
            "data":{
              "array[0]":5,
              "array[1]":8,
              "array[2]":12
            },
            "dataType":"text",
            "success":function (response) {
              console.log(response)
            },
            "error":function (response) {
              console.log(response)
            }
          });
        });
        $("#btn3").click(function () {
          //准备好要发送到服务端的数组
          var array = [5,8,12];
          //将JSON数组转化为JSON字符串
          var requestBody = JSON.stringify(array);
          $.ajax({
            "url":"send/array/three.html",
            "type":"post",
            "data":requestBody,
            "contentType":"application/json;charset=UTF-8", //告诉服务器端本次请求体的数据是json数据
            "dataType":"text",
            "success":function (response) {
              console.log(response)
            },
            "error":function (response) {
              console.log(response)
            }
          });
        });
        $("#btn4").click(function () {
          //准备好要发送到服务端的数组
          var student={
            "studentId":5,
            "studentName":"tom",
            "address":{
              "province":"广东",
              "city":"深证",
              "street":"后瑞",
            },
            "subjectList":[{
              "subjectName": "javase",
              "subjectScore":100,
            },
              {
                "subjectName": "c++",
                "subjectScore":99,
              }],
            "map":{
              "k1":"v1",
              "k2":"v2",
            }
          };


          //将JSON数组转化为JSON字符串
          var requestBody = JSON.stringify(student);
          $.ajax({
            "url":"send/array/object.json",
            "type":"post",
            "data":requestBody,
            "contentType":"application/json;charset=UTF-8", //告诉服务器端本次请求体的数据是json数据
            "dataType":"json",
            "success":function (response) {
              console.log(response)
            },
            "error":function (response) {
              console.log(response)
            }
          });
        });
        $("#btn5").click(function () {
          layer.msg("layer的弹窗");

        });
      });
    </script>
  </head>
  <body>
    <%--这里不能斜杠开头，他才会参考base标签--%>
    <a href="test/ssm.html">测试SSM</a>
    <br>
    <button id="btn1">Send [5,8,12]</button>
    <br>
    <button id="btn2">send </button>
    <br>
    <button id="btn3">send </button>
    <br>
    <button id="btn4">send fuza</button>
    <br>
    <button id="btn5">点我弹窗</button>
  </body>
</html>
