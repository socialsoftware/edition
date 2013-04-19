<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/ldoD.tld" prefix="ldod"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Fragmentos do LdoD</title>
<link rel="stylesheet" type="text/css" href="/static/css/bootstrap.css" />
<link rel="stylesheet" type="text/css" href="/static/css/style.css" />
<script type="text/javascript" src="/static/js/jquery.js"></script>
<script type="text/javascript" src="/static/js/bootstrap.js"></script>
</head>
<body>
	<c:set var="contextPath" value="${pageContext.request.contextPath}" />
	<%@ include file="/WEB-INF/jsp/common/header.jsp"%>

	<div class="container-fluid">
		<h1 class="text-center">Fragmentos do LdoD</h1>
		<table class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>Nome</th>
					<th>Edição Jacinto Prado Coelho</th>
					<th>Edição Teresa Sobral Cunha</th>
					<th>Edição Richard Zenith</th>
					<th>Edição Jerónimo Pizarro</th>
					<th>Testemunhos Autorais</th>
				</tr>
			<tbody>
				<c:forEach var="fragment" items='${fragments}'>
					<tr>
						<td><a
							href="${contextPath}/fragments/fragment/${fragment.externalId}">${fragment.title}</a>
						</td>
						<td>
							${ldod:getEditionInter(fragment,"Jacinto Prado Coelho").metaTextual}
						</td>
						<td>
							${ldod:getEditionInter(fragment,"Teresa Sobral Cunha").metaTextual}
						</td>
						<td>
							${ldod:getEditionInter(fragment,"Richard Zenith").metaTextual}
						</td>
						<td>
							${ldod:getEditionInter(fragment,"Jerónimo Pizarro").metaTextual}
						</td>
						<c:forEach var="fragInter" items='${fragment.sortedInterps}'>
							<c:if test="${fragInter.sourceType=='AUTHORIAL'}">
								<td>${fragInter.metaTextual}</td>
							</c:if>
						</c:forEach>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</body>
</html>