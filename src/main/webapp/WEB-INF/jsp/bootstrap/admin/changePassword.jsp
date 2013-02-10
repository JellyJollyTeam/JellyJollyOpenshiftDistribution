<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="head.jsp"%>
<h2>修改密码</h2>
<form action="<c:url value="/admin/user" /><c:out value="/${adminUser.userId}/password" />" method="post" id="commentform">
    <input type="hidden" name="_method" value="PUT" />
    <input type="hidden" name="redirect" value="/admin/users" />
    <p><label for="title" style="float: left; width: 100px">旧的密码 *</label><input type="password" name="oldPassword" style="width: 200px" /><c:if test="${param.err==1}">&nbsp;<span style="color: red;">密码错误</span></c:if></p>
    <p><label for="subtitle" style="float: left; width: 100px">新的密码 *</label><input type="password" name="newPassword" style="width: 200px" />&nbsp;<c:if test="${param.err==2}">&nbsp;<span style="color: red;">密码不能为空</span></c:if></p>
    <p><label for="subtitle" style="float: left; width: 100px">确认密码 *</label><input type="password" name="confirmPassword" style="width: 200px" />&nbsp;<c:if test="${param.err==3}">&nbsp;<span style="color: red;">密码不一致</span></c:if></p>
    <p><input name="submit" type="submit" id="submit" tabindex="5" value="更新" /></p>
</form>
<%@include file="foot.jsp" %>