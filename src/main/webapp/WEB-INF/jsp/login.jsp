<%--
    Document   : login
    Created on : 2012-9-4, 14:54:13
    Author     : sceliay & fanTasy
--%>

<%@page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>登录您的Jelly-Jolly账户</title>
        <link href="./css/login.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <div id="login">
            <h1>
                <a href="www.Jelly-Jolly.com" title="欢迎使用Jelly-Jolly"></a>
            </h1>
            <c:if test="${param.error==1}">
                <p style="margin-left:10px; color:red;">
                    用户名或者密码错误
                </p>
            </c:if>
            <form name="loginForm" action="login" method="post" id="loginform">
                <p>
                    <label>
                        用户名
                    </label>
                    <input type="text" name="username" id="user_login" class="input" size="20"/>
                </p>
                <p>
                    <label>
                        密码
                    </label>
                    <input type="password" name="password" id="user_login" class="input" size="20"/>
                </p>
                <p>
                    <label>
                        <input name="rememberMe" type="checkbox" id="rememberMe" value="true" tabindex="90"/>
                        记住我的信息
                    </label>
                </p>
                <p>
                    <input style="float: right;" type="submit" name="login" value="登录" class="button-primary" tabindex="100"/>
                    <input type="hidden" name="redirect" value="#"/>
                </p>
            </form>
            <p id="nav">
                <a href="*" title="找回密码">忘记密码</a>
            </p>
        </div>
    </body>
</html>
