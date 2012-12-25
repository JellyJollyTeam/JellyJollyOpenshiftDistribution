<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="head.jsp"%>
<c:choose>
    <c:when test="${empty blogpost}">
        <h2>写文章</h2>
    </c:when>
    <c:otherwise>
        <h2>编辑文章</h2>
    </c:otherwise>
</c:choose>
<form action="<c:url value="/admin/post" /><c:out value="/${blogpost.postId}" default="" />" method="POST" id="commentform">
    <c:if test="${!empty blogpost}">
        <input type="hidden" name="_method" value="PUT" />
    </c:if>
    <p>标题<br /><input type="text" name="title" value="<c:out value="${blogpost.title}" default="" />" /></p>
    <p>分类<br />
        <script type="text/javascript">
            function showNewCategoryInput() {
                var newCategorySpan = document.getElementById('newCategorySpan');
                newCategorySpan.removeAttribute('hidden');
                var newCategoryInput = document.getElementById('newCategoryInput');
                newCategoryInput.focus();
            }
            function hideNewCategoryInput() {
                var newCategorySpan = document.getElementById('newCategorySpan');
                newCategorySpan.setAttribute('hidden', 'hidden');
                var newCategoryInput = document.getElementById('newCategoryInput');
                newCategoryInput.setAttribute('value', '');
            }
            function handleCategorySelect() {
                var categorySelect = document.getElementById('categorySelect');
                var id = categorySelect.options[categorySelect.selectedIndex].id;
                // create new category
                if (id == 'newCategory') {
                    showNewCategoryInput();
                // select an existing category
                } else {
                    hideNewCategoryInput();
                }
            }
            window.onload = function() {
                handleCategorySelect();
            }
        </script>
        <select id="categorySelect" style="width: 100px;" name="categoryId" onchange="handleCategorySelect()">
            <c:forEach var="category" items="${categoryList}">
                <c:choose>
                    <c:when test="${empty blogpost}">
                        <option value="${category.categoryId}">${category.name}</option>
                    </c:when>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${category.categoryId == blogpost.category.categoryId}">
                                <option value="${category.categoryId}" selected="selected">${category.name}</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${category.categoryId}">${category.name}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
                <option id="newCategory" value="-1">+ 新建分类</option>
            </c:forEach>
        </select>
        <span hidden="true" id="newCategorySpan">
            <input type="text" name="newCategoryName" id="newCategoryInput" />
        </span>
    <script src="<c:url value="/static/ckeditor/ckeditor.js" />"></script>
    <p>正文<br />
        <textarea name="content" id="editor1"><c:out value="${blogpost.content}" default="" /></textarea>
    </p>
    <p>
        <c:choose>
            <c:when test="${empty blogpost}">
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