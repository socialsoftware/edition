<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

	<c:set var="user"
		value='${pageContext.request.userPrincipal.principal.getUser()}' />
	<c:set var="isAdmin"
		value="${virtualEdition.getAdminSet().contains(user)}" />
	<c:set var="isMember"
		value="${virtualEdition.getParticipantSet().contains(user)}" />
	<c:set var="isPending"
		value="${virtualEdition.getPendingSet().contains(user)}" />


	<div class="container">
		<br />
		<div class="row col-md-1">
			<form class="form-inline" method="GET"
				action="${contextPath}/virtualeditions/restricted/manage/${virtualEdition.externalId}">
				<button type="submit" class="btn btn-default">
					<span class="glyphicon glyphicon-arrow-left"></span>
					<spring:message code="general.back" />
				</button>
			</form>
		</div>
		<br /> <br />
		<div class="row col-md-12">
			<h3 class="text-center">
				${virtualEdition.title} <br>
			</h3>
		</div>
		<br> <br> <br> <br> <br>

		<div class="row col-md-12">
			<c:forEach var="error" items='${errors}'>
				<div class="row text-error">
					<spring:message code="${error}" />
				</div>
			</c:forEach>
			<c:if test="${isAdmin}">
				<form class="form-inline" method="POST"
					action="${contextPath}/virtualeditions/restricted/${virtualEdition.externalId}/participants/add">
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />
					<div class="form-group">
						<input type="hidden" class="form-control" name="externalId"
							value="${virtualEdition.externalId}" />
					</div>
					<div class="form-group">
						<input type="text" class="form-control" name="username"
							placeholder="<spring:message code="login.username" />"
							value="${username}" />
					</div>
					<button type="submit" class="btn btn-primary">
						<span class="glyphicon glyphicon-plus"></span>
						<spring:message code="general.add" />
					</button>
				</form>
			</c:if>
		</div>

		<br /> <br /> <br /> <br />

		<div class="row col-md-12">
			<table class="table table-hover">
				<thead>
					<tr>
						<th><spring:message code="login.username" /></th>
						<th><spring:message code="user.firstName" /></th>
						<th><spring:message code="user.lastName" /></th>
						<th>e-mail</th>
						<th><spring:message code="user.role" /></th>
						<th><spring:message code="general.remove" /></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="member"
						items='${virtualEdition.getActiveMemberSet()}'>
						<tr>
							<td><span class="glyphicon glyphicon-user"></span> <a
								href="${contextPath}/edition/user/${member.getUser().getUsername()}">${member.getUser().getUsername()}</a></td>
							<td>${member.getUser().getFirstName()}</td>
							<td>${member.getUser().getLastName()}</td>
							<td><a href="mailto:${member.getUser().getEmail()}">${member.getUser().getEmail()}</a></td>
							<td><c:choose>
									<c:when
										test="${virtualEdition.canSwitchRole(user, member.getUser())}">
										<form class="form-inline" method="POST"
											action="${contextPath}/virtualeditions/restricted/${virtualEdition.externalId}/participants/role">
											<input type="hidden" name="${_csrf.parameterName}"
												value="${_csrf.token}" /> <input type="hidden"
												name="username" value="${member.getUser().getUsername()}" />
											<button type="submit" class="btn btn-primary btn-sm">
												<span class="glyphicon glyphicon-retweet"></span>
												<c:choose>
													<c:when test='${member.hasRole("ADMIN")}'>
														<spring:message code="general.manager" />
													</c:when>
													<c:otherwise>
														<spring:message code="general.editor" />
													</c:otherwise>
												</c:choose>
											</button>
										</form>
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test='${member.hasRole("ADMIN")}'>
												<spring:message code="general.manager" />
											</c:when>
											<c:otherwise>
												<spring:message code="general.editor" />
											</c:otherwise>
										</c:choose>
									</c:otherwise>
								</c:choose></td>
							<td><c:if
									test="${virtualEdition.canRemoveMember(user, member.getUser())}">
									<form class="form-inline" method="POST"
										action="${contextPath}/virtualeditions/restricted/${virtualEdition.externalId}/participants/remove">
										<input type="hidden" name="${_csrf.parameterName}"
											value="${_csrf.token}" /> <input type="hidden" name="userId"
											value="${member.getUser().getExternalId()}" />
										<button type="submit" class="btn btn-primary btn-sm">
											<span class="glyphicon glyphicon-remove"></span>
											<spring:message code="general.remove" />
										</button>
									</form>
								</c:if></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>

		<div class="row">
			<table class="table table-hover">
				<caption>
					<h3>
						<spring:message code="participantsForm.message2" />
					</h3>
				</caption>
				<thead>
					<tr>
						<th><spring:message code="login.username" /></th>
						<th><spring:message code="user.firstName" /></th>
						<th><spring:message code="user.lastName" /></th>
						<th>email</th>
						<th><spring:message code="general.add" /></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="pending"
						items='${virtualEdition.getPendingMemberSet()}'>
						<tr>
							<td><span class="glyphicon glyphicon-user"></span> <a
								href="${contextPath}/edition/user/${pending.getUser().getUsername()}">${pending.getUser().getUsername()}</a></td>
							<td>${pending.getUser().getFirstName()}</td>
							<td>${pending.getUser().getLastName()}</td>
							<td><a href="mailto:${pending.getUser().getEmail()}">${pending.getUser().getEmail()}</a></td>
							<td><c:if test="${isAdmin}">
									<form class="form-inline" method="POST"
										action="${contextPath}/virtualeditions/restricted/${virtualEdition.getExternalId()}/participants/approve">
										<input type="hidden" name="${_csrf.parameterName}"
											value="${_csrf.token}" /> <input type="hidden"
											name="username" value="${pending.getUser().getUsername()}" />
										<button type="submit" class="btn btn-primary btn-sm">
											<span class="glyphicon glyphicon-plus"></span>
											<spring:message code="general.add" />
										</button>
									</form>
								</c:if></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>

	</div>
</body>
</html>
