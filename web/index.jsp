<%--
  Created by IntelliJ IDEA.
  User: Eugene
  Date: 16.03.2016
  Time: 22:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script type="text/javascript" src="http://www.google.com/jsapi"></script>
    <script type="text/javascript">
        google.load("jquery", "1.3.2");
    </script>
    <script src="https://code.jquery.com/jquery-1.10.2.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $.get("/new")
                    .done(function (data) {
                        console.log(data);
                        $("#one").text(JSON.stringify(data));
                    })
                    .fail(function (data) {
                        console.log(data)
                    });
        });
    </script>
</head>
<body>
<div id="one"></div>
</body>
</html>
