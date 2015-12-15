<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

	<div class="container">
		<h1 class="text-center">
			<spring:message code="virtualedition" />
			: ${fragInter.getEdition().getTitle()}
		</h1>
		<h2 class="text-center">
		${fragInter.getTitle()} :
			<a
				href="${contextPath}/virtualeditions/restricted/${fragInter.getEdition().getExternalId()}/taxonomy"><spring:message
					code="general.taxonomy" /></a>
		</h2>

		<h4 class="pull-right">
			<spring:message code="general.public.pages" />
			- <a
				href="${contextPath}/edition/internalid/${fragInter.getEdition().getExternalId()}">
				<spring:message code="general.edition" /></a> : <a
				href="${contextPath}/fragments/fragment/inter/${fragInter.getExternalId()}">
				${fragInter.getTitle()}</a>
		</h4>
		<br />
		<div class="row">
			<table class="table table-hover">
				<thead>
					<tr>
						<th><spring:message code="general.category" /></th>
					</tr>
				</thead>
				<tbody>
					<c:set var="taxonomy"
						value='${fragInter.getEdition().getTaxonomy()}' />
					<c:forEach var="tag" items='${taxonomy.getSortedTags(fragInter)}'>
						<tr>
							<td><a
								href="${contextPath}/virtualeditions/restricted/category/${tag.getCategory().getExternalId()}">${tag.getCategory().getName()}</a>
								(${tag.getWeight()})</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>
