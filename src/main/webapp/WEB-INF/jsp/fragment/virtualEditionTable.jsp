<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<link rel="stylesheet" type="text/css"
	href="/resources/css/bootstrap-table.min.css">
<script src="/resources/js/bootstrap-table.min.js"></script>

<table class="table table-bordered table-striped table-condensed">
	<thead>
		<tr>
			<th><spring:message code="virtualcompare.quote" /></th>
			<th><spring:message code="virtualcompare.comment" /></th>
			<th><spring:message code="virtualcompare.user" /></th>
			<th><spring:message code="general.tags" /></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="annotation" items='${inter.getAnnotationSet()}'>
			<tr>
				<td>${annotation.quote}</td>
				<td>${annotation.text}</td>
				<td><span class="glyphicon glyphicon-user"></span> <a
					href="${contextPath}/edition/user/${annotation.user.username}">${annotation.user.username}</a></td>
				<td><c:forEach var="tag" items='${annotation.getTagSet()}'>
						<span class="glyphicon glyphicon-tag"></span>
						<a
							href="${contextPath}/edition/category/${tag.getCategory().getExternalId()}">${tag.getCategory().getName()}</a>
					</c:forEach></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
