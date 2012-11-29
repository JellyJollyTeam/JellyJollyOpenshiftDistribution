<%--
    Document   : post
    Created on : 2012-9-3, 16:50:34
    Author     : sceliay & fanTasy
--%>

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="jj" uri="/WEB-INF/tlds/getGravatar.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title><c:out value="${blogInfo.blogTitle}"/> - <c:out value="${blogpost.title}"/></title>
            <link href="css/viewBlog.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <div class="container">
            <div class="header">
                <h1 id="blog-name"><a href="home.jsp"><c:out value="${blogInfo.blogTitle}"/></a></h1>
                <h4 id="blog-name2"><c:out value="${blogInfo.blogSubTitle}"/></h4>
                <img src="images/shore.jpg" width="100%" height="300px" />
            </div>
            <div class="centerbar">
                <ul class="nav">
                    <li><a href="home.jsp">首页</a></li>
                    <!--the page created by the user-->
                    <c:forEach items="${pageList}" var="page">
                        <li>
                            <a href="page.jsp?pageid=<c:out value="${page.blogPageId}"/>">
                                <c:out value="${page.pageTitle}"/></a>
                        </li>
                    </c:forEach>
                </ul>
            </div>
            <div class="main">
                <div class="sort">
                    <h5>发表于<a href="#"><fmt:formatDate pattern="yyyy年MM月dd日" value="${blogpost.date}" /></a></h5>
                </div>
                <div class="text">
                    <h2 ><c:out value="${blogpost.title}"/></h2>
                    <p id="passage"><c:out value="${blogpost.content}"/></p>
                    <h5>此条目是由<c:out value="${blogpost.author.displayName}"/>
                        发表在<a href="./?categoryid=<c:out value="${blogpost.category.categoryId}"/>#home"><c:out value="${blogpost.category.name}"/></a></h5>
                </div>
                <div class="comment-list">
                    <div id="comment-list-head">
                        <a name="comments" class="comment-list-head">评论</a></div>
                        <c:forEach items="${blogpost.comments}" var="comment">
                        <div class="comment-list-one">
                            <p><img src="<jj:getGravatar email="${comment.authorEmail}" />" /></p>
                            <p><c:out value="${comment.authorName}"/>在<fmt:formatDate pattern="yyyy年MM月dd日" value="${comment.date}" />说道：</p>
                            <p><c:out value="${comment.content}"/></p>
                        </div>
                    </c:forEach>
                </div>
                <div class="comment">
                    <c:choose>
                        <c:when test="${empty userAuth}">
                            <div>
                                <div id="comment-head"><a name="comments" class="comment-head">发表评论</a></div>
                                <div id="comment-head2">电子邮件地址不会被公开。 必填项已用 * 标注</div>
                                <form method="post" name="usrform" action="./comment">
                                    <input type="hidden" value="add" name="op" />
                                    <input type="hidden" value="<c:out value="${blogpost.postId}" />" name="postid" />
                                    <p class="form-p"><label class="form-label">姓名 *</label>
                                        <input type="text" name="authorname" size="20" class="form-input"/></p>
                                    <p class="form-p"><label class="form-label">电子邮箱 *</label>
                                        <input type="text" name="email" size="50" class="form-input"/></p>
                                    <p class="form-p"><label class="form-label">站点</label>
                                        <input type="text" name="homepage" size="50" class="form-input"/></label></p>
                                    <p class="form-p"><label class="form-label">评论 *</label>
                                        <textarea name="content" cols="40" rows="15" class="form-input-text"></textarea>
                                        <p><input type="submit" name="btnSubmit" value="发表评论" class="form-submit"/></p>
                                </form>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div>
                                <div id="comment-head"><a name="comments" class="comment-head">发表评论</a></div>
                                <div id="comment-head2"><c:out value="${userAuth.user.displayName}"/>已经登录 | <a href="./logout">登出</a></div>
                                <form method="post" name="userform" action="./comment">
                                    <input type="hidden" value="add" name="op" />
                                    <input type="hidden" value="<c:out value="${blogpost.postId}"/>" name="postid"/>
                                    <input type="hidden" value="<c:out value="${userAuth.user.username}"/>" name="authorname" />
                                    <input type="hidden" value="<c:out value="${userAuth.user.email}"/>" name="email"/>
                                    <input type="hidden" value="<c:out value="${userAuth.user.homePageUrl}"/>" name="homepage"/>
                                    <p class="form-p">
                                        <label class="form-label">评论 *</label>
                                        <textarea name="content" cols="40" rows="15" class="form-input-text"></textarea>
                                    </p>
                                    <p><input type="submit" name="btnSubmit" value="发表评论" class="form-submit"/></p>
                                </form>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <jsp:include page="foot.jsp"/>
    </body>
</html>
