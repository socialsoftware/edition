<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

	<div class="container">
		<br />
		<div class="row col-md-1">
			<form class="form-inline" method="GET"
				action="${contextPath}/virtualeditions/restricted/manage/${fragInter.getEdition().getExternalId()}">
				<input type="hidden" name="${_csrf.parameterName}"
					value="${_csrf.token}" />
				<button type="submit" class="btn btn-default">
					<span class="glyphicon glyphicon-arrow-left"></span>
					<spring:message code="general.back" />
				</button>
			</form>
		</div>
		<div class="col-md-12">
			<h3 class="text-center">
				${fragInter.getEdition().getTitle()} - <a
					href="${contextPath}/virtualeditions/restricted/${fragInter.getEdition().getExternalId()}/taxonomy"><spring:message
						code="general.taxonomy" /></a> - ${fragInter.getTitle()}
			</h3>
			<br>
			<div class="row pull-right" align="center">
				<span class="bg-info" style="padding: 8px"> <spring:message
						code="general.public.pages" />: <a
					href="${contextPath}/edition/acronym/${fragInter.getEdition().getAcronym()}">
						<span class="glyphicon glyphicon-list-alt"></span> <spring:message
							code="general.edition" />
				</a> - <a
					href="${contextPath}/fragments/fragment/${fragInter.getFragment().getXmlId()}/inter/${fragInter.getUrlId()}">
						<span class="glyphicon glyphicon-align-left"></span> <spring:message
							code="fragment" />
				</a>
				</span>
			</div>
			<br>
			<div class="row">
				<table class="table table-hover">
					<thead>
						<tr>
							<th><spring:message code="general.category" /></th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td><c:forEach var="category"
									items='${fragInter.getSortedCategories(fragInter.getEdition())}'>
									<a
										href="${contextPath}/virtualeditions/restricted/category/${category.getExternalId()}">${category.getName()}
									</a>
									<br />
								</c:forEach></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>
