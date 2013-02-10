<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="jj" uri="/WEB-INF/tlds/getGravatar.tld" %>
<%@include file="head.jsp" %>
    <div class="post" id="post-46">
        <small class="caps">&nbsp;</small>
        <div class="entry">
            <c:out value="${blogpage.pageContent}" escapeXml="false" />
        </div>
    </div>
<%@include file="foot.jsp" %>