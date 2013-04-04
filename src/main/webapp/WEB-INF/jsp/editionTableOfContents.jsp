<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Índice da Edição</title>
		<link rel="stylesheet" type="text/css" href="/static/css/bootstrap.css" />
		<link rel="stylesheet" type="text/css" href="/static/css/style.css" />
	</head>
	<body>
		<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
		<%@ include file="/WEB-INF/jsp/common/header.jsp" %>
		
		<div class="container">
			<h1>Edição de ${edition.editor}</h1>
			<table class="table table-bordered table-condensed">
			<caption>Índice:</caption>
				<thead>
					<tr>
						<th>Número</th>
						<th>Título</th>
						<th>Página</th>
					</tr>
				<tbody>
				<c:forEach var="interp" items='${edition.sortedInterps}'>
					<tr>
						<td><c:if test="${interp.number!=0}">${interp.number}</c:if></td> 
						<td> <a href="${contextPath}/fragments/${interp.fragment.externalId}">${interp.fragment.title}</a></td>
						<td> ${interp.page}</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>
	</body>
</html>