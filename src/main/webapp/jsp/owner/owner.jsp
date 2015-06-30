<%--
  Created by IntelliJ IDEA.
  User: Sasha
  Date: 19.06.2015
  Time: 15:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Creative - Bootstrap 3 Responsive Admin Template">
    <meta name="author" content="GeeksLabs">
    <meta name="keyword" content="Creative, Dashboard, Admin, Template, Theme, Bootstrap, Responsive, Retina, Minimal">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/img/icons/favicon.png">

    <title>All Posts</title>

    <!-- Bootstrap CSS -->
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <!-- bootstrap theme -->
    <link href="${pageContext.request.contextPath}/css/bootstrap-theme.css" rel="stylesheet">
    <!--external css-->
    <!-- font icon -->
    <link href="${pageContext.request.contextPath}/css/elegant-icons-style.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/font-awesome.min.css" rel="stylesheet"/>
    <!-- Custom styles -->
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style-responsive.css" rel="stylesheet"/>

    <!-- javascripts -->
    <script src="${pageContext.request.contextPath}/js/jquery.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
    <!-- nice scroll -->
    <script src="${pageContext.request.contextPath}/js/jquery.scrollTo.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/jquery.nicescroll.js" type="text/javascript"></script>
    <!-- gritter -->

    <%--<!-- custom gritter script for this page only-->--%>
    <script src="${pageContext.request.contextPath}/js/gritter.js" type="text/javascript"></script>
    <%--<!--custome script for all page-->--%>
    <script src="${pageContext.request.contextPath}/js/scripts.js"></script>
    <%--<script src="${pageContext.request.contextPath}/js/jquery.tokenize.js"></script>--%>

    <%--for table--%>
    <link href="http://cdn.datatables.net/1.10.3/css/jquery.dataTables.css" rel="stylesheet" type="text/css">
    <link href="http://datatables.net/release-datatables/extensions/ColVis/css/dataTables.colVis.css" rel="stylesheet"
          type="text/css">
    <script src="http://cdn.datatables.net/1.10.3/js/jquery.dataTables.min.js"></script>
    <script src="http://datatables.net/release-datatables/extensions/ColVis/js/dataTables.colVis.js"></script>
    <script src="http://jquery-datatables-column-filter.googlecode.com/svn/trunk/media/js/jquery.dataTables.columnFilter.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/plugin/fnStandingRedraw.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/plugin/fnSetFilteringDelay.js"></script>

    <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">

    <script src="${pageContext.request.contextPath}/js/jquery.multi-select.js" type="text/javascript"></script>
    <link href="${pageContext.request.contextPath}/css/multi-select.css" media="screen" rel="stylesheet">

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 -->
    <!--[if lt IE 9]>
    <script src="${pageContext.request.contextPath}/js/html5shiv.js"></script>
    <script src="${pageContext.request.contextPath}/js/respond.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/lte-ie7.js"></script>
    <![endif]-->


    <%--Видалення--%>
    <link href="${pageContext.request.contextPath}/css/toastr.css" rel="stylesheet" type="text/css"/>
    <script src="${pageContext.request.contextPath}/js/toastr.js"></script>
    <script type="text/javascript">

        // При завантаженні сторінки
        setTimeout(function () {
            if (${toastr_notification!=null}) {
                var args = "${toastr_notification}".split("|");
                toastrNotification(args[0], args[1]);
            }
        }, 500);

        function removeOwner(id) {
            var xmlhttp = new XMLHttpRequest();
            xmlhttp.open('GET', '/owner?action=remove&id=' + id, true);
            xmlhttp.onreadystatechange = function () {
                if (xmlhttp.readyState == 4) {
                    var response = JSON.parse(xmlhttp.responseText);
                    toastrNotification(response.status, response.msg);
                    if (response.status === 'success') {
                        $('#ownersTable').DataTable().row($(this).parents('tr'))
                                .remove().draw();
                    }
                }
            };
            xmlhttp.send(null);
        }
    </script>

    <%--Наповнення таблиці--%>
    <script type="text/javascript">
        var table;

        jQuery(document).ready(function () {
            table = $('#ownersTable').dataTable({

                "bSort": false,
                "bPaginate": true,
                "paging": true,
                "bInfo": false,
                "iDisplayStart": 0,
                "bProcessing": true,
                'iDisplayLength': 10,
                "bServerSide": true,
                "sAjaxSource": "http://localhost:8080/owner?action=get",
                colVis: {
                    "align": "right",
                    "buttonText": "columns <img src=\"/img/caaret.png\"/>",
                },

                "columnDefs": [{
                    "aTargets": [0], "createdCell": function (td, cellData, rowData, row, col) {
                        if (cellData > 0) {
                            $(td).html('<i class="fa fa-user"> ' + Math.abs(cellData) + '</i>');
                        } else {
                            $(td).html('<i class="fa fa-users"> ' + Math.abs(cellData) + '</i>');
                        }
                    }
                }, {
                    "aTargets": [1], "createdCell": function (td, cellData, rowData, row, col) {
                        if (rowData[0] > 0) {
                            $(td).html('<a href="http://vk.com/id' + rowData[0] + '" target="_blank">' + cellData + '</a>');
                        } else {
                            $(td).html('<a href="http://vk.com/public' + Math.abs(rowData[0]) + '" target="_blank">' +
                                    cellData + '</a>');
                        }
                    }
                }, {
                    "aTargets": [2], "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).html('<a class="btn btn-default" onclick="PopUpShow(' + cellData + ')"><span class="fa fa-users"></span></a>');
                    }
                }, {
                    "aTargets": [3], "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).html('<a class="btn btn-primary" onclick="showGroupStat(' + cellData +
                                ')" data-toggle="modal" data-target="#modal_stat"><span class="fa fa-bar-chart"></span></a>');
                    }
                }, {
                    "aTargets": [4], "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).html('<div class="btn-group"><a class="btn btn-success" href="#"><i class="icon_pencil-edit"></i></a><a class="btn btn-danger" onclick="removeOwner(' + cellData + ',this)"><i class="icon_close_alt2"></i></a></div>');
                    }
                }, {
                    "width": "60%", "targets": 1
                }, {
                    "class": "dt-body-left", "targets": [1]
                }, {
                    "width": "2%", "targets": [2, 3, 4]
                }]

            });

