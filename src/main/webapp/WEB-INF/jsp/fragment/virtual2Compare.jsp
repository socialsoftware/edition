<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>

<div id=fragmentInter class="row">
	<h4 class="text-center">
		<spring:message code="virtualcompare.title" />
	</h4>
	<br>
	<c:forEach var="inter" items="${inters}">
		<div class="row col-md-12">
			<h5>
				<strong><spring:message code="general.edition" />:</strong>
				${inter.edition.getReference()}
			</h5>
			<%@ include file="/WEB-INF/jsp/fragment/virtualEditionTable.jsp"%>
		</div>
	</c:forEach>
</div>
