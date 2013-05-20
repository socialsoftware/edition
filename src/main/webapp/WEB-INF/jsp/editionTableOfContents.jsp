<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Índice da Edição</title>
<link rel="stylesheet" type="text/css" href="/static/css/bootstrap.css" />
<link rel="stylesheet" type="text/css" href="/static/css/style.css" />
<script type="text/javascript" src="/static/js/jquery.js"></script>
<script type="text/javascript" src="/static/js/bootstrap.js"></script>
</head>
<body>
	<c:set var="contextPath" value="${pageContext.request.contextPath}" />
	<%@ include file="/WEB-INF/jsp/common/header.jsp"%>

	<div class="container">	
		<h3 class="text-center"><spring:message code="tableofcontents.editionof" /> ${edition.editor}</h3>
		<c:if test="${heteronym != null}">
				<h4 class="text-left"><spring:message code="tableofcontents.fragmentsof" /> ${heteronym.name}</h4>
		</c:if>

		<table class="table table-bordered table-condensed">
			<thead>
				<tr>
					<th><spring:message code="tableofcontents.number" /></th>
					<th><spring:message code="tableofcontents.title" /></th>
					<th><spring:message code="tableofcontents.volume" /></th>
					<th><spring:message code="tableofcontents.page" /></th>
				</tr>
			<tbody>
				<c:forEach var="interp" items='${edition.sortedInterps}'>
				<c:if test="${(heteronym == null) || (interp.heteronym == heteronym)}">
					<tr>
						<td><c:if test="${interp.number!=0}">${interp.number}</c:if></td>
						<td><a
							href="${contextPath}/fragments/fragment/interpretation/${interp.externalId}">${interp.fragment.title}</a></td>
						<td>${interp.volume}</td>
						<td>${interp.startPage}</td>
					</tr>
					</c:if>
				</c:forEach>
			</tbody>
		</table>
	</div>
</body>
</html>