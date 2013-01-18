<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="head.jsp"%>
<c:choose>
    <c:when test="${empty adminUser}">
        <h2>添加用户</h2>
    </c:when>
    <c:otherwise>
        <h2>编辑用户</h2>
    </c:otherwise>
</c:choose>
        <form action="<c:url value="/admin/user" /><c:out value="/${adminUser.userId}" default="" />" method="post" id="commentform">
    <c:if test="${!empty adminUser}">
        <input type="hidden" name="_method" value="PUT" />
        <input type="hidden" name="redirect" value="/admin/users" />
    </c:if>
    <p><label for="title" style="float: left; width: 100px">用户名 *</label><input type="text" name="username" style="width: 200px" value="<c:out value="${adminUser.username}" default="" />" /></p>
    <p><label for="subtitle" style="float: left; width: 100px">显示名称 *</label><input type="text" name="displayName" value="<c:out value="${adminUser.displayName}" default="" />" style="width: 200px" /></p>
    <p><label for="subtitle" style="float: left; width: 100px">邮箱 *</label><input type="text" name="email" value="<c:out value="${adminUser.email}" default="" />" style="width: 200px" /></p>
    <p><label for="subtitle" style="float: left; width: 100px">主页</label><input type="text" name="homePage" value="<c:out value="${adminUser.homePageUrl}" default="" />" style="width: 200px" /></p>
    <c:if test="${empty adminUser}">
    </c:if>
    <c:choose>
        <c:when test="${empty adminUser}">
            <p><label for="title" style="float: left; width: 100px">密码 *</label><input type="password" name="password" style="width: 200px" /></p>
            <p><label for="title" style="float: left; width: 100px">确认密码 *</label><input type="password" name="confirmPassword" style="width: 200px" /></p>
            <p><input name="submit" type="submit" id="submit" tabindex="5" value="新建" /></p>
        </c:when>
        <c:otherwise>
            <p><a href="<c:url value="/admin/users/${adminUser.userId}/password" />">修改密码</a></p>
            <p><input name="submit" type="submit" id="submit" tabindex="5" value="更新" /></p>
        </c:otherwise>
    </c:choose>
</form>
<%@include file="foot.jsp" %>