//            $(".dataTables_filter").attr("hidden", "");
            $(".dataTables_length").attr("hidden", "");
            var dataTables_filter_input = $(".dataTables_filter").find("input");
            dataTables_filter_input.attr("class", "form-control");
            dataTables_filter_input.attr("style", "width: 500px")

        })
    </script>

    <%--Привязка акаунтів--%>
    <script>

        var ownerId;

        function PopUpShow(id) {
            var xmlhttp = new XMLHttpRequest();
            xmlhttp.open('GET', '/owner?action=optionFilling&id=' + id, true);
            xmlhttp.onreadystatechange = function () {
                if (xmlhttp.readyState == 4) {
                    var response = JSON.parse(xmlhttp.responseText);
                    setOption(response);
                    ownerId = id;
                    $("#popup_bind").show();
                }
            };
            xmlhttp.send();
        }

        function setOption(response) {
            $('#tokenize_read').empty();
            $('#tokenize_read').multiSelect('refresh');
            $('#tokenize_write').empty();
            $('#tokenize_write').multiSelect('refresh');
            for (var i = 0; i < response.read.length; i++) {
                $('#tokenize_read').multiSelect('addOption', {
                    value: '' + response.read[i].id,
                    text: response.read[i].name, index: 0,
                });
                if (response.read[i].select == true) {
                    $('#tokenize_read').multiSelect('select', '' + response.read[i].id);
                }
            }
            for (var i = 0; i < response.write.length; i++) {
                $('#tokenize_write').multiSelect('addOption', {
                    value: '' + response.write[i].id,
                    text: response.write[i].name, index: 0
                });
                if (response.write[i].select == true) {
                    $('#tokenize_write').multiSelect('select', '' + response.write[i].id);
                }
            }
        }

        function PopUpHide() {
            sendSelectedField(ownerId);
            $("#popup_bind").hide();
        }

        function sendSelectedField(id) {
            var selectionRead = document.getElementById("tokenize_read").options;
            var selectionWrite = document.getElementById("tokenize_write").options;
            var read = [], write = [];
            for (var i = 0; i < selectionRead.length; i++) {
                if (selectionRead[i].selected)
                    read.push(selectionRead[i].value);
            }
            for (i = 0; i < selectionWrite.length; i++) {
                if (selectionWrite[i].selected)
                    write.push(selectionWrite[i].value);
            }
            var request = {
                read: read,
                write: write
            };
            var xmlhttp = new XMLHttpRequest();
            xmlhttp.open("POST", "/owner?action=bind&id=" + id);
            xmlhttp.onreadystatechange = function () {
                if (xmlhttp.readyState == 4) {
                    var response = JSON.parse(xmlhttp.responseText);
                    for (var i = 0; i < response.length; i++) {
                        toastrNotification(response[i].status, response[i].msg);
                    }
                }
            };
            xmlhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
            xmlhttp.send(JSON.stringify(request));
        }
    </script>

    <%--Статистика--%>
    <script>

        var ownerId;

        function showGroupStat(id) {
            ownerId = 1;
            var xmlhttp = new XMLHttpRequest();
            xmlhttp.open('GET', '/owner?action=stat&id=1', true);
            xmlhttp.onreadystatechange = function () {
                if (xmlhttp.readyState == 4) {
                    var response = JSON.parse(xmlhttp.responseText);
                    new Chart(document.getElementById("line").getContext("2d")).Line(response.line);
                    new Chart(document.getElementById("bar").getContext("2d")).Bar(response.bar);
                    new Chart(document.getElementById("pie").getContext("2d")).Pie(response.pie);
                }
            };
            xmlhttp.send();
        }

        function redrawChart() {
            var dateFrom = document.getElementById("startDate").value;
            var dateTo = document.getElementById("endDate").value;
            var xmlhttp = new XMLHttpRequest();
            xmlhttp.open('GET', '/owner?action=stat&id=' + ownerId + '&date_from=' + dateFrom + "&date_to=" + dateTo, true);
            xmlhttp.onreadystatechange = function () {
                if (xmlhttp.readyState == 4) {
                    var response = JSON.parse(xmlhttp.responseText);
                    new Chart(document.getElementById("line").getContext("2d")).Line(response.line);
                    new Chart(document.getElementById("bar").getContext("2d")).Bar(response.bar);
                    new Chart(document.getElementById("pie").getContext("2d")).Pie(response.pie);
                }
            };
            xmlhttp.send();
        }

    </script>

