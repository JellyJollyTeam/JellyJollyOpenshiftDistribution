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
        <title>链接管理</title>
        <link href="../css/links.css" rel="stylesheet" type="text/css"/>
        <link href="../css/global.css" rel="stylesheet" type="text/css" />
     <script type="text/javascript" language="javascript" src="../js/jquery.js"></script>
     <script language="javascript" src="../js/jquery-1.8.1.min.js"></script>
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
    function AddLink(){
        $("#add_Links").show();
    }
    function cancel(){
        $("#add_Links").hide();
    }
    </script>
    </head>
    <body>
        <jsp:include page = "./sidebar.jsp" flush = "true"/>
        <div class="content">
            <jsp:include page = "./head.jsp" flush="true"/>
   	<div class="panel">
      	<p>链接</p>
        <p><input id="add_link" type="button" value="添加链接" onclick="AddLink()"/></p>
        <div id="add_Links" class="hide">
            <form name="addlink" action="./link" method="post">
                <input type="hidden" name="op" value="add"/>
                <table width="98%" height="100px">
                <tr>
                    <td><label for="linkName" >链接名字</label></td>
                    <td><input type="text" size="15" name="title"/></td>
                </tr>
                <tr>
                    <td><label for="linkURL">链接地址（URL）</label></td>
                    <td><input typ="text" size="15" name="url" /></td>
                </tr>
                <tr>
                    <td><label for="linkIMG">链接图片（URL）</label></td>
                    <td><input type="text" size="15" name="image" /></td>
                </tr>
                    <tr>
                    <input id="add_link" type="submit" value="确定" />
                    <input id="add_link" type="button" value="取消" onclick="cancel()"/></tr>
                </table>
            </form>
        </div>
        <table width="97%" border="0" cellpadding="0" cellspacing="0">
            <tr id="table-head">
                <td width="25%">链接名</td>
                <td width="20%">链接地址</td>
                <td width="50%">链接图片</td>
            </tr>
            <c:forEach items="${linkList}" var="link">
            <tr>
                <td height="60"><a href="#"><c:out value="${link.title}"/></a>&nbsp;
                    <a href="link?linkid=<c:out value="${link.linkId}"/>&op=del" class="a-font">删除</a>
                </td>
                <td><c:out value="${link.url}"/></td>
                <c:choose>
                    <c:when test="${empty link.image}">
                       <td>啊哦，暂无图片</td>
                    </c:when>
                    <c:otherwise>
                        <td><img style="WIDTH: 100px!important;HEIGHT: auto!important;" src="<c:out value="${link.image}"/>"/></td>
                    </c:otherwise>
                </c:choose>
            </tr>
            </c:forEach>
        </table>
      <!--panel end--></div>
        <div class="foot">
      	感谢使用<a href="#">Jelly-Jolly</a> | <a href="#">反馈</a>
      <!--foot end--></div>
<!--content end--></div>
    </body>
</html>
