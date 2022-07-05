<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
<link rel="stylesheet" type="text/css"
	href="/resources/css/bootstrap-table.min.css">
<script src="/resources/js/bootstrap-table.min.js"></script>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

	<div class="container">
		<h3 class="text-center"><spring:message code="general.citations.twitter" /> (${citations.size()})</h3>
		<br />
		<table id="tablecitations" data-pagination="false" style="display: none;">
			<thead>
				<tr>
					<th><strong><spring:message code="general.date" /></strong></th>
					<th><strong><spring:message code="fragment" /></strong></th>
					<th><strong>Tweet</strong></th>
					<th><strong><spring:message code="general.text" /></strong></th>
					<th><strong><spring:message code="criteria.geolocation" /></strong></th>
					<th><strong><spring:message code="general.country" /></strong></th>
					<th><strong><spring:message code="user.role.user" /></strong></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="citation" items='${citations}'>
					<tr>
						<td>
							<fmt:parseDate value="${citation.getFormatedDate()}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
							<fmt:formatDate pattern="dd-MM-yyyy HH:mm" value="${ parsedDateTime }" />
						</td>
						<td><a
							href="${contextPath}/fragments/fragment/${citation.getFragment().xmlId}">${citation.getFragment().getTitle()}</a></td>
						<td><a href="${citation.getSourceLink()}" target="_blank">Tweet</a></td>
						<td>${citation.getTweetText()}</td>
						<td><c:if test='${!citation.getLocation().equals("unknown")}'>${citation.getLocation()}</c:if></td>
						<td><c:if test='${!citation.getCountry().equals("unknown")}'>${citation.getCountry()}</c:if></td>
						<td><a href="https://twitter.com/${citation.getUsername()}" target="_blank">${citation.getUsername()}</a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<br />
	<script>
		$('#tablecitations').attr("data-search", "true");
		$('#tablecitations').bootstrapTable();
	</script>
	<script>
		$(document).ready(function() {
			$('#tablecitations').show();
		});
	</script>
</body>
</html>

