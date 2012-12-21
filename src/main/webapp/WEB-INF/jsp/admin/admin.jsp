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
    <li><a href="#">文章 (11)</a></li>
    <li><a href="#">评论 (39)</a></li>
    <li><a href="#">页面 (5)</a></li>
</ul>
<h3 style="margin-bottom: 10px;">最近评论</h3>
<ul style="margin-bottom: 35px;">
    <li>张三 评论了《<a href="#">我的第一篇博客</a>》</li>
    <li>张三 评论了《<a href="#">我的第一篇博客</a>》</li>
    <li>张三 评论了《<a href="#">我的第一篇博客</a>》</li>
</ul>
<h3 style="margin-bottom: 10px;">快速发表</h3>
<form action="#" method="post" id="commentform">
    <p>标题<br /><input type="text" name="title" /></p>
    <p>分类<br />
        <select style="width: 100px;">
            <option>默认</option>
            <option>日记</option>
        </select>
    </p>
    <p>正文<br />
        <textarea name="content" id="comment" cols="100%" rows="10" tabindex="2"></textarea>
    </p>
    <p><input name="submit" type="submit" id="submit" tabindex="5" value="发表" />
        <input type='hidden' name='comment_post_ID' value='20' id='comment_post_ID' />
        <input type='hidden' name='comment_parent' id='comment_parent' value='0' />
    </p>
    <p style="display: none;"></p>
</form>
<%@include file="foot.jsp" %>