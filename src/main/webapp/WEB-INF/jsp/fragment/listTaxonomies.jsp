<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<div class="row" id="taxonomies">
<table class="table table-hover">
	<thead>

		<tr>
			<th><spring:message code="general.taxonomy" /></th>
			<th><span class="glyphicon glyphicon-tag"></span></th>
			<th><span class="glyphicon glyphicon-user"></span></th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="taxonomy"
			items='${inters.get(0).getEdition().getTaxonomiesSet()}'>
			<tr>
				<td><a
					href="${contextPath}/edition/taxonomy/${taxonomy.externalId}">${taxonomy.getName()}</a>
				</td>
				<td><c:forEach var="tag"
						items='${taxonomy.getSortedActiveTags(inters.get(0))}'>
						<a
							href="${contextPath}/edition/category/${tag.getActiveCategory().getExternalId()}">
							${tag.getActiveCategory().getName()}</a>
						<c:if test="${taxonomy.getAdHoc() }">
                                (${tag.getWeight()})</c:if>
						<c:if test="${!taxonomy.getAdHoc() }">
							<a
								href="${contextPath}/virtualeditions/restricted/tag/dissociate/${tag.getExternalId()}"><span
								class="glyphicon glyphicon-remove"></span></a>							
						</c:if>
					</c:forEach></td>
				<td><c:forEach var="user"
						items='${inters.get(0).getTagContributorSet(taxonomy)}'>
						<a href="${contextPath}/edition/user/${user.username}">${user.username}</a>
					</c:forEach></td>
				<td><c:if test="${!taxonomy.getAdHoc() }">
						<a
							href="${contextPath}/virtualeditions/restricted/tag/associateForm/${taxonomy.getExternalId()}/${inters.get(0).getExternalId()}">
							<span class="glyphicon glyphicon-plus"></span>
						</a>
					</c:if></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
</div>
