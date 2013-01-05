<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions' %>
<%@include file="head.jsp" %>
<c:forEach items="${postList}" var="post" varStatus="varStatus">
    <div class="post-261 post type-post status-publish format-standard hentry category-general">
        <small class="caps"><fmt:formatDate pattern="yyyy年MM月dd日" value="${post.date}" /></small>
        <span class="bubble"><c:out value="${fn:length(post.comments)}" /></span>
        <h2 id="post-261"><a href="<c:url value="/post/${post.postId}"/>"><c:out value="${post.title}"/></a></h2>
        <small><a href="<c:url value="/post/${post.postId}" />">
                <c:out value="${post.author.displayName}"/></a>&nbsp;发表在&nbsp;
                <a href="<c:url value="/category/${post.category.categoryId}" />">
                    <c:out value="${post.category.name}"/></a></small>
        <div class="entry">
            <p><c:out value="${post.content}" escapeXml="false" /></p>
        </div>
    </div>
</c:forEach>
<div class="navigation">
    <div class="alignleft">
        <c:if test="${hasPrev}">
            <a href="?page=${pageNum - 1}">较新的</a>
        </c:if>
    </div>
    <div class="alignright">
        <c:if test="${hasNext}">
            <a href="?page=${pageNum + 1}">较早的</a>
        </c:if>
    </div>
</div>
<%@include file="foot.jsp" %>