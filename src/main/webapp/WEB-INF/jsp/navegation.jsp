<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false"%>

<div class="row-fluid">

	<legend>Navegação</legend>

	<c:forEach var="fragInter" items='${fragment.sortedInterps}'>
		<c:if test="${fragInter.sourceType=='EDITORIAL'}">
			<a class="btn-tiny"
				href="${contextPath}/edition/internalid/${fragInter.edition.externalId}"><i
				class="icon-list"></i> ${fragInter.edition.editor}</a>
			<br>
			<div class="text-center">
				<a class="btn-tiny"
					href="${contextPath}/fragments/fragment/interpretation/prev/number/${fragInter.externalId}"><i
					class="icon-backward"></i></a> ${fragInter.number} <a class="btn-tiny"
					href="${contextPath}/fragments/fragment/interpretation/next/number/${fragInter.externalId}"><i
					class="icon-forward"></i></a><br> <a class="btn-tiny"
					href="${contextPath}/fragments/fragment/interpretation/prev/heteronym/${fragInter.externalId}"><i
					class="icon-backward"></i></a> <a
					href="${contextPath}/edition/internalid/heteronym/${fragInter.edition.externalId}/${fragInter.heteronym.externalId}">${fragInter.heteronym.name}
				</a><a class="btn-tiny"
					href="${contextPath}/fragments/fragment/interpretation/next/heteronym/${fragInter.externalId}"><i
					class="icon-forward"></i></a><br> <br>
			</div>
		</c:if>
	</c:forEach>

</div>

