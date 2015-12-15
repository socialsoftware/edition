<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<script type="text/javascript">
	function validateForm() {
		var x = document.forms["updateName"]["name"].value;
		if (x == null || x == "") {
			$("#message").html("A categoria deve ter um nome.");
			$("#myModal").modal('show');
			return false;
		}
	}
</script>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

	<div class="container">
		<h1 class="text-center">
			<spring:message code="virtualedition" /> : ${category.getTaxonomy().getEdition().getTitle()}
			</h1>
			<h2 class="text-center">
			<spring:message code="general.taxonomy" />
			: <a
				href="${contextPath}/virtualeditions/restricted/${category.getTaxonomy().getEdition().getExternalId()}/taxonomy">${category.getTaxonomy().getName()}</a>
			<spring:message code="general.category" />
			: ${category.getName()}
		</h2>
		<h4 class="pull-right">
			<spring:message code="general.public.pages" />
			- <a
				href="${contextPath}/edition/internalid/${category.getTaxonomy().getEdition().getExternalId()}">
				<spring:message code="general.edition" /></a> : <a
				href="${contextPath}/edition/taxonomy/${category.getTaxonomy().getExternalId()}"><spring:message code="general.taxonomy" /></a>
			: <a
				href="${contextPath}/edition/category/${category.getExternalId()}"><spring:message code="general.category" /></a>
		</h4>
		<br /> <br /> <br />
		<div class="row pull-right">
			<c:forEach var="error" items='${errors}'>
				<div class="row text-error">${error}</div>
			</c:forEach>
			<form name="updateName" class="form-inline" method="POST"
				action="/virtualeditions/restricted/category"
				onsubmit="return validateForm()">
				<div class="form-group">
					<input type="hidden" class="form-control" name="categoryId"
						value="${category.externalId}" />
				</div>
				<div class="form-group">
					<input type="text" class="form-control" name="name"
						placeholder="<spring:message code="general.name" />"
						value="${category.getName()}" />
				</div>
				<div class="form-group">
					<button type="submit" class="btn btn-primary">
						<span class="glyphicon glyphicon-edit"></span>
						<spring:message code="general.update" />
					</button>
				</div>
			</form>
		</div>
		<br /> <br /><br />
		<div class="row pull-right">
			<form class="form-inline" method="POST"
				action="/virtualeditions/restricted/category/delete">
				<div class="form-group">
					<input type="hidden" class="form-control" name="categoryId"
						value="${category.externalId}" />
				</div>
				<button type="submit" class="btn btn-primary">
					<span class="glyphicon glyphicon-remove"></span>
					<spring:message code="general.delete" />
				</button>
			</form>
		</div>
		<br />
		<div class="row">
			<table class="table table-hover">
				<thead>
					<tr>
						<th><spring:message code="fragments" /></th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td><c:forEach var="tag" items='${category.getSortedTags()}'>
								<a
									href="${contextPath}/virtualeditions/restricted/fraginter/${tag.getInter().getExternalId()}">
									${tag.getInter().getTitle()}</a> (${tag.getWeight()})
                                    </c:forEach></td>
					</tr>

				</tbody>
			</table>
		</div>
		<div class="row pull-right">
			<form class="form-inline" method="GET"
				action="/virtualeditions/restricted/category/extractForm">
				<div class="form-group">
					<input type="hidden" class="form-control" name="categoryId"
						value="${category.getExternalId()}" />
				</div>
				<button type="submit" class="btn btn-primary">
					<span class="glyphicon glyphicon-edit"></span>
					<spring:message code="general.extract" />
				</button>
			</form>
		</div>
	</div>
	<!-- Modal HTML -->
	<div id="myModal" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title">Informação</h4>
				</div>
				<div class="modal-body">
					<h3 id="message" />
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Fechar</button>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
