<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
<link rel="stylesheet" type="text/css"
	href="/resources/css/bootstrap-table.min.css">
<script src="/resources/js/bootstrap-table.min.js"></script>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

	<div class="container">
		<h3 class="text-center">${user.firstName} ${user.lastName}
			(${user.username})</h3>
		<br />
		<p>
			<strong><spring:message code="header.editions" />: </strong>
			<c:forEach var="edition" items="${user.getPublicEditionList()}"
				varStatus="loop">
				<a href="${contextPath}/edition/acronym/${edition.getAcronym()}">
					${edition.getTitle()}</a><c:if test="${!loop.last}">, </c:if>
			</c:forEach>
		</p>
		<c:if test="${not empty games}">
			<p>
				<strong><spring:message code="general.participant"/>: </strong>
				<c:forEach var="game" items="${games}" varStatus="loop">
					<a href="${contextPath}/virtualeditions/${game.getVirtualEdition().getExternalId()}/classificationGame/${game.getExternalId()}">
							${game.getVirtualEdition().getTitle()} - ${game.getVirtualEditionInter().getTitle()}</a>
					<c:if test="${!loop.last}">, </c:if>
				</c:forEach>
			</p>
			<p>
				<strong><spring:message code="general.points"/>: </strong> ${user.getPlayer().getScore()}
			</p>
			<c:if test="${position != -1}">
			<p>
				<strong><spring:message code="general.position"/>: </strong> ${position}
			</p>
			</c:if>
		</c:if>
		<p>
			<strong>${user.getFragInterSet().size()} <spring:message
					code="fragments" />:
			</strong>
		</p>
		<br />
		<table id="tableUser" data-pagination="false" style="display: none;">
			<!-- <table class="table table-hover table-condensed"> -->
			<thead>
				<tr>
					<th><spring:message code="tableofcontents.title" /></th>
					<th><spring:message code="general.edition" /></th>
					<th><spring:message code="general.category" /></th>
					<th><spring:message code="tableofcontents.usesEditions" /></th>
				</tr>
			<tbody>
				<c:forEach var="inter" items='${user.getFragInterSet()}'>
					<tr>
						<td><a
							href="${contextPath}/fragments/fragment/${inter.getFragment().getXmlId()}/inter/${inter.getUrlId()}">${inter.title}</a></td>
						<td><a
							href="${contextPath}/edition/acronym/${inter.edition.acronym}">${inter.getEdition().getReference()}</a></td>
						<td><c:forEach var="category"
								items='${inter.getAssignedCategories(user)}'>
								<a
									href="${contextPath}/edition/acronym/${inter.edition.acronym}/category/${category.getUrlId()}">
									${category.getNameInEditionContext(inter.getEdition())} </a>
							</c:forEach></td>
						<td><c:forEach var="used" items="${inter.getListUsed()}">-><a
									href="${contextPath}/fragments/fragment/${used.getFragment().getXmlId()}/inter/${used.getUrlId()}">${used.shortName}</a>
							</c:forEach></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</body>
<script>
	$('#tableUser').attr("data-search", "true");
	$('#tableUser').bootstrapTable();
	$(".tip").tooltip({
		placement : 'bottom'
	});
</script>
<script>
	$(document).ready(function() {
		$('#tableUser').show();
	});
</script>
</html>


