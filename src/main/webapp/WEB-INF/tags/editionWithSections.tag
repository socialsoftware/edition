<%@ attribute name="sections" type="java.util.List" required="true" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="edition" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:forEach var="section" items='${sections}'>
	<c:choose>
		<c:when test="${section.isLeaf()}">
			<td rowspan="${section.size()}" class="level">
				<p>${section.getTitle()}</p>
			</td>
			
			<!--<c:set var="interList" value="${section.getIntersAsList() }"/>-->
			<c:forEach var="inter" items="${section.getSortedInters()}" varStatus="i">
				<c:if test="${i.index > 0}">
					<tr>
				</c:if>
				<c:if test="${(heteronym == null) || (inter.heteronym == heteronym)}">
						<td>
							<c:if test="${inter.number!=0}">
								${inter.number}
							</c:if>
						</td>
						<td>
							<a id="${inter.externalId}" class="inter"
								href="${contextPath}/fragments/fragment/inter/${inter.externalId}">
								${inter.title}</a>
						</td>
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
						<td><c:forEach var="used" items="${inter.getListUsed()}">-><a
										href="${contextPath}/fragments/fragment/inter/${used.externalId}">${used.shortName}</a>
							</c:forEach>
						</td>
					</tr>
					</c:if>
					
					
			
			
			</c:forEach>
			
			
			
		</c:when>
		<c:when test="${section.isRootSection()}">
		<tr>
			<td rowspan="${section.size()}" class="level">
				<p>${section.getTitle()}</p>
			</td>
			<edition:editionWithSections sections="${section.getSortedSubSections()}"/>
		</c:when>
		<c:when test="${!section.isLeaf()}">
		
			<td rowspan="${section.size()}" class="level">
				<p>${section.getTitle()}</p>
			</td>
			 <edition:editionWithSections sections="${section.getSortedSubSections()}"/>
		</c:when>
	</c:choose>			
</c:forEach>