<%--
    Document   : blogs
    Created on : 2012-9-3, 17:25:27
    Author     : sceliay & fanTasy
--%>

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>管理博文</title>
        <link href="../css/blogs.css" rel="stylesheet" type="text/css"/>
        <link href="../css/global.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" language="javascript" src="../js/jquery.js"></script>
	<script type="text/javascript">
        $(document).ready(function()
        {
            //slides the element with class "menu_body" when mouse is over the paragraph
            $("#secondpane p.menu_head").mouseover(function()
            {
                 $(this).css({backgroundImage:"url(../images/down.png)"}).next("div.menu_body").slideDown(500).siblings("div.menu_body").slideUp("slow");
                 $(this).siblings().css({backgroundImage:"url(../images/left.png)"});
            });
        });
        function refresh() {
            window.location.href = '?';
        }
        function redirect(categoryId) {
            if (categoryId=='') {
                window.location.href = '?';
            } else {
                window.location.href = '?categoryid=' + categoryId;
            }
        }
        </script>
    </head>
    <body>
    <jsp:include page = "./sidebar.jsp" flush = "true"/>
    <div class="content">
        <jsp:include page = "./head.jsp" flush="true"/>
   	<div class="panel">
      	<p>文章</p>
        <p><a href="writeBlog.jsp">写文章</a> </p>
        <select id="categorySelect" style="margin-left: 0px;" onchange="redirect(this.value)">
            <option onclick="refresh()" value="">显示所有分类</option>
            <c:forEach items="${categoryList}" var="category">
                <c:choose>
                    <c:when test="${param.categoryid == category.categoryId}">
                     <option selected="selected" value="<c:out value="${category.categoryId}"/>"><a href="?categoryid=<c:out value="${category.categoryId}"/>"><c:out value="${category.name}" /></a></option>
                    </c:when>
                    <c:otherwise>
                          <option value="<c:out value="${category.categoryId}"/>"><c:out value="${category.name}" /></option>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </select>
        <table width="97%" border="0" cellpadding="0" cellspacing="0">
            <tr id="table-head">
                <td width="30%" >标题</td>
                <td width="14%" >作者</td>
                <td width="13%" >分类目录</td>
                <td width="17%" >日期</td>
            </tr>
            <c:forEach items="${postList}" var="post">
                <tr>
                    <td width="30%" height="60px"><input type="checkbox" name="choice" />
                    <a href="editBlog.jsp?postid=<c:out value="${post.postId}"/>"><c:out value="${post.title}"/></a>&nbsp;
                    <a href="editBlog.jsp?postid=<c:out value="${post.postId}"/>" class="a-font">编辑</a>|<a href="post?postid=<c:out value="${post.postId}"/>&op=del">删除</a></td>
                    <td><a href="#"><c:out value="${post.author.displayName}"/></a></td>
                    <td><a href="?categoryid=<c:out value="${post.category.categoryId}"/>"><c:out value="${post.category.name}"/></a></td>
                    <td id="data"><fmt:formatDate pattern="yyyy年MM月dd日" value="${post.date}" /></td>
                </tr>
            </c:forEach>
        </table>
	<p>
            <input type="button" name="delete" value="删除" id="button-primary"/>
        </p>
      <!--panel end--></div>
   	  <div class="foot">
              <p>感谢使用<a href="#">Jelly-Jolly</a> | <a href="#">反馈</a></p>
      <!--foot end--></div>
<!--content end--></div>
    </body>
</html>
