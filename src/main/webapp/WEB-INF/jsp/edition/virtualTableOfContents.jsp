<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<h3 class="text-center">
	<spring:message code="virtualedition" />:
	${edition.title}
</h3>
<br />
<p>
	<strong><spring:message code="general.editors" />: </strong>
	<c:forEach var="user" items="${edition.getParticipantList()}"
		varStatus="loop">
		<a href="${contextPath}/edition/user/${user.getUsername()}">${user.getFirstName()}
			${user.getLastName()}</a><c:if test="${!loop.last}">, </c:if>
	</c:forEach>
</p>
<c:if test="${edition.getSynopsis().length() > 0}">
	<p>
		<strong><spring:message code="virtualedition.synopsis" />: </strong>
		${edition.synopsis}
	</p>
</c:if>
<c:if test="${edition.getTaxonomy().getCategoriesSet().size() > 0}">
	<p>
		<strong><spring:message code="general.taxonomy" />: </strong><a
			href="${contextPath}/edition/acronym/${edition.getAcronym()}/taxonomy">
			${edition.title}</a>
	</p>
</c:if>
<p>
	<strong>${edition.getSortedInterps().size()} <spring:message code="fragments" />: </strong>
</p>
<table id="tablevirtual" data-pagination="false" style="display: none;">
	<!-- <table class="table table-hover table-condensed"> -->
	<thead>
		<tr>
			<th><span class="tip"
				title="<spring:message code="tableofcontents.tt.number" />"><spring:message
						code="tableofcontents.number" /></span></th>
			<th><span class="tip"
				title="<spring:message code="tableofcontents.tt.title" />"><spring:message
						code="tableofcontents.title" /></span></th>
			<th><span class="tip"
				title="<spring:message code="tableofcontents.tt.taxonomy" />"><spring:message
						code="general.category" /></span></th>
			<th><span class="tip"
				title="<spring:message code="tableofcontents.tt.usesEditions" />"><spring:message
						code="tableofcontents.usesEditions" /></span></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="inter" items='${edition.sortedInterps}'>
			<tr>
				<td><c:if test="${inter.number!=0}">${inter.number}</c:if></td>
				<td><a
					href="${contextPath}/fragments/fragment/${inter.getFragment().getXmlId()}/inter/${inter.getUrlId()}">${inter.title}</a></td>
				<td><c:forEach var="category"
						items='${inter.getAssignedCategories()}'>
						<a
							href="${contextPath}/edition/acronym/${category.getTaxonomy().getEdition().getAcronym()}/category/${category.getUrlId()}">
							${category.getNameInEditionContext(edition)}</a>
						<br>
					</c:forEach></td>
				<td><c:forEach var="used" items="${inter.getListUsed()}">-><a
							href="${contextPath}/fragments/fragment/${used.getFragment().getXmlId()}/inter/${used.getUrlId()}">${used.shortName}</a>
						<br>
					</c:forEach></td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<script>
	$('#tablevirtual').attr("data-search", "true");
	$('#tablevirtual').bootstrapTable();
	$(".tip").tooltip({
		placement : 'bottom'
	});
</script>
<script>
	$(document).ready(function() {
		$('#tablevirtual').show();
	});
</script>