</head>
<body>

<!-- container section start -->
<section id="container" class="">

    <jsp:include page="../pagecontent/header.jsp"/>
    <jsp:include page="../pagecontent/sidebar.jsp"/>

    <jsp:include page="../owner/stat.jsp"/>

    <section id="main-content">
        <section class="wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h3 class="page-header" style="width: 80%"><i class="fa fa-list-alt"></i> Created</h3>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-12">
                    <ol class="breadcrumb">
                        <li><i class="fa fa-home"></i><a href="/">Home</a></li>
                        <li><i class="fa fa-desktop"></i>Post</li>
                        <li><i class="fa fa-list-alt"></i>Created</li>
                    </ol>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <div class="pull-left">Created</div>
                            <div class="clearfix"></div>
                        </div>

                        <div class="b-popup" id="popup_bind">
                            <div class="b-popup-content" style="height: 340px;">
                                <h4>Binding Accounts</h4>
                                <table style="margin-left: 12px">
                                    <tr>
                                        <th style="width:400px;">Read</th>
                                        <th style="width:400px;">Write</th>
                                    </tr>
                                    <tr>
                                        <td>
                                            <select id="tokenize_read" multiple="multiple">
                                            </select>
                                        </td>
                                        <td>
                                            <select id="tokenize_write" multiple="multiple">
                                            </select>
                                        </td>
                                    </tr>
                                </table>
                                <br>

                                <div align="right">
                                    <a href="javascript:PopUpHide()">
                                        <button class="btn btn-info" style="margin-right: 14px">Save</button>
                                    </a>
                                </div>

                            </div>
                        </div>

                        <div class="panel-body">
                            <div id="active" class="tab-pane active">
                                <div class="col-lg-12">
                                    <table width="100%" border="0" margin="0" padding="0"
                                           class="row-border tableHeader" id="ownersTable">
                                        <thead>
                                        <tr style="align-content: center">
                                            <th>id</th>
                                            <th>Name</th>
                                            <th>Binding account</th>
                                            <th>Statistics</th>
                                            <th>Action</th>
                                        </tr>
                                        </thead>
                                    </table>
                                    <a class="btn btn-primary" href="">Add group</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </section>
</section>
</body>
</html>