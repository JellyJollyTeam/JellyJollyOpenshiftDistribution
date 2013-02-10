<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="head.jsp"%>
<script type="text/javascript">
    function check() {
        var confirmed = window.confirm("确定删除？");
        if (confirmed) {
            document.getElementById('form').submit();
        }
    }
</script>
<h2>所有用户</h2>
<form id="form" action="<c:url value="/admin/user" />" method="POST">
    <input type="hidden" name="_method" value="DELETE" />
    <input type="hidden" name="redirect" value="/admin/users" />
    <input type="button" value="删除" style="
        width:80px;
        border:none;
        background:#343434;
        font-size:11px;
        padding:0;
        color:#fff;
        font-size:12px;
        cursor:pointer;
        padding:2px 0 3px;" onclick="check()" />
    <table style="margin-top: 10px; margin-bottom: 10px;">
        <tbody>
            <tr>
                <td width="1%">&nbsp;</td>
                <td style="padding-top: 2px; padding-left: 10px" width="17%">显示名字</td>
                <td style="padding-top: 2px; padding-left: 10px" width="10%">用户名</td>
                <td style="padding-top: 2px; padding-left: 10px" width="15%">邮箱</td>
                <td style="padding-top: 2px; padding-left: 10px" width="30%">主页</td>
                <td style="padding-top: 2px; padding-left: 10px" width="12%">功能</td>
            </tr>
            <c:forEach var="user" items="${adminUserList}">
            <tr>
                <td style="padding-top: 2px; padding-left: 10px" height="60px"><input type="checkbox" name="userIds" value="${user.userId}"></td>
                <td style="padding-top: 2px; padding-left: 10px" height="60px"><a href="<c:url value="/admin/users/${user.userId}" />"><c:out value="${user.displayName}" /></a></td>
                <td style="padding-top: 2px; padding-left: 10px" height="60px"><a href="<c:url value="/admin/users/${user.userId}" />"><c:out value="${user.username}" /></a></td>
                <td style="padding-top: 2px; padding-left: 10px" height="60px"><c:out value="${user.email}" /></td>
                <td style="padding-top: 2px; padding-left: 10px" height="60px"><c:out value="${user.homePageUrl}" /></td>
                <td style="padding-top: 2px; padding-left: 10px" height="60px">
                    <a href="<c:url value="/admin/users/${user.userId}/password" />">修改密码</a>
                </td>
            </tr>
            </c:forEach>
        </tbody>
    </table>
    <input type="button" value="删除" style="
        width:80px;
        border:none;
        background:#343434;
        font-size:11px;
        padding:0;
        color:#fff;
        font-size:12px;
        cursor:pointer;
        padding:2px 0 3px;" onclick="check()" />
</form>
<%@include file="foot.jsp" %>