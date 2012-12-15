<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions' %>
<%@taglib prefix="jj" uri="/WEB-INF/tlds/getGravatar.tld" %>
<%@include file="head.jsp" %>
    <div class="post-261 post type-post status-publish format-standard hentry category-general" id="post-261">
        <small class="caps"><fmt:formatDate pattern="yyyy年MM月dd日" value="${blogpost.date}" /></small>
        <span class="bubble"><c:out value="${fn:length(blogpost.comments)}" /></span>
        <h1><c:out value="${blogpost.title}"/></h1>

        <small><a href=""><c:out value="${blogpost.author.displayName}"/></a> 发表在 <a href="/category/<c:out value="${blogpost.category.categoryId}"/>"><c:out value="${blogpost.category.name}"/></a></small>

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
                        <img src="<jj:getGravatar email="${comment.authorEmail}" size="32" />" class="avatar avatar-32 photo" height="32" width="32" />
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
                    <img src="<jj:getGravatar email="${userAuth.user.email}" size="32" />" class="avatar avatar-32 photo" height="32" width="32" />
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
<%@include file="foot.jsp" %>