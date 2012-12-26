<%--
    Document   : admin.jsp
    Created on : 2012-9-3, 15:49:15
    Author     : sceliay & fanTasy
--%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="jj" uri="/WEB-INF/tlds/getPostTitle.tld" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="head.jsp"%>
<h2>基本设置</h2>
<form action="<c:url value="/admin/settings" />" method="post" id="commentform">
    <p><label for="title" style="float: left; width: 100px">博客标题</label><input type="text" name="title" style="width: 200px" value="${blogInfo.blogTitle}" /></p>
    <p><label for="subtitle" style="float: left; width: 100px">博客副标题</label><input type="text" name="subtitle" value="${blogInfo.blogSubTitle}" style="width: 200px" /></p>
    <p><input name="submit" type="submit" id="submit" tabindex="5" value="更改" /></p>
</form>
<%@include file="foot.jsp" %>