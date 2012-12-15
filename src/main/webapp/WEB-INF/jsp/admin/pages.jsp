<%--
    Document   : pages of the blog
    Created on : 2012-9-3, 17:24:15
    Author     : sceliay & fanTasy
--%>

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>页面管理</title>
        <link href="../css/pages.css" rel="stylesheet" type="text/css"/>
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
    </script>
    </head>
    <body>
        <jsp:include page = "./sidebar.jsp" flush = "true"/>
        <div class="content">
        <jsp:include page = "./head.jsp" flush="true"/>
   	<div class="panel">
      	<p>页面</p>
        <p><a href="newPage.jsp">新建页面</a></p>
        <table width="97%" border="0" cellpadding="0" cellspacing="0">
            <tr id="table-head">
                <td width="47%">页面名</td>
            </tr>
            <c:forEach items="${pageList}" var="currentPage">
                <tr>
                    <td height="60" width="47%" ><a href="#"><c:out value="${currentPage.pageTitle}"/></a>&nbsp;
                        <a href="editPage.jsp?pageid=<c:out value="${currentPage.blogPageId}"/>" class="a-font">编辑</a>|<a href="page?pageid=<c:out value="${currentPage.blogPageId}"/>&op=del" class="a-font">删除</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
	<p>
            <input type="button" name="delete" value="删除" id="button-primary"/>
        </p>
      <!--panel end--></div>
        <div class="foot">
      	感谢使用<a href="#">Jelly-Jolly</a> | <a href="#">反馈</a>
      <!--foot end--></div>
<!--content end--></div>
    </body>
</html>
