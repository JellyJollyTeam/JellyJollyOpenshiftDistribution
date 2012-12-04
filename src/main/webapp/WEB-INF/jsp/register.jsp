<%--
    Document   : register
    Created on : 2012-9-4, 19:07:13
    Author     : sceliay & fanTasy
--%>

<%@page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>注册您的Jelly-Jolly账户</title>
        <link rel="stylesheet" href="login.css" type="text/css" />
        <script language="javascript">
            function CheckPwd(){
                    if($("#pwd1").val()!=$("#pwd2").val()){
                            $("#wrong").show();
                            $("#ok").hide();
                    }
                    else{
                            $("#wrong").hide();
                            $("#ok").show();
                    }
            }
        </script>
    </head>
    <body>
        <div id="register">
            <h1>
                <a href="www.Jelly-Jolly.com" title="欢迎使用Jelly-Jolly"></a>
            </h1>
            <form action="" method="post" id="registerform">
                <p>
                    <label for="username">用户名</label><br />
                    <input type="text" name="username" size="20" class="input"/>
                </p>
                <p>
                    <label for="displayname">昵称</label><br />
                    <input type="text" name="displayname" size="20" class="input"/>
                </p>
                <p>
                    <label for="email">电子邮件</label><br />
                    <input type="text" name="email" size="30" class="input"/>
                </p>
                <p>
                    <label for="homePage">个人主页</label><br />
                    <input type="text" name="homePage" size="30" class="input"/>
                </p>
                <p>
                <label for="password">密码</label><br />
                <input type="password" name="password" size="20" class="input" id="pwd1"/>
                </p>
                <p>
                    <label for="comfirmPwd">确认密码</label><br />
                    <input type="password" name="password" size="20" class="input" id="pwd2" onblur="CheckPwd()"/>
                    <span id="ok" class="hide"><label>ok!</label></span>
                    <span id="wrong" class="hide"><label>sorry.两次输入密码不同</label></span>
                </p>
                <p>
                    <input type="submit" name="register" value="注册" class="button-primary" tabindex="100"/>
                    <input type="hidden" name="redirect" value="#"/>
                </p>
            </form>
        </div>
    </body>
</html>
