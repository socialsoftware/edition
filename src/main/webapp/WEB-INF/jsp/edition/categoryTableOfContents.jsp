<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

	<div class="container">
		<h3 class="text-center">
			<spring:message code="virtualedition" />
			<a
				href="${contextPath}/edition/internalid/${category.getTaxonomy().getEdition().getExternalId()}">
				${category.getTaxonomy().getEdition().title}</a>
		</h3>
		<h4 class="text-center">
			<spring:message code="general.category" />
			${category.getName()} (${category.getTagSet().size()})
		</h4>

		<table class="table table-hover table-condensed">
			<thead>
				<tr>
					<th><spring:message code="tableofcontents.number" /></th>
					<th><spring:message code="tableofcontents.title" /></th>
					<th><spring:message code="general.weight" /></th>
					<th><spring:message code="tableofcontents.usesEditions" /></th>
				</tr>
			<tbody>
				<c:forEach var="categoryInFragInter"
					items='${category.getSortedTags()}'>
					<tr>
						<td>${categoryInFragInter.getInter().getNumber()}</td>
						<td><a
							href="${contextPath}/fragments/fragment/inter/${categoryInFragInter.getInter().getExternalId()}">${categoryInFragInter.getInter().getTitle()}</a></td>
						<td>${categoryInFragInter.getWeight()}</td>
						<td><c:forEach var="used"
								items="${categoryInFragInter.getInter().getListUsed()}">-><a
									href="${contextPath}/fragments/fragment/inter/${used.externalId}">${used.shortName}</a>
							</c:forEach></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</body>
</html>

