<%--
    Document   : user
    Created on : 2012-9-9, 15:55:50
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>用户</title>
        <link href="../css/global.css" rel="stylesheet" type="text/css" />
        <link href="../css/user.css" rel="stylesheet" type="text/css"/>
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
                <p>用户</p>
                <p><a href="newUser.jsp">添加用户</a></p>
                <table width="97%" border="0" cellpadding="0" cellspacing="0">
                    <tr id="table-head">
                        <td width="20%">用户名</td>
                        <td width="10%">昵称</td>
                        <td width="15%">邮箱</td>
                        <td width="25%">主页</td>
                        <td width="15%">注册时间</td>
                        <td width="15%">上次登录时间</td>
                    </tr>
                    <c:forEach items="${adminUserList}" var="user">
                    <tr>
                        <td height="60"><a href=""><c:out value="${user.username}"/></a></td>
                        <td><c:out value="${user.displayName}"/></td>
                        <td><c:out value="${user.email}"/></td>
                        <td><c:out value="${user.homePageUrl}"/></td>
                        <td><fmt:formatDate pattern="yyyy年MM月dd日" value="${user.registerTime}" /></td>
                        <td><fmt:formatDate pattern="yyyy年MM月dd日" value="${user.lastLoginTime}" /></td>
                    </tr>
                    </c:forEach>
                </table>
            </div>
            <div class="foot">
            感谢使用<a href="#">Jelly-Jolly</a> | <a href="#">反馈</a>
            </div>
        </div>
    </body>
</html>
