<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="exceptions.title" /></title>
<link rel="stylesheet" type="text/css" href="/static/css/bootstrap.css" />
<link rel="stylesheet" type="text/css" href="/static/css/style.css" />
<script type="text/javascript" src="/static/js/jquery.js"></script>
<script type="text/javascript" src="/static/js/bootstrap.js"></script>
<style type="text/css">
body {
	padding: 50px;
}
</style>
</head>
<body>
	<c:set var="contextPath" value="${pageContext.request.contextPath}" />
	<%@ include file="/WEB-INF/jsp/common/header.jsp"%>

	<div class="container">
		<div class="hero-unit">
			<h1>
				<spring:message code="exceptions.error" />
			</h1>
			<br>
			<div class="alert alert-error">
				<a class="close" data-dismiss="alert"></a> <strong><c:choose>
						<c:when test="${i18n}"><spring:message code="${message}" /></c:when>
						<c:otherwise>${message}</c:otherwise>
					</c:choose> </strong>
			</div>
		</div>
	</div>

</body>
</html>