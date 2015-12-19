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
		<c:choose>
			<c:when test="${not empty inters}">
				<c:forEach var="inter" items='${inters}'>
					<c:if
						test="${(heteronym == null) || (inter.heteronym == heteronym)}">
						<tr>
							<td><c:if test="${inter.number!=0}">${inter.number}</c:if></td>
							<td><c:choose>
									<c:when test="${inter.externalId == selected}">
										<a id="${inter.externalId}" class="inter selected"
											href="${contextPath}/fragments/fragment/inter/${inter.externalId}">${inter.title}</a>
									</c:when>
									<c:otherwise>
										<a id="${inter.externalId}" class="inter"
											href="${contextPath}/fragments/fragment/inter/${inter.externalId}">${inter.title}</a>
									</c:otherwise>
								</c:choose></td>
							<td><c:choose>
									<c:when test="${inter.externalId == selected}">
										<button type="submit"
											class="btn btn-primary btn-sm sort disabled">
											<span class="glyphicon glyphicon-check"></span>
											<spring:message code="general.selected" />
										</button>
									</c:when>
									<c:otherwise>
										<button type="submit" class="btn btn-primary btn-sm sort">
											<span class="glyphicon glyphicon-check"></span>
											<spring:message code="general.select" />
										</button>
									</c:otherwise>
								</c:choose></td>
							<td><c:forEach var="used" items="${inter.getListUsed()}">-><a
										href="${contextPath}/fragments/fragment/inter/${used.externalId}">${used.shortName}</a>
								</c:forEach></td>
						</tr>
					</c:if>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<c:forEach var="inter" items='${edition.sortedInterps}'>
					<c:if
						test="${(heteronym == null) || (inter.heteronym == heteronym)}">
						<tr>
							<td><c:if test="${inter.number!=0}">${inter.number}</c:if></td>
							<td><c:choose>
									<c:when test="${inter.externalId == selected}">
										<a id="${inter.externalId}" class="inter selected"
											href="${contextPath}/fragments/fragment/inter/${inter.externalId}">${inter.title}</a>
									</c:when>
									<c:otherwise>
										<a id="${inter.externalId}" class="inter"
											href="${contextPath}/fragments/fragment/inter/${inter.externalId}">${inter.title}</a>
									</c:otherwise>
								</c:choose></td>
							<td><c:choose>
									<c:when test="${inter.externalId == selected}">
										<button type="submit"
											class="btn btn-primary btn-sm sort disabled">
											<span class="glyphicon glyphicon-check"></span>
											<spring:message code="general.selected" />
										</button>
									</c:when>
									<c:otherwise>
										<button type="submit" class="btn btn-primary btn-sm sort">
											<span class="glyphicon glyphicon-check"></span>
											<spring:message code="general.select" />
										</button>
									</c:otherwise>
								</c:choose></td>
							<td><c:forEach var="used" items="${inter.getListUsed()}">-><a
										href="${contextPath}/fragments/fragment/inter/${used.externalId}">${used.shortName}</a>
								</c:forEach></td>
						</tr>
					</c:if>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</tbody>
</table>