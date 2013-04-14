<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false"%>
<div id=fragmentTextual class="row span12">
	<div id="transcription" class="row">
		<div class="addBorder">
			<p>${inter.transcription}</p>
		</div>
	</div>

	<div id="metatextual" class="row">
		<div class="addBorder">
			<c:choose>
				<c:when test="${inter.sourceType=='EDITORIAL'}">
					<c:if test="${inter.title!=''}">
						<em>Título</em>: ${inter.title}</c:if>
					<br>
					<c:if test="${inter.heteronym.name!=''}">
						<em>Heterónimo</em>: ${inter.heteronym.name}</c:if>
					<br>
					<c:if test="${inter.number!=''}">
						<em>Número</em>: ${inter.number}</c:if>
					<br>
					<c:if test="${inter.page!=''}">
						<em>Página</em>: ${inter.page}</c:if>
					<br>
					<c:if test="${inter.date!=''}">
						<em>Data</em>: ${inter.date}</c:if>
					<br>
					<c:if test="${inter.notes!=''}">
						<em>Notas</em>: ${inter.notes}</c:if>
				</c:when>
				<c:otherwise>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</div>
