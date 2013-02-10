<%--
    Document   : home
    Created on : 2013-2-10, 16:42:34
    Author     : rAy <predator.ray@gmail.com>
--%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <link href="<c:url value="/static/css/bootstrap.css" />" rel="stylesheet" />
    <style>
    <!--
    body {
        background-image: url("<c:url value="/static/images/kindajean.png" />");
    }
    #loading {
        margin-top: 30px;
        text-align: center;
    }
    #loading p {
        color: #666666;
        font-size: 10px;
        margin-top: 5px;
    }
    #content {
        padding: 100px 0;
    }
    .footer .container {
        margin-bottom: 20px;
    }
    .footer .container p {
        text-align: center;
        color: #666666;
        font-size: 10px;
    }
    .thumbnail {
        margin-bottom: 20px;
        background-color: #ffffff;
    }
    .thumbnail p {
        margin: 10px 10px;
    }
    -->
    </style>
</head>
<body>
    <!-- Navbar
    ================================================== -->
    <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
            <a class="brand" href="<c:url value="/" />"><c:out value="${blogInfo.blogTitle}"/></a>
          <div class="nav-collapse collapse">
            <ul class="nav">
              <li class="active">
                  <a href="<c:url value="/" />">主页</a>
              </li>
            <c:forEach items="${pageList}" var="page" >
              <li class="">
                <a href="<c:url value="/page/${page.blogPageId}" />">${page.pageTitle}</a>
              </li>
            </c:forEach>
            </ul>
          </div>
        </div>
      </div>
    </div> <!-- END OF NAVBAR -->

  <div class="container" id="content">

    <!-- Docs nav
    ================================================== -->
    <div class="row">
      <div class="span3">
        <ul class="nav nav-list">
          <li class="nav-header">分类</li>
          <c:forEach items="${categoryList}" var="category">
          <li>
              <a href="<c:url value="/category/${category.categoryId}" />"><c:out value="${category.name}"/></a>
          </li>
          </c:forEach>

          <li class="nav-header">归档</li>
          <c:forEach items="${archivelist}" var="archive">
          <li>
              <a href="<c:url value="/archive/${archive.year}/${archive.month}" />"> <c:out value="${archive.year}"/>年<c:out value="${archive.month}"/>月&nbsp;(<c:out value="${archive.count}"/>)</a>
          </li>

          <li class="nav-header">功能</li>
          <c:choose>
              <c:when test="${empty userAuth}">
          <li><a href="<c:url value="/login" />">登录</a></li>
              </c:when>
              <c:otherwise>
          <li><a href="<c:url value="/admin" />">控制板</a></li>
          <li><a href="<c:url value="/logout?redirect=%2F" />">登出</a></li>
              </c:otherwise>
          </c:choose>
        </c:forEach>
        </ul>
      </div> <!-- END OF SPAN 3 -->
      <div class="span9">
          <div class="container">
              <div class="span3">
                  <div class="thumbnail">
                      <img src="<c:url value="/static/images/holder_300x200.png" />" />
                      <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris convallis eleifend blandit. In hac habitasse platea dictumst. Suspendisse vel mi dolor.</p>
                      <a class="btn btn-link" href="#">详情</a>
                  </div>
                  <div class="thumbnail">
                      <img src="<c:url value="/static/images/holder_300x200.png" />" />
                      <p>Duis massa nunc, pellentesque quis pellentesque vel, tincidunt in tortor.</p>
                      <a class="btn btn-link" href="#">详情</a>
                  </div>
              </div>
              <div class="span3">
                  <div class="thumbnail">
                      <img src="<c:url value="/static/images/holder_300x200.png" />" />
                      <p>Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Curabitur iaculis commodo eleifend. Fusce tincidunt tincidunt libero non tincidunt.</p>
                      <a class="btn btn-link" href="#">详情</a>
                  </div>
                  <div class="thumbnail">
                      <img src="<c:url value="/static/images/holder_300x200.png" />" />
                      <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris convallis eleifend blandit. In hac habitasse platea dictumst. Suspendisse vel mi dolor.</p>
                      <a class="btn btn-link" href="#">详情</a>
                  </div>
              </div>
              <div class="span3">
                  <div class="thumbnail">
                      <img src="<c:url value="/static/images/holder_300x200.png" />" />
                      <p>Quisque eu rhoncus tortor. Morbi ornare ultrices dui, nec convallis purus cursus adipiscing. In dapibus vehicula velit, eget congue odio dignissim et. </p>
                      <a class="btn btn-link" href="#">详情</a>
                  </div>
                  <div class="thumbnail">
                      <img src="<c:url value="/static/images/holder_300x200.png" />" />
                      <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris convallis eleifend blandit. In hac habitasse platea dictumst. Suspendisse vel mi dolor.</p>
                      <a class="btn btn-link" href="#">详情</a>
                  </div>
              </div>
          </div>
          <div class="span9" id="loading">
              <img src="<c:url value="/static/images/loading.gif" />" />
              <p>载入中…</p>
          </div>
      </div> <!-- END OF SPAN 9 -->
    </div> <!-- END OF Docs nav (row) -->

  </div> <!-- END OF CONTENT (container) -->
  <footer class="footer">
      <div class="container">
          <p>Powered by <a href="#">JellyJolly</a></p>
      </div>
    </footer>
  <script src="<c:url value="/static/jquery-1.8.1.min.js" />"></script>
  <script src="<c:url value="/static/js/bootstrap.min.js" />"></script>
</body>
</html>