<%--
    Document   : show all the comments and the operations to them.
    Created on : 2012-9-3, 17:18:32
    Author     : sceliay & fanTasy
--%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="jj" uri="/WEB-INF/tlds/getPostTitle.tld" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>管理评论</title>
        <link href="../css/comments.css" rel="stylesheet" type="text/css"/>
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
      	<p>评论</p>
        <table width="97%"  border="0" cellpadding="0" cellspacing="0">
            <tr id="table-head">
              <td width="35%" >作者</td>
              <td width="24%" >评论</td>
              <td width="24%" >评论给</td>
              <td width="17%" >日期</td>
            </tr>
            <c:forEach items="${commentlist}" var="comment">
            <tr>
              <td height="60" width="35%" ><input type="checkbox" name="choice" />
                  <a href="#"><c:out value="${comment.authorName}"/></a>&nbsp;
                  <a href="comment?commentid=<c:out value="${comment.commentId}"/>&op=del" class="a-font">删除</a></td>
              <td style="white-space: pre; white-space: -moz-pre-wrap; white-space: pre-wrap; white-space: pre-line;word-wrap: break-word;"><c:out value="${comment.content}"/></td>
              <td>《<a href="../post.jsp?postid=<c:out value="${comment.postId}"/>#comments"><jj:getPostTitle postId="${comment.postId}" /></a>》</td>
              <td id="data"><fmt:formatDate pattern="yyyy年MM月dd日" value="${comment.date}" /></td>
            </tr></c:forEach>
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
