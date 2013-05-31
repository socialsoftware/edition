<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="participant.manage" /></title>
<link rel="stylesheet" type="text/css" href="/static/css/bootstrap.css" />
<link rel="stylesheet" type="text/css" href="/static/css/style.css" />
<script type="text/javascript" src="/static/js/jquery.js"></script>
<script type="text/javascript" src="/static/js/bootstrap.js"></script>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/header.jsp"%>
	<div class="container-fluid">
		<h1 class="text-center">
			<spring:message code="virtualedition" />
			: ${virtualedition.title}<br>
			<spring:message code="participant.manage" />
			<br>
		</h1>
		<br>

		<div class="row-fluid span12">
			<c:forEach var="error" items='${errors}'>
				<div class="row-fluid text-error">
					<spring:message code="${error}" />
				</div>
			</c:forEach>
			<form class="form-inline" method="POST"
				action="/virtualeditions/restricted/addparticipant">
				<fieldset>
					<input type="hidden" name="externalId" id="externalId"
						value="${virtualedition.externalId}" /> <input type="text"
						class="username" name="username" id="username"
						placeholder="<spring:message code="login.username" />"
						value="${username}" /></label>
					<button type="submit" class="btn">
						<i class="icon-plus"></i>
						<spring:message code="general.add" />
					</button>
				</fieldset>
			</form>
		</div>
		<div class="row-fluid span12">
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th><spring:message code="login.username" /></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="participant" items='${virtualedition.participant}'>
						<tr>
							<td>${participant.username}</td>
							<td>
								<form class="form-inline" method="POST"
									action="${contextPath}/virtualeditions/restricted/removeparticipant">
									<input type="hidden" name="veId"
										value="${virtualedition.externalId}" /> <input type="hidden"
										name="userId" value="${participant.externalId}" />
									<button type="submit" class="btn btn-mini">
										<i class="icon-minus"></i>
										<spring:message code="general.remove" />
									</button>
								</form>
							</td>
						</tr>

					</c:forEach>
				</tbody>
			</table>
		</div>

	</div>
</body>
</html>
