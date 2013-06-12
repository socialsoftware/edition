<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/ldoD.tld" prefix="ldod"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="header.manageeditions" /></title>
<link rel="stylesheet" type="text/css" href="/static/css/bootstrap.css" />
<link rel="stylesheet" type="text/css" href="/static/css/style.css" />
<script type="text/javascript" src="/static/js/jquery.js"></script>
<script type="text/javascript" src="/static/js/bootstrap.js"></script>
</head>
<body>
	<c:set var="contextPath" value="${pageContext.request.contextPath}" />
	<%@ include file="/WEB-INF/jsp/common/header.jsp"%>

	<div class="container-fluid">
		<h1 class="text-center">
			<spring:message code="header.manageeditions" />
		</h1>
		<br>
		<div class="row-fluid">
			<form class="form-inline" method="POST"
				action="/virtualeditions/restricted/create">
				<fieldset>
					<c:forEach var="error" items='${errors}'>
						<div class="row-fluid text-error">
							<spring:message code="${error}" />
						</div>
					</c:forEach>
					<input type="text" class="input-small" name="acronym" id="acronym"
						placeholder="<spring:message code="virtualeditionlist.acronym" />"
						value="${acronym}" /> <input type="text" class="input"
						name="title" id="title"
						placeholder="<spring:message code="virtualeditionlist.name" />"
						value="${title}" /> <select class="selectpicker" name="public"
						id="public">
						<c:choose>
							<c:when test="${public == false}">
								<option value="true"><spring:message code="general.public" /></option>
								<option value="false" selected><spring:message code="general.private" /></option>
							</c:when>
							<c:otherwise>
								<option value="true" selected><spring:message code="general.public" /></option>
								<option value="false"><spring:message code="general.private" /></option>
							</c:otherwise>
						</c:choose>
					</select>
					<button type="submit" class="btn">
						<i class="icon-edit"></i>
						<spring:message code="general.create" />
					</button>
				</fieldset>
			</form>
		</div>
		<div class="row-fluid">
			<div>
				<table class="table table-striped table-bordered table-condensed table-hover">
					<thead>
						<tr>
							<th><spring:message code="virtualeditionlist.acronym" /></th>
							<th><spring:message code="virtualeditionlist.name" /></th>
							<th><spring:message code="general.date" /></th>
							<th><spring:message code="general.access" /></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="virtualEdition" items='${virtualEditions}'>
							<tr>
								<td>${virtualEdition.acronym}</td>
								<td>${virtualEdition.title}</td>
								<td>${virtualEdition.date}</td>
								<td><c:choose>
										<c:when test="${virtualEdition.pub}"><spring:message code="general.public" /></c:when>
										<c:otherwise><spring:message code="general.private" /></c:otherwise>
									</c:choose></td>
								<td><form class="form-inline" method="POST"
										action="${contextPath}/virtualeditions/toggleselection">
										<input type="hidden" name="externalId"
											value="${virtualEdition.externalId}" />
										<button type="submit" class="btn btn-mini">
											<i class="icon-check"></i>
											<c:choose>
												<c:when
													test="${ldoDSession.selectedVEs.contains(virtualEdition)}">
													<spring:message code="general.deselect" />
												</c:when>
												<c:otherwise>
													<spring:message code="general.select" />
												</c:otherwise>
											</c:choose>
										</button>
									</form></td>
									<c:choose>
									
								<c:when test="${virtualEdition.participantSet.contains(user)}">
									<td><a class="btn btn-mini"
										href="${contextPath}/virtualeditions/restricted/editForm/${virtualEdition.externalId}"><i
											class="icon-edit"></i> <spring:message code="general.edit" /></a></td>
									<td><a class="btn btn-mini"
										href="${contextPath}/virtualeditions/restricted/participantsForm/${virtualEdition.externalId}"><i
											class="icon-edit"></i> <spring:message
												code="participant.manage" /></a></td>
									<td>
										<form class="form-inline" method="POST"
											action="${contextPath}/virtualeditions/restricted/delete">
											<input type="hidden" name="externalId"
												value="${virtualEdition.externalId}" />
											<button type="submit" class="btn btn-mini">
												<i class="icon-remove"></i>
												<spring:message code="general.delete" />
											</button>
										</form>
									</td>
								</c:when>
								<c:otherwise><td></td><td></td><td></td></c:otherwise>
								</c:choose>
							</tr>

						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>