<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<h3 class="text-center">
	<spring:message code="virtualedition" />
	${edition.title} (${edition.getSortedInterps().size()})
</h3>
<c:if test="${heteronym != null}">
	<h4 class="text-left">
		<spring:message code="tableofcontents.fragmentsof" />
		${heteronym.name}
	</h4>
</c:if>

<table class="table table-condensed table-hover">
	<thead>
		<tr>
			<th><spring:message code="tableofcontents.number" /></th>
			<th><spring:message code="tableofcontents.title" /></th>
			<th><spring:message code="general.category" /></th>
			<th><spring:message code="tableofcontents.usesEditions" /></th>
		</tr>
	<tbody>
		<c:forEach var="inter" items='${edition.sortedInterps}'>
			<c:if test="${(heteronym == null) || (inter.heteronym == heteronym)}">
				<tr>
					<td><c:if test="${inter.number!=0}">${inter.number}</c:if></td>
					<td><a
						href="${contextPath}/fragments/fragment/inter/${inter.externalId}">${inter.title}</a></td>
					<td><c:forEach var="tag"
								items='${inter.getEdition().getTaxonomy().getSortedTags(inter)}'>
								<a
									href="${contextPath}/edition/category/${tag.getCategory().getExternalId()}">
									${tag.getCategory().getName()} </a> (${tag.getWeight()})
                            </c:forEach></td>
					<td><c:forEach var="used" items="${inter.getListUsed()}">-><a
								href="${contextPath}/fragments/fragment/inter/${used.externalId}">${used.shortName}</a>
						</c:forEach></td>
				</tr>
			</c:if>
		</c:forEach>
	</tbody>
</table>
