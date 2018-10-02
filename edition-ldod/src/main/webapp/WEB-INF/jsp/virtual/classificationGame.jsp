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
			<c:if test="${isAdmin}">
				<a
					href="${contextPath}/virtualeditions/restricted/${virtualEdition.externalId}/classificationGame/create">
					<button type="submit" class="btn btn-primary">
						<span class="glyphicon glyphicon-plus"></span>
						<spring:message code="general.create" />
					</button>
				</a>

			</c:if>
		</div>

		<br /> <br /> <br /> <br />

		<div class="row col-md-12">
			<table class="table table-hover">
				<thead>
					<tr>
						<th><spring:message code="general.description" /></th>
						<th><spring:message code="general.title" /></th>
						<th><spring:message code="general.date" /></th>
						<th><spring:message code="taxonomy.annotation" /></th>
						<th><spring:message code="general.category" /></th>
						<th><spring:message code="general.players" /></th>
						<th><spring:message code="general.winner" /></th>
						<th><spring:message code="general.responsible" /></th>
						<th><spring:message code="general.delete" /></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="game"
						items='${games}'>
						<tr>
							<td>${game.getDescription()}</td>
							<c:choose>
								<c:when test="${game.isActive()}">
									<td><a href="/virtualeditions/${game.getVirtualEdition().getExternalId()}/classificationGame/${game.getExternalId()}">${game.getVirtualEditionInter().getTitle()}</a></td>
								</c:when>
								<c:otherwise>
									<td class="success"><span class="glyphicon glyphicon-asterisk"></span><a href="/virtualeditions/${game.getVirtualEdition().getExternalId()}/classificationGame/${game.getExternalId()}">${game.getVirtualEditionInter().getTitle()}</a></td>
								</c:otherwise>
							</c:choose>
							<td>${game.getDateTime().toString("dd/MM/yyyy HH:mm")}</td>
							<td><c:choose>
									<c:when test="${game.getOpenAnnotation()}">
										<spring:message code="taxonomy.annotation.all" />
									</c:when>
									<c:otherwise>
										<spring:message code="taxonomy.annotation.members" />
										<br />
									</c:otherwise>
								</c:choose></td>
							<td>${game.getTag().getCategory().getName()}</td>
							<td><c:forEach var="participant" items='${game.getClassificationGameParticipantSet()}'
									varStatus="loop">
									<a
										href="${contextPath}/edition/user/${participant.getPlayer().getUser().getUsername()}">${participant.getPlayer().getUser().getFirstName()}
										${participant.getPlayer().getUser().getLastName()}</a>
									<c:if test="${!loop.last}">, </c:if>
								</c:forEach>
							<td>${game.getTag().getContributor().getFirstName()}
								${game.getTag().getContributor().getLastName()}</td>
							<td>${game.getResponsible().getFirstName()}
								${game.getResponsible().getLastName()}</td>
							<td><c:if test="${isAdmin && game.canBeRemoved()}">
									<form class="form-inline" method="POST"
										action="${contextPath}/virtualeditions/restricted/${virtualEdition.externalId}/classificationGame/${game.externalId}/remove">
										<input type="hidden" name="${_csrf.parameterName}"
											value="${_csrf.token}" />
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
	</div>
</body>
</html>