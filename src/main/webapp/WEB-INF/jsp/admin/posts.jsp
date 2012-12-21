<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="head.jsp"%>
<h2>全部文章</h2>
<form action="#" method="POST">
    <p><span style="float: left; width: 40px;">分类</span>
    <select style="width: 100px;">
        <option>全部</option>
        <option>默认</option>
        <option>日记</option>
    </select>
    </p>
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
                <td style="padding-top: 2px; padding-left: 10px" width="34%">标题</td>
                <td style="padding-top: 2px; padding-left: 10px" width="12%">作者</td>
                <td style="padding-top: 2px; padding-left: 10px" width="10%">分类目录</td>
                <td style="padding-top: 2px; padding-left: 10px" width="17%">日期</td>
            </tr>
            <tr>
                <td><input type="checkbox" name="choice"></td>
                <td style="padding-top: 2px; padding-left: 10px" width="30%" height="60px">
                <a href="editBlog.jsp?postid=10">互联网时代的社会语言学：基于SNS的文本数据挖掘</td>
                <td style="padding-top: 2px; padding-left: 10px"><a href="#">季文昊</a></td>
                <td style="padding-top: 2px; padding-left: 10px"><a href="?categoryid=1">默认</a></td>
                <td style="padding-top: 2px; padding-left: 10px" id="data">2012年12月03日</td>
            </tr>
            <tr>
                <td><input type="checkbox" name="choice"></td>
                <td style="padding-top: 2px; padding-left: 10px" width="30%" height="60px">
                <a href="editBlog.jsp?postid=10">互联网时代的社会语言学：基于SNS的文本数据挖掘</a></td>
                <td style="padding-top: 2px; padding-left: 10px"><a href="#">季文昊</a></td>
                <td style="padding-top: 2px; padding-left: 10px"><a href="?categoryid=1">默认</a></td>
                <td style="padding-top: 2px; padding-left: 10px" id="data">2012年12月03日</td>
            </tr>
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