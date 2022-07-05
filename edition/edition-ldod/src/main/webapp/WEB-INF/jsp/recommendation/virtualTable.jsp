<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<table id="virtual-table" class="table table-condensed table-hover">
	<thead>
		<tr>
			<th><spring:message code="tableofcontents.number" /></th>
			<th><spring:message code="tableofcontents.title" /></th>
			<th></th>
			<th><spring:message code="tableofcontents.usesEditions" /></th>
		</tr>
	</thead>
	<tbody>
		<c:if test="${not empty inters}">
			<c:forEach var="inter" items='${inters}'>
				<tr>
					<td><c:if test="${inter.number!=0}">${inter.number}</c:if></td>
					<td><c:choose>
							<c:when test="${inter.externalId == selected}">
								<a id="${inter.externalId}" class="inter selected"
									href="${contextPath}/fragments/fragment/${inter.getFragment().getXmlId()}/inter/${inter.getUrlId()}">${inter.title}</a>
							</c:when>
							<c:otherwise>
								<a id="${inter.externalId}" class="inter"
									href="${contextPath}/fragments/fragment/${inter.getFragment().getXmlId()}/inter/${inter.getUrlId()}">${inter.title}</a>
							</c:otherwise>
						</c:choose></td>
					<td><c:if test="${inter.externalId != selected}">
							<button type="submit" class="btn btn-primary btn-sm sort">
								<span class="glyphicon glyphicon-check"></span>
								<spring:message code="recommendation.setinitial" />
							</button>
						</c:if></td>
					<td><c:forEach var="used" items="${inter.getListUsed()}">-><a
								href="${contextPath}/fragments/fragment/${used.getFragment().getXmlId()}/inter/${used.getUrlId()}">${used.shortName}</a>
						</c:forEach></td>
				</tr>
			</c:forEach>
		</c:if>
	</tbody>
</table>