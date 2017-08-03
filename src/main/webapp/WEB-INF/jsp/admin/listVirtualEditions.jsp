<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

	<div class="container">
		<h3 class="text-center">Manage Virtual Editions</h3>
		<br /> <br />
		<div id="fragmentList" class="row">
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th>Acronym</th>
						<th>Title</th>
						<th>Editors</th>
						<th>Categories</th>
						<th>Annotations</th>
						<th></th>
					</tr>
				<tbody>
					<c:forEach var="edition" items='${editions}'>
						<tr>
							<td>${edition.getAcronym()}</td>
							<td>${edition.getTitle()}</td>
							<td><c:forEach var="participant"
									items="${edition.getParticipantSet()}">${participant.getUsername()} </c:forEach></td>
							<td><c:forEach var="category"
									items="${edition.getTaxonomy().getCategoriesSet()}">${category.getName()}, </c:forEach></td>
							<td><c:forEach var="text"
									items="${edition.getAnnotationTextList()}">${text}, </c:forEach></td>
							<td><form class="form-inline" method="POST"
									action="${contextPath}/admin/virtual/delete">
									${fragment.title}<input type="hidden"
										name="${_csrf.parameterName}" value="${_csrf.token}" /> <input
										type="hidden" name="externalId" value="${edition.externalId}" />
									<button type="submit" class="btn btn-danger btn-sm pull-right">
										<span class="glyphicon glyphicon-remove"></span>
										<spring:message code="general.remove" />
									</button>
								</form></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>

