<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false"%>
<div id=fragmentTextual class="row-fluid span12">
	<div class="row-fluid">
		<div id="transcription" class="span6">
			<div class="addBorder">
				<p>${writer.transcription}</p>
			</div>
		</div>

		<div id="transcription" class="span6">
			<div class="addBorder">
				<p>${writer2.transcription}</p>
			</div>
		</div>
	</div>
	<br>
	<div class="row-fluid">
		<div id="metatextual" class="span6">
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

		<div id="metatextual" class="span6">
			<div class="addBorder">
				<c:choose>
					<c:when test="${inter2Compare.sourceType=='EDITORIAL'}">
						<c:if test="${inter2Compare.title!=''}">
							<em>Título</em>: ${inter2Compare.title}</c:if>
						<br>
						<c:if test="${inter2Compare.heteronym.name!=''}">
							<em>Heterónimo</em>: ${inter2Compare.heteronym.name}</c:if>
						<br>
						<c:if test="${inter2Compare.number!=''}">
							<em>Número</em>: ${inter2Compare.number}</c:if>
						<br>
						<c:if test="${inter2Compare.page!=''}">
							<em>Página</em>: ${inter2Compare.page}</c:if>
						<br>
						<c:if test="${inter2Compare.date!=''}">
							<em>Data</em>: ${inter2Compare.date}</c:if>
						<br>
						<c:if test="${inter2Compare.notes!=''}">
							<em>Notas</em>: ${inter2Compare.notes}</c:if>
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose>
			</div>
		</div>


	</div>
</div>
