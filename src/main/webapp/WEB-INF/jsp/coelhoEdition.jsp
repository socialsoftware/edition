<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Edição de Jacinto Prado Coelho</title>
		<link rel="stylesheet" type="text/css" href="/static/css/bootstrap.css" />
		<link rel="stylesheet" type="text/css" href="/static/css/style.css" />
	</head>
	<body>
		<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
		<%@ include file="/WEB-INF/jsp/common/header.jsp" %>
		
		<div class="container">
			<h1>Edição de Jacinto Prado Coelho</h1>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th>Nome do Fragmento</th>
					</tr>
				<tbody>
				<c:forEach var="fragment" items='${fragments}'>
					<tr>
						<td><a href="${contextPath}/fragments/${fragment.externalId}">${fragment.title}</a></td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>
	</body>
</html>