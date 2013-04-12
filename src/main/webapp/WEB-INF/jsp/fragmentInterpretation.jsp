<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false"%>

<div id="fragmentInterpretation" class="row">

	<c:if test="${interpretation!=null}">

		<div id="menu" class="row span12">
			<c:choose>
				<c:when test="${interpretation.sourceType=='EDITORIAL'}">
					<em>Testemunho Editorial</em>
				</c:when>
				<c:otherwise>
					<em>Testemunho Autoral</em>
				</c:otherwise>
			</c:choose>
			: ${interpretation.name}
		</div>

		<div id=textual class="row span6">
			<div id="transcription" class="row">

				<div class="addBorder">
					<p>${interpretation.transcription}</p>
				</div>

			</div>

			<div id="metatextual" class="row">
				<div class="addBorder">
					<c:choose>
						<c:when test="${interpretation.sourceType=='EDITORIAL'}">
							<c:if test="${interpretation.title!=''}">
								<em>Título</em>: ${interpretation.title}</c:if>
							<br>
							<c:if test="${interpretation.heteronym.name!=''}">
								<em>Heterónimo</em>: ${interpretation.heteronym.name}</c:if>
							<br>
							<c:if test="${interpretation.number!=''}">
								<em>Número</em>: ${interpretation.number}</c:if>
							<br>
							<c:if test="${interpretation.page!=''}">
								<em>Página</em>: ${interpretation.page}</c:if>
							<br>
							<c:if test="${interpretation.date!=''}">
								<em>Data</em>: ${interpretation.date}</c:if>
							<br>
							<c:if test="${interpretation.notes!=''}">
								<em>Notas</em>: ${interpretation.notes}</c:if>
						</c:when>
						<c:otherwise>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</c:if>
</div>