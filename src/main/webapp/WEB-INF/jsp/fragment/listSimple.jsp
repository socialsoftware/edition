<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<div id="fragmentList" class="row">
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th><spring:message code="tableofcontents.title"/></th>
			</tr>
		<tbody>
			<c:forEach var="fragment" items='${fragments}'>
				<tr>
					<td><a
						href="${contextPath}/fragments/fragment/${fragment.externalId}">${fragment.title}</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>