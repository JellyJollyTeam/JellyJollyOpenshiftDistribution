<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="head.jsp"%>
<c:choose>
    <c:when test="${empty blogpage}">
        <h2>新建页面</h2>
    </c:when>
    <c:otherwise>
        <h2>编辑页面</h2>
    </c:otherwise>
</c:choose>
<script type="text/javascript">
    function checkPublishPage() {
        var title = document.getElementById('title');
        if (title.value == '') {
            alert('请输入标题');
            title.focus();
            return false;
        }
        return true;
    }
</script>
<form action="<c:url value="/admin/page" /><c:out value="/${blogpage.blogPageId}" default="" />" method="POST" id="commentform" onsubmit="return checkPublishPage();">
    <c:if test="${!empty blogpage}">
        <input type="hidden" name="_method" value="PUT" />
    </c:if>
    <p>标题<br /><input type="text" id="title" name="title" value="<c:out value="${blogpage.pageTitle}" default="" />" /></p>
    <script src="<c:url value="/static/ckeditor/ckeditor.js" />"></script>
    <p>正文<br />
        <textarea name="content" id="editor1"><c:out value="${blogpage.pageContent}" default="" /></textarea>
    </p>
    <p>
        <c:choose>
            <c:when test="${empty blogpage}">
                <input name="submit" type="submit" id="submit" value="发表" />
            </c:when>
            <c:otherwise>
                <input name="submit" type="submit" id="submit" value="更新" />
            </c:otherwise>
        </c:choose>
    </p>
    <script>
        CKEDITOR.replace('editor1');
    </script>
</form>
<%@include file="foot.jsp" %>