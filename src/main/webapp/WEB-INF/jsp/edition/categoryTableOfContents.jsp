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
			<spring:message code="general.taxonomy" />
			<a href="${contextPath}/edition/taxonomy/${category.getTaxonomy().getExternalId()}">
				${category.getTaxonomy().getEdition().title}</a>: <spring:message code="general.category" />
			${category.getName()} (${category.getTagSet().size()})
		</h3>
		<br>
		<table class="table table-hover table-condensed">
			<thead>
				<tr>
					<th><spring:message code="tableofcontents.title" /></th>
					<th><spring:message code="virtualedition" /></th>
					<th><spring:message code="user.user" /></th>
					<th><spring:message code="tableofcontents.usesEditions" /></th>
				</tr>
			<tbody>
				<c:forEach var="inter" items='${category.getSortedInters()}'>
					<tr>
						<td><a
							href="${contextPath}/fragments/fragment/inter/${inter.getExternalId()}">${inter.getTitle()}</a></td>
						<td><a
							href="${contextPath}/edition/internalid/${inter.getEdition().getExternalId()}">${inter.getEdition().getTitle()}</a></td>
						<td><c:forEach var="user"
								items="${inter.getContributorSet(category)}">
								<a href="${contextPath}/edition/user/${user.username}">${user.username}</a><span style="padding-left:1em"/> 
							</c:forEach></td>
						<td><c:forEach var="used" items="${inter.getListUsed()}">-><a
									href="${contextPath}/fragments/fragment/inter/${used.externalId}">${used.shortName}</a>
							</c:forEach></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</body>
</html>

