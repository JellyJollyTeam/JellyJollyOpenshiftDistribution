<%--
    Document   : set
    Created on : 2012-9-3, 17:24:34
    Author     : sceliay & fanTasy
--%>

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>博客设置</title>
            <link href="../css/set.css" rel="stylesheet" tyep="text/css"/>
            <link href="../css/global.css" rel="stylesheet" type="text/css" />
            <script type="text/javascript" language="javascript" src="../js/jquery.js"></script>
            <script type="text/javascript">
                $(document).ready(function()
                {
                    //slides the element with class "menu_body" when mouse is over the paragraph
                    $("#secondpane p.menu_head").mouseover(function()
                    {
                        $(this).css({backgroundImage:"url(../images/down.png)"}).next("div.menu_body").slideDown(500).siblings("div.menu_body").slideUp("slow");
                        $(this).siblings().css({backgroundImage:"url(../images/left.png)"});
                    });
                });
            </script>
    </head>
    <body>
        <jsp:include page = "./sidebar.jsp" flush = "true"/>
        <div class="content">
            <jsp:include page = "./head.jsp" flush="true"/>
            <div class="panel">
                <p>博客设置</p>
                <form name="set" action="./info" method="POST">
                    <p><label class="option-label" for="title">站点标题</label>
                        <input type="text" name="title" size="30" class="form-input" value="<c:out value="${blogInfo.blogTitle}"/>"/></p>
                    <p><label class="option-label" for="subtitle">副标题</label>
                        <input type="text" name="subtitle" size="30" class="form-input" value="<c:out value="${blogInfo.blogSubTitle}"/>"/></p>
                    <p><label class="option-label" for="url">Jelly-Jolly地址(URL)</label>
                        <input type="text" name="url" size="30" class="form-input" value="<c:out value="${blogInfo.blogUrl}"/>"/></p>
                    <p><label class="option-label" for="date">日期格式</label>
                        <input id="date1" type='radio' name="date" value="yyyy年MM月dd日" /><label for="date1">2012年8月31日</label>
                        <input id="date2" type='radio' name="date" value="yyyy/MM/dd" /><label for="date2">2012/08/31</label>
                        <input id="date3" type='radio' name="date" value="MM/dd/yyyy" /><label for="date3">08/31/2012</label>
                        <input id="date4" type='radio' name="date" value="dd/MM/yyyy" /><label for="date4">31/08/2012</label>
                    </p>
                    <%--
                    <c:forEach items="${blogInfo.otherProperties.value}" var="info">
                        <p><label><c:out value="${info.otherProperties.key}"/></label></p>
                    </c:forEach>
                    --%>
                    <p><input type="submit" name="btnSubmit" value="保存更改" id="button-primary"></p>
                </form>
            </div>
            <div class="foot">
                感谢使用<a href="#">Jelly-Jolly</a> | <a href="#">反馈</a>
            </div>
        </div>
    </body>
</html>
