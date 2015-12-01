<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

	<div class="container">
		<h1 class="text-center">
			<spring:message code="user.list" />
		</h1>

		<div id="fragmentList" class="row">
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<td><strong><spring:message code="login.username" /></strong></td>
						<td><strong><spring:message code="user.firstName" /></strong></td>
						<td><strong><spring:message code="user.lastName" /></strong></td>
						<td><strong><spring:message code="user.email" /></strong></td>
						<td><strong><spring:message code="user.enabled" /></strong></td>
						<td><strong><spring:message code="user.roles" /></strong></td>
						<td><strong><spring:message code="user.active" /></strong></td>
						<td><strong><spring:message code="general.edit" /></strong></td>
						<td><strong><spring:message code="general.delete" /></strong></td>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="user" items='${users}'>
						<tr>
							<td>${user.getUsername()}</td>
							<td>${user.getFirstName()}</td>
							<td>${user.getLastName()}</td>
							<td>${user.getEmail()}</td>
							<td><c:choose>
									<c:when test="${user.getEnabled()}">
										<spring:message code="general.yes" />
									</c:when>
									<c:otherwise>
										<spring:message code="general.no" />
									</c:otherwise>
								</c:choose></td>
							<td>${user.getListOfRolesAsStrings()}</td>
							<td><form class="form-inline" method="POST"
									action="${contextPath}/admin/user/active">
									<input type="hidden" name="externalId"
										value="${user.externalId}" />
									<button type="submit" class="btn btn-sm pull-right">
										<span class="glyphicon glyphicon-edit"></span>
										<c:choose>
											<c:when test="${user.getActive()}">
												<spring:message code="general.yes" />
											</c:when>
											<c:otherwise>
												<spring:message code="general.no" />
											</c:otherwise>
										</c:choose>
									</button>
								</form></td>
							<td><form class="form-inline" method="GET"
									action="${contextPath}/admin/user/edit">
									<input type="hidden" name="externalId"
										value="${user.externalId}" />
									<button type="submit" class="btn btn-sm pull-right">
										<span class="glyphicon glyphicon-edit"></span>
										<spring:message code="general.edit" />
									</button>
								</form></td>
							<td><form class="form-inline" method="POST"
									action="${contextPath}/admin/user/delete">
									<input type="hidden" name="externalId"
										value="${user.externalId}" />
									<button type="submit" class="btn btn-sm pull-right">
										<span class="glyphicon glyphicon-remove"></span>
										<spring:message code="general.remove" />
									</button>
								</form></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>