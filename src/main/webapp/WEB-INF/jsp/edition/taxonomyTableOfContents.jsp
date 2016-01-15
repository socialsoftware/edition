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
			(${taxonomy.getCategoriesSet().size()}) - <spring:message code="virtualedition" /> <a
				href="${contextPath}/edition/internalid/${taxonomy.getEdition().getExternalId()}">
				${taxonomy.getEdition().title}</a>
		</h3>

		<table class="table table-hover table-condensed">
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
							href="${contextPath}/edition/category/${category.getExternalId()}">${category.getName()}</a>
						</td>
						<td><c:forEach var="user" items="${category.getSortedUsers()}"> <a href="${contextPath}/edition/user/${user.getUsername()}">${user.getUsername()}</a></c:forEach></td>
						<td><c:forEach var="edition" items="${category.getSortedEditions()}"> <a href="${contextPath}/edition/internalid/${edition.getExternalId()}">${edition.getTitle()}</a></c:forEach></td>
						<td><c:forEach var="inter"
								items='${category.getSortedInters()}'>
								<a
									href="${contextPath}/fragments/fragment/inter/${inter.getExternalId()}"> ${inter.getTitle()}</a><span style="padding-left:1em"/> 
							</c:forEach></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</body>
</html>
