<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/ldoD.tld" prefix="ldod"%>
<div id="fragmentList" class="row-fluid">
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th><spring:message code="tableofcontents.title"/></th>
				<th><spring:message code="navigation.edition"/> Jacinto Prado Coelho</th>
				<th><spring:message code="navigation.edition"/> Teresa Sobral Cunha</th>
				<th><spring:message code="navigation.edition"/> Richard Zenith</th>
				<th><spring:message code="navigation.edition"/> Jerónimo Pizarro</th>
				<th><spring:message code="header.authorialsources"/></th>
			</tr>
		<tbody>
			<c:forEach var="fragment" items='${fragments}'>
				<tr>
					<td><a
						href="${contextPath}/fragments/fragment/${fragment.externalId}">${fragment.title}</a>
					</td>
					<td>${ldod:getExpertEditionInter(fragment,"Jacinto Prado Coelho").metaTextual}</td>
					<td>${ldod:getExpertEditionInter(fragment,"Teresa Sobral Cunha").metaTextual}</td>
					<td>${ldod:getExpertEditionInter(fragment,"Richard Zenith").metaTextual}</td>
					<td>${ldod:getExpertEditionInter(fragment,"Jerónimo Pizarro").metaTextual}</td>
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
