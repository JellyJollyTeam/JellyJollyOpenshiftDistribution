<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions' %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!-- saved from url=(0054)http://www.tammyhartdesigns.com/demo/category/general/ -->
<html xmlns="http://www.w3.org/1999/xhtml" dir="ltr" lang="en-US">
    <head profile="http://gmpg.org/xfn/11">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title><c:out value="${blogInfo.blogTitle}"/></title>

        <link rel="stylesheet" href="./static/style.css" type="text/css" media="screen" />

        <meta name="robots" content="noindex,nofollow" />
        <meta name="generator" content="WordPress 3.4.2" />
    </head>

    <body class="archive category category-general category-1">
        <div id="page">

            <div id="header">
                <a href="./" id="sitename" title="<c:out value="${blogInfo.blogSubTitle}"/>"><c:out value="${blogInfo.blogTitle}"/></a>

                <ul id="nav">
                    <c:forEach items="${pageList}" var="page" >
                        <li class="page_item"><a href="page.jsp?pageid=<c:out value="${page.blogPageId}" />"><c:out value="${page.pageTitle}"/></a></li>
                    </c:forEach>
                </ul>

                <form method="get" id="searchform" action="#">
                    <input type="text" value="" name="keyword" id="s" />
                    <input type="image" id="searchsubmit" src="./images/search.gif" />
                </form>
            </div>
            <div id="content">
                <c:forEach items="${postList}" var="post" varStatus="varStatus">
                    <div class="post-261 post type-post status-publish format-standard hentry category-general">

                        <small class="caps"><fmt:formatDate pattern="yyyy年MM月dd日" value="${post.date}" /></small>
                        <span class="bubble"><c:out value="${fn:length(post.comments)}" /></span>
                        <h2 id="post-261"><a href="post.jsp?postid=<c:out value="${post.postId}"/>"><c:out value="${post.title}"/></a></h2>
                        <small><a href="post.jsp?postid=<c:out value="${post.postId}"/>"><c:out value="${post.author.displayName}"/></a>&nbsp;发表在&nbsp;<a href="?categoryid=<c:out value="${post.category.categoryId}"/>" title="View all posts in General" rel="category tag"><c:out value="${post.category.name}"/></a></small>
                        <div class="entry">
                            <p><c:out value="${post.content}" escapeXml="false" /></p>
                        </div>
                    </div>
                </c:forEach>

                <div class="navigation">
                    <div class="alignleft">
                        <c:if test="${hasPrev}">
                            <a href="./?page=${pageNum - 1}">较新的</a>
                        </c:if>
                    </div>
                    <div class="alignright">
                        <c:if test="${hasNext}">
                            <a href="./?page=${pageNum + 1}">较早的</a>
                        </c:if>
                    </div>
                </div>


            </div>

            <div id="sidebar">
                <ul>
                    <li class="categories"><h4>分类</h4>
                        <ul>
                            <c:forEach items="${categoryList}" var="category">
                                <li class="cat-item">
                                    <a href="./?categoryid=<c:out value="${category.categoryId}"/>"><c:out value="${category.name}"/></a>
                                </li>
                            </c:forEach>
                        </ul>
                    </li>
                    <li>
                        <h4>归档</h4>
                        <ul>
                            <c:forEach items="${archivelist}" var="archive">
                                <li><a href="./?year=<c:out value="${archive.year}"/>&month=<c:out value="${archive.month}"/>&max=5"><c:out value="${archive.year}"/>年<c:out value="${archive.month}"/>月</a>&nbsp;(<c:out value="${archive.count}"/>)</li>
                            </c:forEach>
                        </ul>
                    </li>

                    <li id="linkcat-4" class="linkcat"><h4>链接</h4>
                        <ul class="xoxo blogroll">
                            <c:forEach items="${linkList}" var="link">
                                <li><a href="${link.url}" title="${link.title}"><c:out value="${link.title}"/></a></li>
                            </c:forEach>
                        </ul>
                    </li>

                    <li><h4>功能</h4>
                        <ul>
                            <c:choose>
                                <c:when test="${empty userAuth}">
                                    <li><a href="login.jsp">登录</a></li>
                                </c:when>
                                <c:otherwise>
                                    <li><a href="./admin/admin.jsp">控制板</a></li>
                                    <li><a href="./logout">登出</a></li>
                                </c:otherwise>
                            </c:choose>
                        </ul>
                    </li>

                </ul>
            </div>


            <div class="clear"></div>

            <div id="footer">

                <div class="alignright">
                    <a href="#page" class="top">Top</a>
                    <a href="./rss" class="rss">RSS</a>
                    <a href="./" class="home">Home</a>
                </div>

                <a href="http://www.tammyhartdesigns.com/fifty-fifth-street">Fifty Fifth Street</a> theme by <a href="http://www.tammyhartdesigns.com/">Tammy Hart Designs</a><br>
                    Powered by <a href="https://github.com/JellyJollyTeam">JellyJolly</a>
            </div>

        </div>



    </body>
</html>