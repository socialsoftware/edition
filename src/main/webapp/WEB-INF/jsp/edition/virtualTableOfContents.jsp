<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<h3 class="text-center">
	<spring:message code="virtualedition" />
	${edition.title} (${edition.getSortedInterps().size()})
</h3>
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
					href="${contextPath}/fragments/fragment/inter/${inter.externalId}">${inter.title}</a></td>
				<td><c:forEach var="category"
						items='${inter.getAssignedCategories()}'>
						<a
							href="${contextPath}/edition/category/${category.getExternalId()}">
							${category.getNameInEditionContext(edition)}</a>
						<br>
					</c:forEach></td>
				<td><c:forEach var="used" items="${inter.getListUsed()}">-><a
							href="${contextPath}/fragments/fragment/inter/${used.externalId}">${used.shortName}</a>
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