<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="jj" uri="/WEB-INF/tlds/getGravatar.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!-- saved from url=(0054)http://www.tammyhartdesigns.com/demo/category/general/ -->
<html xmlns="http://www.w3.org/1999/xhtml" dir="ltr" lang="en-US">
    <head profile="http://gmpg.org/xfn/11">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title><c:out value="${blogInfo.blogTitle}"/></title>

        <link rel="stylesheet" href="./style.css" type="text/css" media="screen" />

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
                <div class="post-261 post type-post status-publish format-standard hentry category-general" id="post-261">
                    <small class="caps"><fmt:formatDate pattern="yyyy年MM月dd日" value="${blogpost.date}" /></small>
                    <span class="bubble">0</span>
                    <h1><c:out value="${blogpost.title}"/></h1>

                    <small><a href=""><c:out value="${blogpost.author.displayName}"/></a> 发表在 <a href="./?categoryid=<c:out value="${blogpost.category.categoryId}"/>"><c:out value="${blogpost.category.name}"/></a></small>

                    <div class="entry">
                        <p></p>
                        <c:out value="${blogpost.content}" escapeXml="false" />
                    </div>
                </div>
                <h3 id="comments">评论：</h3>

                <ol class="commentlist">
                    <c:forEach items="${blogpost.comments}" var="comment">
                        <li class="comment byuser even thread-even depth-1" id="comment-249">
                            <div id="div-comment-249" class="comment-body">
                                <div class="comment">
                                    <img src="<jj:getGravatar email="${comment.authorEmail}" />" class="avatar avatar-32 photo" height="32" width="32" />
                                    <cite class="fn"><c:out value="${comment.authorName}"/></cite>
                                    <span class="says">说：</span>
                                </div>

                                <div class="comment-meta commentmetadata"><fmt:formatDate pattern="yyyy年MM月dd日" value="${comment.date}" /></div>
                                <p><c:out value="${comment.content}"/></p>
                                <div class="reply">
                                </div>
                            </div>
                        </li>
                    </c:forEach>
                </ol>

                <div class="navigation">
                    <div class="alignleft"></div>
                    <div class="alignright"></div>
                </div>



                <div id="respond">

                    <h3>发表评论</h3>

                    <form action="./comment" method="post" id="commentform">
                        <input type="hidden" name="postid" value="${blogpost.postId}" />
                        <c:choose>
                            <c:when test="${empty userAuth}">
                                <p><input type="text" name="authorname" id="author" value="" size="22" tabindex="1" aria-required='true' />
                                    <label for="author"><small>姓名 *</small></label></p>

                                <p><input type="text" name="email" id="email" value="" size="22" tabindex="2" aria-required='true' />
                                    <label for="email"><small>电子邮箱 *</small></label></p>

                                <p><input type="text" name="homepage" id="url" value="" size="22" tabindex="3" />
                                    <label for="url"><small>主页</small></label>
                                </p>
                            </c:when>
                            <c:otherwise>
                                <input type="hidden" name="op" value="add" />
                                <img src="<jj:getGravatar email="${userAuth.user.email}" />" class="avatar avatar-32 photo" height="32" width="32" />
                                <p><cite class="fn"><c:out value="${userAuth.user.displayName}"/></cite>已经登录 | <a href="./logout">登出</a></p>
                            </c:otherwise>
                        </c:choose>

                        <p><textarea name="content" id="comment" cols="100%" rows="10" tabindex="4"></textarea></p>

                        <p><input name="submit" type="submit" id="submit" tabindex="5" value="发表" />
                            <input type='hidden' name='comment_post_ID' value='20' id='comment_post_ID' />
                            <input type='hidden' name='comment_parent' id='comment_parent' value='0' />
                        </p>
                        <p style="display: none;"><input type="hidden" id="akismet_comment_nonce" name="akismet_comment_nonce" value="758eb52958" /></p>
                    </form>

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