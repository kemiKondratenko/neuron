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
    <script type="text/javascript" src="resources/jquery/jquery-2.1.3.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $.get("/new")
                    .done(function (data) {
                        console.log(data);
                        $("#one").html(JSONstringify(data));
                    })
                    .fail(function (data) {
                        console.log(data)
                    });
        });

        function JSONstringify(json) {
            if (typeof json != 'string') {
                json = JSON.stringify(json, undefined, '\t');
            }

            var
                    arr = [],
                    _string = 'color:green',
                    _number = 'color:darkorange',
                    _boolean = 'color:blue',
                    _null = 'color:magenta',
                    _key = 'color:red';

            json = json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
                var style = _number;
                if (/^"/.test(match)) {
                    if (/:$/.test(match)) {
                        style = _key;
                    } else {
                        style = _string;
                    }
                } else if (/true|false/.test(match)) {
                    style = _boolean;
                } else if (/null/.test(match)) {
                    style = _null;
                }
                arr.push(style);
                arr.push('');
                return '%c' + match + '%c';
            });

            arr.unshift(json);

            console.log.apply(console, arr);
            return arr;
        }
    </script>
</head>
<body>
<div id="one"></div>
</body>
</html>
