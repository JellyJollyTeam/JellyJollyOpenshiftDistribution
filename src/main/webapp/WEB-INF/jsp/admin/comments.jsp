<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="jj" uri="/WEB-INF/tlds/getPostTitle.tld" %>
<%@include file="head.jsp"%>
<h2>全部文章</h2>
<form action="#" method="POST">
    <input type="submit" value="删除" style="
        width:80px;
        border:none;
        background:#343434;
        font-size:11px;
        padding:0;
        color:#fff;
        font-size:12px;
        cursor:pointer;
        padding:2px 0 3px;" />
    <table style="margin-top: 10px; margin-bottom: 10px;">
        <tbody>
            <tr>
                <td width="1%">&nbsp;</td>
                <td style="padding-top: 2px; padding-left: 10px" width="25%">内容</td>
                <td style="padding-top: 2px; padding-left: 10px" width="17%">发表于</td>
                <td style="padding-top: 2px; padding-left: 10px" width="12%">作者</td>
                <td style="padding-top: 2px; padding-left: 10px" width="4%">主页</td>
                <td style="padding-top: 2px; padding-left: 10px" width="17%">日期</td>
            </tr>
            <c:forEach var="comment" items="${commentList}">
            <tr>
                <td><input type="checkbox" name="choice"></td>
                <td style="padding-top: 2px; padding-left: 10px" height="60px">
                    <a href="<c:url value="/post/${comment.postId}" />"><c:out value="${comment.content}" /></td>
                    <td><a href="<c:url value="/post/${comment.postId}" />"><jj:getPostTitle postId="${comment.postId}" /></a></td>
                <td style="padding-top: 2px; padding-left: 10px" height="60px"><c:out value="${comment.authorName}" /></td>
                <td style="padding-top: 2px; padding-left: 10px" height="60px"><a href="<c:out value="${comment.authorHomePageUrl}" default="javascipt:void(0);" />">
                        <c:choose>
                            <c:when test="${comment.authorHomePageUrl}">前往</c:when>
                            <c:otherwise>（无）</c:otherwise>
                        </c:choose>
                    </a></td>
                <td style="padding-top: 2px; padding-left: 10px" height="60px"><fmt:formatDate value="${comment.date}" pattern="yyyy年MM月dd日" /></td>
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
        padding:2px 0 3px;" />
</form>
<div class="navigation">
    <div class="alignleft">
        <c:if test="${hasPrev}">
            <a href="<c:url value="/admin/comments/page/${pageNum - 1}" />">上一页</a>
        </c:if>
    </div>
    <div class="alignright">
        <c:if test="${hasNext}">
            <a href="<c:url value="/admin/comments/page/${pageNum + 1}" />">下一页</a>
        </c:if>
    </div>
</div>
<%@include file="foot.jsp" %>