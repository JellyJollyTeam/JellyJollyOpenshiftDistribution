<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="head.jsp"%>
<c:choose>
    <c:when test="${empty link}">
        <h2>新建链接</h2>
    </c:when>
    <c:otherwise>
        <h2>编辑链接</h2>
    </c:otherwise>
</c:choose>
<script type="text/javascript">
    function checkPublishLink() {
        var title = document.getElementById('title');
        if (title.value == '') {
            alert('请输入标题');
            title.focus();
            return false;
        }
        return true;
    }
</script>
<form action="<c:url value="/admin/link" /><c:out value="/${link.linkId}" default="" />" method="POST" id="commentform" onsubmit="return checkPublishLink();">
    <c:if test="${!empty link}">
        <input type="hidden" name="_method" value="PUT" />
    </c:if>
    <p>标题<br /><input type="text" id="title" name="title" value="<c:out value="${link.title}" default="" />" /></p>
    <p>链接<br /><input type="text" id="url" name="url" value="<c:out value="${link.url}" default="" />" /></p>
    <p>
        <c:choose>
            <c:when test="${empty link}">
                <input name="submit" type="submit" id="submit" value="发表" />
            </c:when>
            <c:otherwise>
                <input name="submit" type="submit" id="submit" value="更新" />
            </c:otherwise>
        </c:choose>
    </p>
</form>
<%@include file="foot.jsp" %>