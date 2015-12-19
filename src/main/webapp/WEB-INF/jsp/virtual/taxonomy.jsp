<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<script type="text/javascript">
function validateForm() {
    var x = document.forms["createTaxonomy"]["numTopics"].value;
    if (x == null || x == "") {
        $("#message").html("Deve ser indicado o número de tópicos a gerar");
        $("#myModal").modal('show');
        return false;
    } else if (isNaN(x) || x < 1) {
        $("#message").html("Deve ser indicado um número de tópicos positivo");
        $("#myModal").modal('show');
        return false;
    }
    x = document.forms["createTaxonomy"]["numWords"].value;
    if (x == null || x == "") {
        $("#message").html("Deve ser indicado o número de palavras a apresentar");
        $("#myModal").modal('show');
        return false;
    } else if (isNaN(x) || x < 1) {
        alert("Deve ser indicado um número de palavras positivo");
        return false;
    }
    x = document.forms["createTaxonomy"]["thresholdCategories"].value;
    if (x == null || x == "") {
        $("#message").html("Deve ser indicado o valor de corte de categorias");
        $("#myModal").modal('show');
        return false;
    } else if (isNaN(x) || x < 0 || x > 100) {
        $("#message").html("Deve ser indicado um valor de corte de categorias entre 0 e 100");
        $("#myModal").modal('show');
        return false;
    }
    x = document.forms["createTaxonomy"]["numIterations"].value;
    if (x == null || x == "") {
        $("#message").html("Deve ser indicado o número de iterações");
        $("#myModal").modal('show');
        return false;
    } else if (isNaN(x) || x < 1) {
        $("#message").html("Deve ser indicado um número de iterações positivo");
        $("#myModal").modal('show');
        return false;
    }
}
</script>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>
	
	<c:set var="taxonomy" value="${virtualEdition.getTaxonomy()}"/>

	<div class="container">
		<h1 class="text-center">
			<spring:message code="general.taxonomy" />
			:
			<spring:message code="general.edition" />
			<a
				href="${contextPath}/virtualeditions/restricted/${virtualEdition.getExternalId()}/taxonomy">
				${virtualEdition.title}</a>
		</h1>
		<h4 class="pull-right">
			<spring:message code="general.public.pages" />
			- <a
				href="${contextPath}/edition/internalid/${virtualEdition.getExternalId()}">
				<spring:message code="general.edition" /></a> : <a
				href="${contextPath}/edition/taxonomy/${virtualEdition.getTaxonomy().getExternalId()}"><spring:message code="general.taxonomy" /></a>
		</h4>
		<br /> <br/> <br/>
		<div class="row col-md-10"></div>
		<br /> <br /> <br />
		<h4><spring:message code="topics.generate" /></h4>
		<div class="row col-md-12">
			<c:forEach var="error" items='${errors}'>
				<div class="row has-error">${error}</div>
			</c:forEach>
		</div>
		<div class="row col-md-12">
			<form name="generateTopics" class="form-inline" method="POST"
				action="/virtualeditions/restricted/taxonomy/createTopics"
				onsubmit="return validateForm()">
				<div class="form-group">
					<input type="hidden" class="form-control" name="externalId"
						value="${virtualEdition.externalId}" />
				</div>
				<div class="form-group">
					<input type="text" class="form-control" name="numTopics"
						placeholder="<spring:message code="general.taxonomies.number.topics" />">
				</div>
				<div class="form-group">
					<input type="text" class="form-control" name="numWords"
						placeholder="<spring:message code="general.taxonomies.number.words" />">
				</div>
				<div class="form-group">
					<input type="text" class="form-control" name="thresholdCategories"
						placeholder="<spring:message code="general.taxonomies.threshold.categories" />">
				</div>
				<div class="form-group">
					<input type="text" class="form-control" name="numIterations"
						placeholder="<spring:message code="general.taxonomies.number.iterations" />">
				</div>
				<button type="submit" class="btn btn-primary">
					<span class="glyphicon glyphicon-plus"></span>
					<spring:message code="general.generate" />
				</button>
			</form>
		</div>
		<br /><br />
				<h4><spring:message code="category.creation" /></h4>
		<div class="row col-md-12">
			<c:forEach var="error" items='${errors}'>
				<div class="row has-error">${error}</div>
			</c:forEach>
		</div>
		<div class="row col-md-12">
			<form name="createCategory" class="form-inline" method="POST"
				action="/virtualeditions/restricted/category/create"
				onsubmit="return validateForm()">
				<div class="form-group">
					<input type="hidden" class="form-control" name="externalId"
						value="${virtualEdition.externalId}" />
				</div>
				<div class="form-group">
					<input type="text" class="form-control" name="name"
						placeholder="<spring:message code="general.name" />">
				</div>
				<button type="submit" class="btn btn-primary">
					<span class="glyphicon glyphicon-plus"></span>
					<spring:message code="general.generate" />
				</button>
			</form>
		</div>					
		<br /> <br /> <br />
		<div class="row col-md-12">
			<div class="row">
				<table class="table table-hover">
					<form class="form-horizontal" role="form" method="POST"
						action="/virtualeditions/restricted/category/merge">
						<div class="form-group">
							<div class="hidden">
								<label> <input type="hidden" name="taxonomyId"
									value="${taxonomy.getExternalId()}">
								</label>
							</div>
						</div>
						<thead>
							<tr>
								<th><spring:message code="general.category" /></th>
								<th><spring:message code="fragments" /></th>
								<th>
									<div class="form-group">
										<button type="submit" class="btn btn-sm btn-primary">
											<spring:message code="general.merge" />
										</button>
									</div>
								</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="category" items='${taxonomy.getCategoriesSet()}'>
								<tr>
									<td><a
										href="${contextPath}/virtualeditions/restricted/category/${category.getExternalId()}">${category.getName()}</a></td>
									<td><c:forEach var="tag"
											items='${category.getSortedTags()}'>
											<a
												href="${contextPath}/virtualeditions/restricted/fraginter/${tag.getInter().getExternalId()}">${tag.getInter().getTitle()}</a> (${tag.getWeight()})</c:forEach></td>
									<td class="col-centered">
										<div class="form-group">
											<div class="checkbox">
												<label> <input type="checkbox" name="categories[]"
													value="${category.getExternalId()}">
												</label>
											</div>
										</div>
									</td>
								</tr>
							</c:forEach>
							<tr>
								<td></td>
								<td></td>
								<td><div class="form-group">
										<button type="submit" class="btn btn-sm btn-primary">
											<spring:message code="general.merge" />
										</button></td>
							</tr>
						</tbody>
					</form>
				</table>
			</div>
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
