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

    <style>
        pre {
            outline: 1px solid #ccc;
            padding: 5px;
            margin: 5px;
        }

        .string {
            color: green;
        }

        .number {
            color: darkorange;
        }

        .boolean {
            color: blue;
        }

        .null {
            color: magenta;
        }

        .key {
            color: red;
        }
    </style>
    <script src="/resources/jquery/jquery-2.1.3.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#sb").click(function(){start();});
            $("#findUdc").click(function(){findUdc();});

            window.setInterval(function(){
                find();
            }, 15000);
        });
        start = function () {
            $.get("/new")
                    .done(function (data) {
                        console.log(data);
                        $("#one").html(syntaxHighlight(data));
                    })
                    .fail(function (data) {
                        console.log(data)
                    });
        };
        find = function () {
            $.get("/find")
                    .done(function (data) {
                        console.log(data);
                        $("#one").html(syntaxHighlight(data));
                    })
                    .fail(function (data) {
                        console.log(data)
                    });
            $.get("/count")
                    .done(function (data) {
                        console.log(data);
                        $("#count").html("found "+data);
                    })
                    .fail(function (data) {
                        console.log("found "+data);
                    });
            $.get("/udcCount")
                    .done(function (data) {
                        console.log(data);
                        $("#udcCount").html("found udc "+data);
                    })
                    .fail(function (data) {
                        console.log("found udc "+data);
                    });
        };
        findUdc = function () {
            $.get("/findUdc")
                    .done(function (data) {
                        console.log(data);
                        $("#findUdc").html(syntaxHighlight(data));
                    })
                    .fail(function (data) {
                        console.log(data)
                    });
        };
        function syntaxHighlight(json) {
            if (typeof json != 'string') {
                json = JSON.stringify(json, undefined, 2);
            }
            json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
            return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
                var cls = 'number';
                if (/^"/.test(match)) {
                    if (/:$/.test(match)) {
                        cls = 'key';
                    } else {
                        cls = 'string';
                    }
                } else if (/true|false/.test(match)) {
                    cls = 'boolean';
                } else if (/null/.test(match)) {
                    cls = 'null';
                }
                return '<span class="' + cls + '">' + match + '</span>';
            });
        }
    </script>
</head>
<body>
<p><input type ="button" id = "sb" value="Search Again"/></p>
<p><input type ="button" id = "findUdc" value="Find UDC"/></p>
<pre><code>
    <div id="count"></div>
    <div id="udcCount"></div>
    <div id="one"></div>
</code></pre>
</body>
</html>
