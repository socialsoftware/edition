<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Visualizar Fragmento</title>
<link rel="stylesheet" type="text/css" href="/static/css/bootstrap.css" />
<link rel="stylesheet" type="text/css" href="/static/css/style.css" />
<script type="text/javascript" src="/static/js/bootstrap.js"></script>
</head>
<body>
	<c:set var="contextPath" value="${pageContext.request.contextPath}" />
	<%@ include file="/WEB-INF/jsp/common/header.jsp"%>

	<div class="container">
		<div class="row">

			<h1>${fragment.title}</h1>
			<!-- 			<div class="btn-group" data-toggle="buttons-radio"> -->
			<c:forEach var="fragInter" items='${fragment.fragmentInter}'>
				<!-- 	<label class="radio"> <input type="radio"
					name="optionsRadios" id="optionsRadios1" value="${contextPath}/fragments/${fragment.externalId}/${fragInter.externalId}">
 -->
				<a
					href="${contextPath}/fragments/${fragment.externalId}/${fragInter.externalId}">
					${fragInter.name}</a>
				<br>
				<!-- 	</label> -->
			</c:forEach>
		</div>
		<br>
		<div class="row">
			<c:choose>
					<c:when test="${interpretation.sourceType=='EDITORIAL'}"><em>Testemunho Editorial</em>
					</c:when>
					<c:otherwise><em>Testemunho Autoral</em>
					</c:otherwise>
				</c:choose>: ${interpretation.name}
			
		</div>
		<div class="row">
			<div class="addBorder">
				<p>${interpretation.transcription}</p>
			</div>
		</div>
		<br>
		<div class="row">
			<div class="addBorder">
				<c:choose>
					<c:when test="${interpretation.sourceType=='EDITORIAL'}">
					<c:if test="${interpretation.title!=''}"><em>Título</em>: ${interpretation.title}</c:if> <br>
					<c:if test="${interpretation.heteronym.name!=''}"><em>Heterónimo</em>: ${interpretation.heteronym.name}</c:if> <br>
					<c:if test="${interpretation.number!=''}"><em>Número</em>: ${interpretation.number}</c:if> <br>
					<c:if test="${interpretation.page!=''}"><em>Página</em>: ${interpretation.page}</c:if> <br>
					<c:if test="${interpretation.date!=''}"><em>Data</em>: ${interpretation.date}</c:if> <br> 
					<c:if test="${interpretation.notes!=''}"><em>Notas</em>: ${interpretation.notes}</c:if>
				</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
	<style>
.addBorder {
	border-radius: 5px;
	border: 1px solid black;
	padding: 5px;
}
</style>

</body>
</html>