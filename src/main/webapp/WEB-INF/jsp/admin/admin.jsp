<%--
    Document   : admin.jsp
    Created on : 2012-9-3, 15:49:15
    Author     : sceliay & fanTasy
--%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="jj" uri="/WEB-INF/tlds/getPostTitle.tld" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="head.jsp"%>
<h2 style="margin-bottom: 10px;">面板主页</h2>
<h3 style="margin-bottom: 10px;">概况</h3>
<ul style="margin-bottom: 35px;">
    <li><a href="<c:url value="/admin/posts" />">文章 (${postNumber})</a></li>
    <li><a href="<c:url value="/admin/comments" />">评论 (${commentNumber})</a></li>
    <li><a href="<c:url value="/admin/pages" />">页面 (${pageNumber})</a></li>
</ul>
<h3 style="margin-bottom: 10px;">最近评论</h3>
<ul style="margin-bottom: 35px;">
    <c:forEach var="comment" items="${recentComments}">
        <li>${comment.authorName} 评论了《<a href="<c:url value="/post/${comment.postId}" />"><jj:getPostTitle postId="${comment.postId}" /></a>》</li>
    </c:forEach>
</ul>
<h3 style="margin-bottom: 10px;">快速发表</h3>
<form action="<c:url value="/admin/post/simple" />" method="post" id="commentform">
    <input type="hidden" name="redirect" value="/" />
    <p>标题<br /><input type="text" name="title" /></p>
    <p>分类<br />
        <select name="categoryId" style="width: 100px;">
            <c:forEach var="category" items="${categoryList}">
                <option value="${category.categoryId}">${category.name}</option>
            </c:forEach>
        </select>
    </p>
    <p>正文<br />
        <textarea name="content" id="comment" cols="100%" rows="10" tabindex="2"></textarea>
    </p>
    <p><input name="submit" type="submit" id="submit" tabindex="5" value="发表" />
    </p>
    <p style="display: none;"></p>
</form>
<%@include file="foot.jsp" %>