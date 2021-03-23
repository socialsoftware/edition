<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

	<div class="container">
		<h3 class="text-center">
			<spring:message code="deletefragment.title" />
		</h3>
		<br /> <br />
		<div id="fragmentList" class="row">
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th><form class="form-inline" method="POST"
								action="${contextPath}/admin/fragment/deleteAll">
								<input type="hidden" name="${_csrf.parameterName}"
									value="${_csrf.token}" />
								<button type="submit" class="btn btn-danger btn-sm pull-right">
									<span class="glyphicon glyphicon-remove"></span>
									<spring:message code="general.removeAll" />
								</button>
							</form> <spring:message code="tableofcontents.title" /></th>
					</tr>
				<tbody>
					<c:forEach var="fragment" items='${fragments}'>
						<tr>
							<td><form class="form-inline" method="POST"
									action="${contextPath}/admin/fragment/delete">
									${fragment.title}<input type="hidden"
										name="${_csrf.parameterName}" value="${_csrf.token}" /> <input
										type="hidden" name="externalId" value="${fragment.externalId}" />
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

