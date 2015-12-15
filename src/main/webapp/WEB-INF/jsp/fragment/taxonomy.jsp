<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<div class="row" id="taxonomy">
	<c:set var="taxonomy"
		value='${inters.get(0).getEdition().getTaxonomy()}' />
	<c:set var="authorized"
		value="${pageContext.request.userPrincipal.authenticated && taxonomy.getEdition().getParticipantSet().contains(pageContext.request.userPrincipal.principal.getUser())}" />
	<table class="table table-hover">
		<thead>

			<tr>
				<th><span class="glyphicon glyphicon-tag"></span></th>
				<th><span class="glyphicon glyphicon-user"></span></th>
				<th><c:if test="${authorized}">
						<a
							href="${contextPath}/virtualeditions/restricted/tag/associateForm/${taxonomy.getExternalId()}/${inters.get(0).getExternalId()}">
							<span class="glyphicon glyphicon-plus"></span>
						</a>
					</c:if></th>

			</tr>
		</thead>
		<tbody>
			<c:forEach var="category"
				items='${taxonomy.getSortedCategories(inters.get(0))}'>
				<tr>
					<td><a
						href="${contextPath}/edition/category/${category.getExternalId()}">
							${category.getName()}</a> <c:if
							test="${authorized}">
							<a
								href="${contextPath}/virtualeditions/restricted/fraginter/${inters.get(0).getExternalId()}/tag/dissociate/${category.getExternalId()}"><span
								class="glyphicon glyphicon-remove"></span></a>
						</c:if></td>
					<td><c:forEach var="user"
							items='${category.getContributorSet(inters.get(0))}'>
							<a href="${contextPath}/edition/user/${user.username}">${user.username}</a>
						</c:forEach></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
