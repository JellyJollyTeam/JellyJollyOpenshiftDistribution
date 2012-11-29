<%--
    Document   : newjsp
    Created on : 2012-9-3, 15:49:15
    Author     : sceliay & fanTasy
--%>

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="jj" uri="/WEB-INF/tlds/getPostTitle.tld" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title><c:out value="${userAuth.user.displayName}" />的控制板</title>
        <link href="../css/admin.css" rel="stylesheet" type="text/css" />
        <link href="../css/global.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" language="javascript" src="../js/jquery.js"></script>
        <script language="javascript" src="jquery-1.8.1.min.js"></script>
        <script language="javascript">
        /*$(function(){
            $("#label-button").click(function(){
                    //$(this).toggleClass("hide");
                    //$("#hello").toggleClass("show");
                    $(this).hide();
                    $("#hello").show();
            });
            $("#delete").click(function(){
                    $("#hello").hide();
                    $("#label-button").show();
            });
        });*/
        $(document).ready(function()
        {
            //slides the element with class "menu_body" when mouse is over the paragraph
            $("#secondpane p.menu_head").mouseover(function()
            {
                 $(this).css({backgroundImage:"url(../images/down.png)"}).next("div.menu_body").slideDown(500).siblings("div.menu_body").slideUp("slow");
                 $(this).siblings().css({backgroundImage:"url(../images/left.png)"});
            });
        });
        function checkClassify(){
            var oForm = document.forms["publish"];
            var oSelectBox = oForm.categoryid;
            var oChoice = oSelectBox.selectedIndex;
            if(oSelectBox.options[oChoice].text == "+添加分类"){
                $("#addBox").show();
            }
            else
                $("#addBox").hide();

        }
        </script>
    </head>

    <body topmargin="0">
        <jsp:include page = "./sidebar.jsp" flush = "true"/>
            <div class="content">
            <jsp:include page = "./head.jsp" flush="true"/>
            <div class="panel">
                <p>控制板</p>
                <div class="basic">
                <div class="panel-head">概况</div>
                    <table width="100%" border="0" >
                        <tr>
                            <td height="30" colspan="2" id="basic-head2">内容</td>
                            <td colspan="2" id="basic-head2">讨论</td>
                        </tr>
                        <tr>
                            <td width="9%" height="30"><a href="blogs.jsp"><c:out value="${blogstat.postCount}"/></a></td>
                            <td width="41%"><a href="blogs.jsp">文章</a></td>
                            <td width="9%"><a href="comments.jsp"><c:out value="${blogstat.commentCount}"/></a></td>
                            <td width="41%"><a href="comments.jsp">评论</a></td>
                        </tr>
                        <tr>
                            <td height="30"><a href="pages.jsp"><c:out value="${blogstat.pageCount}"/></a></td>
                            <td><a href="pages.jsp">页面</a></td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td height="30"><a href="blogs.jsp"><c:out value="${blogstat.categoryCount}"/></a></td>
                            <td><a  href="blogs.jsp">分类目录</a></td>
                            <td></td>
                            <td></td>
                        </tr>
                    </table>
                <!--basic end--></div>
                <div class="published">
                            <div class="panel-head">快速发表</div>
                    <form method="post" name="publish" action="./post">
                        <input type="hidden" name="op" value="post" />
                        <p><label for="text-name">标题</label>
                            <input type="text" name="title" size="50" class="published-input"/></p>
                        <select style="margin-left: 47px;" name="categoryid" id="categoryid" onchange="checkClassify()">
                            <option value="选择分类">选择分类</option>
                            <c:forEach items="${categoryList}" var="category">
                                <option value="<c:out value="${category.categoryId}"/>"><c:out value="${category.name}" /></option>
                            </c:forEach>
                            <option style="color:red;" value="addoption">+添加分类</option>
                        </select>
                        <input type="text" name="text" size="10" id = "addBox" class="hide" placeholder="输入分类名"/>
                        <p><label for="content">内容</label>
                            <textarea name="content" cols="40" rows="5" class="published-input"></textarea></p>
                        <p><input  style="margin-left:45px;" type="submit" name="publish" value="发布" id="button-primary"/></p>
                    </form>
                <!--publsihed end--></div>

                    <div class="comments">
                            <div class="panel-head">近期评论</div>
                            <c:forEach items="${commentlist}" var="currentComment">
                            <div class="comments-one">
                                <p style="font-size: 14px;">由<a href="#"><c:out value="${currentComment.authorName}"/></a>发表在《<a href="../post.jsp?postid=<c:out value="${currentComment.postId}"/>"><jj:getPostTitle postId="${currentComment.postId}" /></a>》</p>
                            <p style="font-size: 14px; white-space: pre; white-space: -moz-pre-wrap; white-space: pre-wrap; white-space: pre-line;word-wrap: break-word;" ><c:out value="${currentComment.content}"/></p>
                            <p style="font-size:12px;"><a href="comment?commentid=<c:out value="${currentComment.commentId}"/>&op=del">删除</a></p>
                            <!--comments-one end--></div>
                            </c:forEach>
                    <!--comments end--></div>
          <!--panel end--></div>
          <div class="foot">
            <p>感谢使用<a href="#">Jelly-Jolly</a> | <a href="#">反馈</a></p>
          <!--foot end--></div>
      <!--content end--></div>
    </body>
</html>