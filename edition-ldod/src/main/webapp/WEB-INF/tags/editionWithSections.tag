<%@ attribute name="cluster"
	type="pt.ist.socialsoftware.edition.ldod.recommendation.Cluster"
	required="true"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="edition"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:forEach var="key" items='${cluster.nodes.keySet()}'>
	<c:choose>
		<c:when test="${cluster.nodes.get(key).getNodes().size()==0}">
			
				<td rowspan="${cluster.nodes.get(key).size()}" class="level"><c:forEach
						var="property" items="${cluster.getProperties()}">
						<p>${property.getTitle(cluster.nodes.get(key).getFirst())}</p>
					</c:forEach> <!--<p>${key}</p>-->  <p>
					<fmt:formatNumber type="number" minFractionDigits="1"
						maxFractionDigits="5" value="${key}" />
				</p> </td>
				<td><c:if
						test="${cluster.nodes.get(key).inters.get(0).number!=0}">${cluster.nodes.get(key).inters.get(0).number}</c:if></td>
				<td><c:choose>
						<c:when
							test="${cluster.nodes.get(key).inters.get(0).externalId == selected}">
							<a id="${cluster.nodes.get(key).inters.get(0).externalId}"
								class="inter selected"
								href="${contextPath}/fragments/fragment/inter/${cluster.nodes.get(key).inters.get(0).externalId}">${cluster.nodes.get(key).inters.get(0).title}</a>
						</c:when>
						<c:otherwise>
							<a id="${cluster.nodes.get(key).inters.get(0).externalId}"
								class="inter"
								href="${contextPath}/fragments/fragment/inter/${cluster.nodes.get(key).inters.get(0).externalId}">${cluster.nodes.get(key).inters.get(0).title}</a>
						</c:otherwise>
					</c:choose></td>
				<td><c:choose>
						<c:when
							test="${cluster.nodes.get(key).inters.get(0).externalId == selected}">
							<button type="submit"
								class="btn btn-primary btn-sm sort disabled">
								<span class="glyphicon glyphicon-check"></span>
								<spring:message code="general.selected" />
							</button>
						</c:when>
						<c:otherwise>
							<button type="submit" class="btn btn-primary btn-sm sort">
								<span class="glyphicon glyphicon-check"></span>
								<spring:message code="recommendation.setinitial" />
							</button>
						</c:otherwise>
					</c:choose></td>
				<td><a
					href="${contextPath}/edition/taxonomy/${edition.getTaxonomy().getExternalId()}">${edition.getTaxonomy().getName()}</a>
				</td>
				<td><c:forEach var="used"
						items="${cluster.nodes.get(key).inters.get(0).getListUsed()}">-><a
							href="${contextPath}/fragments/fragment/inter/${used.externalId}">${used.shortName}</a>
					</c:forEach></td>
			</tr>
			<c:forEach var="i" begin="1"
				end="${cluster.nodes.get(key).inters.size()-1}">
				<tr>

					<td><c:if
							test="${cluster.nodes.get(key).inters.get(i).number!=0}">${cluster.nodes.get(key).inters.get(i).number}</c:if></td>
					<td><c:choose>
							<c:when
								test="${cluster.nodes.get(key).inters.get(i).externalId == selected}">
								<a id="${cluster.nodes.get(key).inters.get(i).externalId}"
									class="inter selected"
									href="${contextPath}/fragments/fragment/inter/${cluster.nodes.get(key).inters.get(i).externalId}">${cluster.nodes.get(key).inters.get(i).title}</a>
							</c:when>
							<c:otherwise>
								<a id="${cluster.nodes.get(key).inters.get(i).externalId}"
									class="inter"
									href="${contextPath}/fragments/fragment/inter/${cluster.nodes.get(key).inters.get(i).externalId}">${cluster.nodes.get(key).inters.get(i).title}</a>
							</c:otherwise>
						</c:choose></td>
					<td><c:choose>
							<c:when
								test="${cluster.nodes.get(key).inters.get(i).externalId == selected}">
								<button type="submit"
									class="btn btn-primary btn-sm sort disabled">
									<span class="glyphicon glyphicon-check"></span>
									<spring:message code="general.selected" />
								</button>
							</c:when>
							<c:otherwise>
								<button type="submit" class="btn btn-primary btn-sm sort">
									<span class="glyphicon glyphicon-check"></span>
									<spring:message code="recommendation.setinitial" />
								</button>
							</c:otherwise>
						</c:choose> <!--  <a id="${cluster.nodes.get(key).inters.get(i).externalId}" class="inter"
							href="${contextPath}/fragments/fragment/inter/${cluster.nodes.get(key).inters.get(i).externalId}">${cluster.nodes.get(key).inters.get(i).title}</a></td>
					<td>
						<button type="submit" class="btn btn-primary btn-sm sort">
							<span class="glyphicon glyphicon-check"></span> 
							<spring:message code="general.select"/>
						</button>
					</td>--><td><a
						href="${contextPath}/edition/taxonomy/${edition.getTaxonomy().getExternalId()}">${edition.getTaxonomy().getName()}</a>
					</td>
					<td><c:forEach var="used"
							items="${cluster.nodes.get(key).inters.get(i).getListUsed()}">-><a
								href="${contextPath}/fragments/fragment/inter/${used.externalId}">${used.shortName}</a>
						</c:forEach>
				</td></tr>
			</c:forEach>
		</c:when>
		<c:when test="${cluster.getOrder() == 0}">
			<tr>
				<td rowspan="${cluster.nodes.get(key).size()}" class="level"><c:forEach
						var="property" items="${cluster.getProperties()}">
						<p>${property.getTitle(cluster.nodes.get(key).getFirst()) }</p>
					</c:forEach> <!-- <p>${key}</p>-->
					<p>
						<fmt:formatNumber type="number" minFractionDigits="1"
							maxFractionDigits="5" value="${key}" />
					</p></td>
				<edition:editionWithSections cluster="${cluster.nodes.get(key)}" />
		</c:when>
		<c:when test="${cluster.getOrder() > 0}">
			<td rowspan="${cluster.nodes.get(key).size()}" class="level"><c:forEach
					var="property" items="${cluster.getProperties()}">
					<p>${property.getTitle(cluster.nodes.get(key).getFirst()) }</p>
				</c:forEach> <!--  <p>${key}</p>-->
				<p>
					<fmt:formatNumber type="number" minFractionDigits="1"
						maxFractionDigits="5" value="${key}" />
				</p></td>
			<edition:editionWithSections cluster="${cluster.nodes.get(key)}" />
		</c:when>
	</c:choose>
</c:forEach>