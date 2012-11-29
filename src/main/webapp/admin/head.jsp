<%--
    Document   : head
    Created on : 2012-9-7, 8:42:27
    Author     : rY & fanTasy
--%>

<%@page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="header">
    <img src="../images/logo-x.png" width="50" height="36" />
    <div id="back"><a href="../home.jsp"><c:out value="${blogInfo.blogTitle}"/></a></div>

    <h4 id="welcome">你好，<c:out value="${userAuth.user.displayName}"/></h4>
</div>