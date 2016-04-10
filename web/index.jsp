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
    <script src="//d3js.org/d3.v3.min.js" charset="utf-8"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#sb").click(function(){start();});
            $("#findUdc").click(function(){findUdc();});
            $("#index").click(function(){index();});
            $("#ctf").click(function(){ctf();});
            $("#cidf").click(function(){cidf();});
            $("#formNormalizedUdc").click(function(){formNormalizedUdc();});
            $("#ctfUdc").click(function(){ctfUdc();});
            $("#linkWordsToNormalizedUdc").click(function(){linkWordsToNormalizedUdc();});
            $("#cluster").click(function(){cluster();});
            $("#countPossibility").click(function(){countPossibility();});

            window.setInterval(function(){
                find();
            }, 100000);
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
        index = function () {
            $.get("/index")
                    .done(function (data) {
                        console.log(data);
                        $("#index").html(syntaxHighlight(data));
                    })
                    .fail(function (data) {
                        console.log(data)
                    });
        };
        ctf = function () {
            $.get("/ctf")
                    .done(function (data) {
                        console.log(data);
                        $("#ctf").html(syntaxHighlight(data));
                    })
                    .fail(function (data) {
                        console.log(data)
                    });
        };
        cidf = function () {
            $.get("/cidf")
                    .done(function (data) {
                        console.log(data);
                        $("#cidf").html(syntaxHighlight(data));
                    })
                    .fail(function (data) {
                        console.log(data)
                    });
        };
        formNormalizedUdc = function () {
            $.get("/formNormalizedUdc")
                    .done(function (data) {
                        console.log(data);
                        $("#formNormalizedUdc").html(syntaxHighlight(data));
                    })
                    .fail(function (data) {
                        console.log(data)
                    });
        };
        linkWordsToNormalizedUdc = function () {
            $.get("/linkWordsToNormalizedUdc")
                    .done(function (data) {
                        console.log(data);
                        $("#linkWordsToNormalizedUdc").html(syntaxHighlight(data));
                    })
                    .fail(function (data) {
                        console.log(data)
                    });
        };
        ctfUdc = function () {
            $.get("/ctfUdc")
                    .done(function (data) {
                        console.log(data);
                        $("#ctfUdc").html(syntaxHighlight(data));
                    })
                    .fail(function (data) {
                        console.log(data)
                    });
        };
        cluster = function () {
            $.get("/cluster")
                    .done(function (data) {
                        console.log(data);
                        $("#clusterRes").html(syntaxHighlight(data));
                    })
                    .fail(function (data) {
                        console.log(data)
                    });
        };
        countPossibility = function () {
            $.get("/countPossibility")
                    .done(function (data) {
                        console.log(data);
                        $("#countPossibility").html(syntaxHighlight(data));
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
<p><input type ="button" id = "index" value="Index found texts"/></p>
<p><input type ="button" id = "ctf" value="Term frequency"/></p>
<p><input type ="button" id = "cidf" value="Inverse document frequency"/></p>
<p><input type ="button" id = "formNormalizedUdc" value="Normalized Udc to 1 char"/></p>
<p><input type ="button" id = "linkWordsToNormalizedUdc" value="Link words to normalized udc"/></p>
<p><input type ="button" id = "ctfUdc" value="Terms frequency for udc"/></p>
<p><input type ="button" id = "countPossibility" value="count possibility for normalized udc"/></p>
<p><input type ="button" id = "cluster" value="cluster"/></p>
<pre><code>
    <div id="clusterRes"></div>
    <div id="count"></div>
    <div id="udcCount"></div>
    <div id="one"></div>
</code></pre>
</body>
</html>
