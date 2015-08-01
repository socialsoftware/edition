<%@ attribute name="cluster" type="pt.ist.socialsoftware.edition.recommendation.Cluster" required="true" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="edition" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:forEach var="key" items='${cluster.nodes.keySet()}'>
	<c:choose>
		<c:when test="${cluster.nodes.get(key).getNodes().size()==0}">
			<td rowspan="${cluster.nodes.get(key).size()}" class="level">
				<c:forEach var="property" items="${cluster.getProperties()}">
					<p>${property.getTitle() }</p>
				</c:forEach>
				<!--<p>${key}</p>-->
				<p><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="5" value="${key}" /></p>
			</td>
			<c:if test="${(heteronym == null) || (cluster.nodes.get(key).inters.get(0).heteronym == heteronym)}">
					<td><c:if test="${cluster.nodes.get(key).inters.get(0).number!=0}">${cluster.nodes.get(key).inters.get(0).number}</c:if></td>
					<td><a id="${cluster.nodes.get(key).inters.get(0).externalId}" class="inter"
							href="${contextPath}/fragments/fragment/inter/${cluster.nodes.get(key).inters.get(0).externalId}">${cluster.nodes.get(key).inters.get(0).title}</a></td>
					<td>
						<button type="submit" class="btn btn-primary btn-sm sort">
							<span class="glyphicon glyphicon-check"></span> 
							<spring:message code="general.select"/>
						</button>
					</td>
					<td><c:forEach var="taxonomy"
							items="${edition.getTaxonomies()}">
							<a
								href="${contextPath}/edition/taxonomy/${taxonomy.getExternalId()}">${taxonomy.getName()}</a>
						</c:forEach></td>
					<td><c:forEach var="used" items="${cluster.nodes.get(key).inters.get(0).getListUsed()}">-><a
									href="${contextPath}/fragments/fragment/inter/${used.externalId}">${used.shortName}</a>
						</c:forEach></td>
					</tr>
				</c:if>
			 <c:forEach var="i" begin="1" end="${cluster.nodes.get(key).inters.size()-1}">
				<tr>
				<c:if test="${(heteronym == null) || (cluster.nodes.get(key).inters.get(i).heteronym == heteronym)}">
					<td><c:if test="${cluster.nodes.get(key).inters.get(i).number!=0}">${cluster.nodes.get(key).inters.get(i).number}</c:if></td>
					<td><a id="${cluster.nodes.get(key).inters.get(i).externalId}" class="inter"
							href="${contextPath}/fragments/fragment/inter/${cluster.nodes.get(key).inters.get(i).externalId}">${cluster.nodes.get(key).inters.get(i).title}</a></td>
					<td>
						<button type="submit" class="btn btn-primary btn-sm sort">
							<span class="glyphicon glyphicon-check"></span> 
							<spring:message code="general.select"/>
						</button>
					</td>
					<td><c:forEach var="taxonomy"
							items="${edition.getTaxonomies()}">
							<a
								href="${contextPath}/edition/taxonomy/${taxonomy.getExternalId()}">${taxonomy.getName()}</a>
						</c:forEach></td>
					<td><c:forEach var="used" items="${cluster.nodes.get(key).inters.get(i).getListUsed()}">-><a
									href="${contextPath}/fragments/fragment/inter/${used.externalId}">${used.shortName}</a>
						</c:forEach></td>
					</tr>
				</c:if>
			</c:forEach>
		</c:when>
		<c:when test="${cluster.getOrder() == 0}">
		<tr>
			<td rowspan="${cluster.nodes.get(key).size()}" class="level">
				<c:forEach var="property" items="${cluster.getProperties()}">
					<p>${property.getTitle() }</p>
				</c:forEach>
				<!-- <p>${key}</p>-->
				<p><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="5" value="${key}" /></p>
			</td>
			<edition:cluster cluster="${cluster.nodes.get(key)}"/>
		</c:when>
		<c:when test="${cluster.getOrder() > 0}">
			<td rowspan="${cluster.nodes.get(key).size()}" class="level">
				<c:forEach var="property" items="${cluster.getProperties()}">
					<p>${property.getTitle() }</p>
				</c:forEach>
				<!--  <p>${key}</p>-->
				<p><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="5" value="${key}" /></p>
			</td>
			 <edition:cluster cluster="${cluster.nodes.get(key)}"/>
		</c:when>
	</c:choose>			
</c:forEach>