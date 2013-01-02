<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@include file="head.jsp"%>
<script type="text/javascript">
    function check() {
        var confirmed = window.confirm("确定删除？");
        if (confirmed) {
            document.getElementById('form').submit();
        }
    }
</script>
<h2>全部文章</h2>
<form id="form" action="<c:url value="/admin/post" />" method="POST">
    <input type="hidden" name="_method" value="DELETE" />
    <input type="hidden" name="redirect" value="/admin/posts" />
    <p><span style="float: left; width: 40px;">分类</span>
    <select style="width: 100px;">
        <c:forEach var="category" items="${categoryList}">
            <option value="${category.categoryId}">${category.name}</option>
        </c:forEach>
    </select>
    </p>
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
                <td style="padding-top: 2px; padding-left: 10px" width="34%">标题</td>
                <td style="padding-top: 2px; padding-left: 10px" width="12%">作者</td>
                <td style="padding-top: 2px; padding-left: 10px" width="10%">分类目录</td>
                <td style="padding-top: 2px; padding-left: 10px" width="17%">日期</td>
            </tr>
            <c:forEach var="post" items="${postList}">
            <tr>
                <td><input type="checkbox" name="postIds" value="${post.postId}"></td>
                <td style="padding-top: 2px; padding-left: 10px" height="60px">
                    <a href="<c:url value="/admin/posts/${post.postId}/editor" />"><c:out value="${post.title}" /></td>
                <td style="padding-top: 2px; padding-left: 10px" height="60px"><a href="#"><c:out value="${post.author.displayName}" /></a></td>
                <td style="padding-top: 2px; padding-left: 10px" height="60px"><c:out value="${post.category.name}" /></td>
                <td style="padding-top: 2px; padding-left: 10px" height="60px"><fmt:formatDate value="${post.date}" pattern="yyyy年MM月dd日" /></td>
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
<div class="navigation">
    <div class="alignleft">
        <c:if test="${hasPrev}">
            <a href="<c:url value="/admin/posts/page/${pageNum - 1}" />">较新的</a>
        </c:if>
    </div>
    <div class="alignright">
        <c:if test="${hasNext}">
            <a href="<c:url value="/admin/posts/page/${pageNum + 1}" />">较早的</a>
        </c:if>
    </div>
</div>
<%@include file="foot.jsp" %>