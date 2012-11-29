<%--
    Document   : page.jsp
    Created on : 2012-9-6, 16:16:34
    Author     : sceliay & fanTasy
--%>

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><c:out value="${blogInfo.blogTitle}"/> - <c:out value="${blogpage.pageTitle}"/></title>
        <link href="css/viewBlog.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <div class="container">
        <div class="header">
            <h1 id="blog-name"><a href="home.jsp"><c:out value="${blogInfo.blogTitle}"/></a></h1>
            <h4 id="blog-name2"><c:out value="${blogInfo.blogSubTitle}"/></h4>
            <img src="images/shore.jpg" width="1024" height="45%" alt="img" />
        </div>
        <div class="centerbar">
        	<ul class="nav">
            	<li><a href="home.jsp">首页</a></li>
      		<!--the page created by the user-->
                <c:forEach items="${pageList}" var="page">
                    <li>
                        <a href="page.jsp?pageid=<c:out value="${page.blogPageId}"/>">
                    <c:out value="${page.pageTitle}"/></a>
                    </li>
                </c:forEach>
            </ul>
        </div>
        <div class="main">
        	<div class="text">
                    <h2 style="text-align: center;"><c:out value="${blogpage.pageTitle}"/></h2>
                    <p id="passage" style="text-align: left;"><c:out value="${blogpage.pageContent}" escapeXml="false" /></p>
                </div>
       </div>
        <jsp:include page="foot.jsp" flush="true"/>
       </div>
    </body>
</html>
