<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- class="table table-hover table-condensed" -->
<div>
	<h1>Included</h1>
	<table border="1" class="result-table">
		<thead>
			<tr>
				<td><spring:message code="fragment" /> (${fragCount})</td>
				<td><spring:message code="interpretations" /> (${interCount})</td>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${results}" var="fragmentEntry">
				<c:forEach items="${ fragmentEntry.value }" var="fragInterEntry">
					<tr>
						<td><a
							href="/fragments/fragment/${fragmentEntry.key.getExternalId()}">${fragmentEntry.key.getTitle()}</a></td>
						<c:choose>
							<c:when
								test="${ fragInterEntry.getClass().getSimpleName().equals(\"SourceInter\") && 
										fragInterEntry.getSource().getType() == 'MANUSCRIPT'}">
								<td><a
									href="/fragments/fragment/inter/${fragInterEntry.getExternalId()}">${fragInterEntry.getShortName()}</a></td>
							</c:when>
							<c:when
								test="${ fragInterEntry.getClass().getSimpleName().equals(\"ExpertEditionInter\")}">
								<td><a
									href="/fragments/fragment/inter/${fragInterEntry.getExternalId()}">${fragInterEntry.getTitle()} (${fragInterEntry.getEdition().getEditor()})</a></td>
							</c:when>
							<c:otherwise>
								<td><a
									href="/fragments/fragment/inter/${fragInterEntry.getExternalId()}">${fragInterEntry.getTitle()}</a></td>
							</c:otherwise>
						</c:choose>
				</c:forEach>
			</c:forEach>
		</tbody>
	</table>
</div>