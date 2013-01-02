<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="head.jsp"%>
<h2>全部页面</h2>
<script type="text/javascript">
    function confirm() {
        var confirmed = window.confirm("确定删除？");
        if (confirmed) {
            document.getElementById('form').submit();
        }
    }
</script>
<form id="form" action="<c:url value="/admin/page" />" method="POST">
    <input type="hidden" name="_method" value="DELETE" />
    <input type="hidden" name="redirect" value="/admin/pages" />
    <input type="button" value="删除" style="
        width:80px;
        border:none;
        background:#343434;
        font-size:11px;
        padding:0;
        color:#fff;
        font-size:12px;
        cursor:pointer;
        padding:2px 0 3px;" onclick="confirm()" />
    <table style="margin-top: 10px; margin-bottom: 10px;">
        <tbody>
            <tr>
                <td width="1%">&nbsp;</td>
                <td style="padding-top: 2px; padding-left: 10px" width="80%">标题</td>
            </tr>
            <c:forEach var="page" items="${pageList}">
            <tr>
                <td><input type="checkbox" name="pageIds" value="${page.blogPageId}"></td>
                <td style="padding-top: 2px; padding-left: 10px" height="60px">
                    <a href="<c:url value="/admin/pages/${page.blogPageId}/editor" />"><c:out value="${page.pageTitle}" /></td>
            </tr>
            </c:forEach>
        </tbody>
    </table>
    <input type="submit" value="删除" style="
        width:80px;
        border:none;
        background:#343434;
        font-size:11px;
        padding:0;
        color:#fff;
        font-size:12px;
        cursor:pointer;
        padding:2px 0 3px;" onclick="confirm()" />
</form>
<%@include file="foot.jsp" %>