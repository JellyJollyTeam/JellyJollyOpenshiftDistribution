<%--
    Document   : forgetpass
    Created on : 2012-9-4, 14:54:13
    Author     : sceliay & fanTasy
--%>
<%@page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>忘记密码</title>
        <link href="<c:url value="/static/login.css" />" rel="stylesheet" type="text/css"/>
        <script type="text/javascript">
            window.onload = function() {
                document.getElementById('user_login').focus();
            }
        </script>
    </head>
    <body>
        <div id="login">
            <h1>
                <a href="#" title="欢迎使用JellyJolly"></a>
            </h1>
            <c:if test="${param.done==1}">
                <p style="margin-left:10px; color: grey">
                    邮件已发送
                </p>
            </c:if>
            <c:if test="${param.error==1}">
                <p style="margin-left:10px;">
                    用户名不存在
                </p>
            </c:if>
            <c:if test="${param.error==2}">
                <p style="margin-left:10px;">
                    邮件发送时发送了错误
                </p>
            </c:if>
            <form name="loginForm" action="forgetpass" method="post" id="loginform">
                <p>
                    <label>
                        用户名
                    </label>
                    <input type="text" name="username" id="user_login" class="input" size="20"/>
                </p>
                <p>
                    <input style="float: right;" type="submit" name="login" value="获取新密码" class="button-primary" tabindex="100"/>
                </p>
            </form>
            <p id="nav">
                <p style="margin-left: 10px; margin-bottom: 10px"><a href="<c:url value="/login" />" title="回到主页" style="text-decoration: none;">&lt;&lt;&nbsp;回到登录</a></p>
                <p style="margin-left: 10px; margin-bottom: 10px"><a href="<c:url value="/" />" title="回到主页" style="text-decoration: none;">&lt;&lt;&nbsp;回到主页</a></p>
            </p>
        </div>
    </body>
</html>
