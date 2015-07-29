<%@ taglib tagdir="/WEB-INF/tags" prefix="edition"%>
<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<div id="result-type-iterative"></div>
<table id="virtual-table" class="table table-condensed table-hover">
	<thead>
		<tr>
			<c:forEach var="i" begin="1"
						end="${cluster.getNumberOfIterations()}">
				<th>${i}</th>
			</c:forEach>
			<th><spring:message code="tableofcontents.number" /></th>
			<th><spring:message code="tableofcontents.title" /></th>
			<th></th>
			<th><spring:message code="general.taxonomy" /></th>
			<th><spring:message code="tableofcontents.usesEditions" /></th>
		</tr>
	</thead>
	<tbody>
		<edition:cluster cluster="${cluster}" />
	</tbody>
</table>