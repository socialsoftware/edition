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
		<h3 class="text-center">
			<spring:message code="general.taxonomy" />: ${taxonomy.getEdition().title}
		</h3>
		<br />
		<p>
			<strong><spring:message code="virtualedition" />:</strong> <a
				href="${contextPath}/edition/acronym/${taxonomy.getEdition().getAcronym()}">
					${taxonomy.getEdition().title}</a> 
		</p>
		<p>
			<strong>${taxonomy.getCategoriesSet().size()} <spring:message
					code="general.categories" />:
			</strong>
		</p>
		<br />
		<table id="tableTaxonomy" data-pagination="false"
			style="display: none;">
			<!-- <table class="table table-hover table-condensed"> -->
			<thead>
				<tr>
					<th><spring:message code="general.category" /></th>
					<th>Users</th>
					<th>Editions</th>
					<th><spring:message code="interpretations" /></th>
				</tr>
			<tbody>
				<c:forEach var="category" items='${taxonomy.getCategoriesSet()}'>
					<tr>
						<td><a
							href="${contextPath}/edition/acronym/${taxonomy.getEdition().getAcronym()}/category/${category.getUrlId()}">${category.getName()}</a>
						</td>
						<td><c:forEach var="user"
								items="${category.getSortedUsers()}">
								<a href="${contextPath}/edition/user/${user.getUsername()}">${user.firstName} ${user.lastName} (${user.getUsername()})</a>
								<br>
							</c:forEach></td>
						<td><c:forEach var="edition"
								items="${category.getSortedEditions()}">
								<a
									href="${contextPath}/edition/acronym/${taxonomy.getEdition().getAcronym()}">${edition.getTitle()}</a>
								<br>
							</c:forEach></td>
						<td><c:forEach var="inter"
								items='${category.getSortedInters()}'>
								<a
									href="${contextPath}/fragments/fragment/${inter.getFragment().getXmlId()}/inter/${inter.getUrlId()}">
									${inter.getTitle()}</a>
								<br>
							</c:forEach></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</body>
<script>
	$('#tableTaxonomy').attr("data-search", "true");
	$('#tableTaxonomy').bootstrapTable();
	$(".tip").tooltip({
		placement : 'bottom'
	});
</script>
<script>
	$(document).ready(function() {
		$('#tableTaxonomy').show();
	});
</script>
</html>

