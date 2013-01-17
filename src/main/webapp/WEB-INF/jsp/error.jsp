<%--
    Document   : error
    Created on : 2013-1-5, 20:03:26
    Author     : rAy <predator.ray@gmail.com>
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" dir="ltr" lang="en-US">
    <head profile="http://gmpg.org/xfn/11">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>JellyJolly演示</title>

        <link rel="stylesheet" href="<c:url value="/static/style.css" />" type="text/css" media="screen" />

        <meta name="robots" content="noindex,nofollow" />
        <meta name="generator" content="WordPress 3.4.2" />
    </head>

    <body class="archive category category-general category-1">
        <div id="page">

            <div id="header">
                <a href="javascript:void(0);" id="sitename">错误页面</a>
            </div>
            <div id="content">
                <small class="caps">&nbsp;</small>
                <h1 class="errortitle">500 - 出错了额 ╮(╯▽╰)╭ </h1>
                <h3>${exception.localizedMessage}</h3>
                <c:forEach var="stackTrack" items="${exception.stackTrace}">
                    <p><c:out value="${stackTrace}" /></p>
                </c:forEach>
                <p><a href="https://github.com/JellyJollyTeam/JellyJollyOpenshiftDistribution">报告这个错误</a></p>
            </div>
            <div class="clear"></div>
            <div id="footer">
                <div class="alignright">
                </div>
                <a href="http://www.tammyhartdesigns.com/fifty-fifth-street">Fifty Fifth Street</a> theme by <a href="http://www.tammyhartdesigns.com/">Tammy Hart Designs</a><br />
                    Powered by <a href="https://github.com/JellyJollyTeam">JellyJolly</a>
            </div>
        </div>
    </body>
</html>
