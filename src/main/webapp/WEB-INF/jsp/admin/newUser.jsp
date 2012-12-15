<%--
    Document   : userSet
    Created on : 2012-9-4, 10:24:08
    Author     : sceliay & fanTasy
--%>

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>创建新用户</title>
        <link href="../css/userSet.css" rel="stylesheet" type="text/css"/>
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
      	<p>添加用户</p>
        <form name="set" action="./user" method="post">
            <input type="hidden" name="op" value="add"/>
        <h4>姓名</h4>
        <p><label for="userName">用户名</label>
            <input type="text" name="username" size="30" class="form-input" /></p>
        <p><label for="userNickName">昵称（必填）</label>
            <input type="text" name="displayname" size="30" class="form-input"/></p>
        <h4>联系信息</h4>
        <p><label for="mail">电子邮件（必填）</label>
            <input type="text" name="email" size="30" class="form-input"/></p>
        <p><label for="site">个人主页</label>
            <input type="text" name="homepage" size="30" class="form-input" /></p>
        <p><label for="pwd">新密码</label><input type="password" name="newpass" size="20" class="form-input-pwd" />
            <span>如果您想修改您的密码，请在此输入新密码。不然请留空。</span></p>
        <p><label for="pwd2">确认信密码</label><input type="password" size="20" class="form-input-pwd" />
            <span>请再次输入密码</span></p>
    	<p><input type="submit" name="btnSubmit" value="确定创建" id="button-primary"/></p>
        </form>
      <!--panel end--></div>
<div class="foot">
   	感谢使用<a href="#">Jelly-Jolly</a> | <a href="#">反馈</a>
    <!--foot end--></div>
<!--content end--></div>
    </body>
</html>

