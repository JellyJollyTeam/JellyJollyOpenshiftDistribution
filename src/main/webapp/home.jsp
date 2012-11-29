<%--
Document   : home.jsp
Created on : 2012-9-3, 15:19:43
Author     : sceliay & fanTasy
--%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title><c:out value="${blogInfo.blogTitle}"/></title>
        <!--[if lte IE 7]>
        <style>
        .content { margin-right: -1px; } /* 此 1px 负边距可以放置在此布局中的任何列中，且具有相同的校正效果。 */
        ul.nav a { zoom: 1; }  /* 缩放属性将为 IE 提供其需要的 hasLayout 触发器，用于校正链接之间的额外空白 */
        </style>
        <![endif]-->
        <link href="css/home.css" rel="stylesheet" type="text/css" />
    </head>
    <body>
        <div class="container">
            <div class="header">
                <h1 id="blog-name"><c:out value="${blogInfo.blogTitle}"/></h1>
                <h4 id="blog-name2"><c:out value="${blogInfo.blogSubTitle}"/></h4>
                <img src="images/shore.jpg" width="100%" height="300px" />
            </div>
            <div class="centerbar">
                <ul class="nav">
                    <li><a href="home.jsp" name="home">首页</a></li>
                    <!--the pages created by the user-->
                    <c:forEach items="${pageList}" var="page">
                        <li>
                            <a href="page.jsp?pageid=<c:out value="${page.blogPageId}" />">
                                <c:out value="${page.pageTitle}"/></a>
                        </li>
                    </c:forEach>
                </ul>
            </div>
            <div class="main">
                <div class="text">
                    <c:forEach items="${postList}" var="post" varStatus="varStatus">
                        <%--c:if test="${varStatus.index<=5}"--%>
                        <div class="passage">
                            <h2 class="passage-name"><a href="post.jsp?postid=<c:out value="${post.postId}"/>"s>
                                    <c:out value="${post.title}"/></a></h2>
                            <h5><fmt:formatDate pattern="yyyy年MM月dd日" value="${post.date}" /></h5>
                            <h4><c:out value="${post.content}"/></h4>
                            <h5>发表在&nbsp;
                                <a href="?categoryid=<c:out value="${post.category.categoryId}"/>"><c:out value="${post.category.name}"/></a>&nbsp;
                                |&nbsp;<a href="post.jsp?postid=<c:out value="${post.postId}"/>#comments">发表评论</a></h5>
                        </div>
                        <%--/c:if--%>
                    </c:forEach>

                    <%--
                  <div class="next">
                    <h6><a href="#">上一页</a>|<a href="#">下一页</a></h6>
                <!--next end--></div>

                    --%>
                </div>

                <div class="app">
                    <div>
                        <a href="./rss" title="订阅本博客"><img  style="margin-top: 10px; margin-left: 15px; margin-bottom: 10px; width: 30px; height:30px;" src="./images/feed-icon.png"/></a>
                    </div>
                    <form method="get" name="searchFrom" action="#">
                        <!--<label class="assistive-text" for="search">搜索</label>-->
                        <input style="border: 1px solid; border-color: #CCC" type="text" name="keyword" id="s" placeholder="关键词" size="20"/>
                        <input id="searchButton" type="submit" name="submit" value="搜索" />
                    </form>
                    <div class="sort">
                        <h4>归档</h4>
                        <c:forEach items="${archivelist}" var="archive">
                            <h5><a href="?year=<c:out value="${archive.year}"/>&month=<c:out value="${archive.month}"/>&max=5#home">
                                    <c:out value="${archive.year}"/>年<c:out value="${archive.month}"/>月</a>
                                (<c:out value="${archive.count}"/>)</h5>
                            </c:forEach>
                    </div>
                    <div class="sort">
                        <h4>分类</h4>
                        <c:forEach items="${categoryList}" var="category">
                            <h5><a href="?categoryid=<c:out value="${category.categoryId}"/>#home"><c:out value="${category.name}"/></a></h5>
                        </c:forEach>
                    </div>
                    <div class="function">
                        <h4>功能</h4>
                        <c:choose>
                            <c:when test="${empty userAuth}">
                                <h5><a href="login.jsp">登录</a></h5>
                            </c:when>
                            <c:otherwise>
                                <h5><a href="./admin/admin.jsp">控制板</a></h5>
                                <h5><a href="./logout">登出</a></h5>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div>
                        <h4>链接</h4>
                        <c:forEach items="${linkList}" var="link">
                            <h5><a href="${link.url}">
                                    <c:choose>
                                        <c:when test="${empty link.image}">
                                            <c:out value="${link.title}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <img style="WIDTH: 100px!important;HEIGHT: auto!important;" src="<c:out value="${link.image}"/>" title="<c:out value="${link.title}"/>" />
                                        </c:otherwise>
                                    </c:choose>
                                </a>
                            </h5>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="foot.jsp" flush="true"/>
    </body>
</html>


