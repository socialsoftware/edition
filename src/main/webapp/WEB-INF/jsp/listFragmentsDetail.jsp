<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/ldoD.tld" prefix="ldod"%>
<%@ page session="false"%>
<div id="fragmentList" class="row-fluid">
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th><spring:message code="tableofcontents.title"/></th>
				<th>Edição Jacinto Prado Coelho</th>
				<th>Edição Teresa Sobral Cunha</th>
				<th>Edição Richard Zenith</th>
				<th>Edição Jerónimo Pizarro</th>
				<th><spring:message code="header.authorialsources"/></th>
			</tr>
		<tbody>
			<c:forEach var="fragment" items='${fragments}'>
				<tr>
					<td><a
						href="${contextPath}/fragments/fragment/${fragment.externalId}">${fragment.title}</a>
					</td>
					<td>${ldod:getEditionInter(fragment,"Jacinto Prado Coelho").metaTextual}</td>
					<td>${ldod:getEditionInter(fragment,"Teresa Sobral Cunha").metaTextual}</td>
					<td>${ldod:getEditionInter(fragment,"Richard Zenith").metaTextual}</td>
					<td>${ldod:getEditionInter(fragment,"Jerónimo Pizarro").metaTextual}</td>
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
