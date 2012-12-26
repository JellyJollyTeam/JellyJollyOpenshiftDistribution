<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" 、>
        <link rel="stylesheet" href="<c:url value="/static/style.css" />" type="text/css" media="screen" />
        <title>安装您的JellyJolly博客</title>
    </head>
    <body>
        <div id="page">
            <div id="header">
                <h1>安装您的JellyJolly博客</h1>
                <ul id="nav">
                    <li><a href="https://github.com/JellyJollyTeam/JellyJollyOpenshiftDistribution">访问我们的主页</a></li>
                    <li><a href="https://github.com/JellyJollyTeam/JellyJollyOpenshiftDistribution">参与我们的开发</a></li>
                    <li><a href="https://github.com/JellyJollyTeam/">关注我们的组织</a></li>
                </ul>
            </div>
            <div id="content">
                <h2>请您填写以下信息</h2>
                <form action="<c:url value="/install" />" method="post" id="commentform">
                    <p><label for="title" style="float: left; width: 100px">博客标题 *</label><input type="text" name="title" style="width: 200px" /></p>
                    <p><label for="subtitle" style="float: left; width: 100px">博客副标题 *</label><input type="text" name="subtitle" style="width: 200px" /></p>
                    <p><label for="subtitle" style="float: left; width: 100px">您的登录用户名 *</label><input type="text" name="username" style="width: 200px" /></p>
                    <p><label for="subtitle" style="float: left; width: 100px">您的名字 *</label><input type="text" name="displayName" style="width: 200px" /></p>
                    <p><label for="subtitle" style="float: left; width: 100px">您的邮箱 *</label><input type="text" name="email" style="width: 200px" /></p>
                    <p><label for="subtitle" style="float: left; width: 100px">登录密码 *</label><input type="password" name="password" style="width: 200px" /></p>
                    <p><label for="subtitle" style="float: left; width: 100px">再次输入密码 *</label><input type="password" name="confirmPassword" style="width: 200px" /></p>
                    <p><input name="submit" type="submit" id="submit" tabindex="5" value="完成" /></p>
                </form>
            </div>
        </div>
    </body>
</html>
