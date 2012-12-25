<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="head.jsp"%>
<h2>全部链接</h2>
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
                <td style="padding-top: 2px; padding-left: 10px" width="40%">标题</td>
                <td style="padding-top: 2px; padding-left: 10px" width="40%">链接</td>
            </tr>
            <c:forEach var="link" items="${linkList}">
            <tr>
                <td><input type="checkbox" name="choice"></td>
                <td style="padding-top: 2px; padding-left: 10px" width="30%" height="60px">
                    ${link.title}</td>
                <td style="padding-top: 2px; padding-left: 10px" width="30%" height="60px">
                    <a href="${link.url}">${link.url}</a></td>
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
<%@include file="foot.jsp" %>