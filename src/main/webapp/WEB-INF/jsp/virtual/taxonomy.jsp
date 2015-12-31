<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

	<c:set var="taxonomy" value="${virtualEdition.getTaxonomy()}" />

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
				<spring:message code="general.edition" />
			</a> : <a
				href="${contextPath}/edition/taxonomy/${virtualEdition.getTaxonomy().getExternalId()}"><spring:message
					code="general.taxonomy" /></a>
		</h4>
		<br /> <br /> <br />
		<div class="row">
			<div class="col-md-5">
				<c:forEach var="categoryError" items='${categoryErrors}'>
					<div class="row has-error">${categoryError}</div>
				</c:forEach>
				<form name="createCategory" class="form-inline" method="POST"
					action="/virtualeditions/restricted/category/create"
					onsubmit="return validateCreateCategoryForm()">
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
						<spring:message code="category.add" />
					</button>
				</form>
			</div>
			<div class="col-md-6">
				<button class="btn btn-primary pull-right" data-toggle="modal"
					data-target="#topicModal">
					<span class="glyphicon glyphicon-plus"></span>
					<spring:message code="topics.generate.short" />
				</button>
			</div>

		</div>
		<br />
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
								<td>
									<button type="submit" class="btn btn-sm btn-primary">
										<spring:message code="general.merge" />
									</button>
								</td>
							</tr>
						</tbody>
					</form>
				</table>
			</div>
		</div>
	</div>
	<!-- Validate Modal HTML -->
	<div id="validateModal" class="modal fade">
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
	<!-- Topic Model Generation Modal HTML -->
	<div class="modal fade" id="topicModal" tabindex="-1" role="dialog">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title text-center">
						<spring:message code="topics.generate.long" />
					</h4>
					<br />
					<div class="row">
						<div class="col-xs-2">
							<input type="text" class="form-control" id="numTopics"
								placeholder="<spring:message code="general.taxonomies.number.topics" />">
						</div>
						<div class="col-xs-2">
							<input type="text" class="form-control" id="numWords"
								placeholder="<spring:message code="general.taxonomies.number.words" />">
						</div>
						<div class="col-xs-2">
							<input type="text" class="form-control" id="thresholdCategories"
								placeholder="<spring:message code="general.taxonomies.threshold.categories" />">
						</div>
						<div class="col-xs-2">
							<input type="text" class="form-control" id="numIterations"
								placeholder="<spring:message code="general.taxonomies.number.iterations" />">
						</div>

						<button type="submit" class="btn btn-primary" onclick="generate()">
							<span class="glyphicon glyphicon-cog"></span>
							<spring:message code="general.generate" />
						</button>
					</div>
					<div class="row">
						<div class="col-md-6">
							<p class="text-danger" id="error"></p>
						</div>
					</div>
				</div>
				<div class="modal-body">
					<div id="topics">
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
</body>

<script>
function validateCreateCategoryForm() {
    var x = document.forms["createCategory"]["name"].value;
    if (x == null || x == "") {
        $("#message").html("Deve ser indicado o nome da categoria");
        $("#validateModal").modal('show');
        return false;
    }
}
</script>

<script type="text/javascript">
function validateGenerateTopics(topics, words, threshold, iterations) {
	$("#error").empty();
	
	var errors = [];
    if (topics == null || topics == "") {
    	errors.push("Deve ser indicado o número de tópicos a gerar");
    } else if (isNaN(topics) || topics < 1) {
    	errors.push("Deve ser indicado um número de tópicos positivo");
    }
    if (words == null || words == "") {
    	errors.push("Deve ser indicado o número de palavras a apresentar");
    } else if (isNaN(words) || words < 1) {
    	errors.push("Deve ser indicado um número de palavras positivo");
    }
    if (threshold == null || threshold == "") {
    	errors.push("Deve ser indicado o valor de corte de categorias");
    } else if (isNaN(threshold) || threshold < 0 || threshold > 100) {
    	errors.push("Deve ser indicado um valor de corte de categorias entre 0 e 100");
    }
    if (iterations == null || iterations == "") {
    	errors.push("Deve ser indicado o número de iterações");
    } else if (isNaN(iterations) || iterations < 1) {
    	errors.push("Deve ser indicado um número de iterações positivo");
    }
    
    if (errors.length > 0) {
   	 	$("#error").html(errors.join("<br>"));
    	return false;
    } else {
    	return true;
    }
}

function validateCreateCategoryForm() {
    var x = document.forms["createCategory"]["name"].value;
    if (x == null || x == "") {
        $("#message").html("Deve ser indicado o nome da categoria");
        $("#validateModal").modal('show');
        return false;
    }
}
</script>


<script>
function generate() {
	var topics = $("#numTopics").val();
	var words = $("#numWords").val();
	var threshold = $("#thresholdCategories").val();
	var iterations = $("#numIterations").val();
	
	if (validateGenerateTopics(topics, words, threshold, iterations)) {
	
	$.get("${contextPath}/virtualeditions/restricted/${virtualEdition.getExternalId()}/taxonomy/generateTopics", 
	{
		numTopics : topics,
		numWords : words,
		thresholdCategories : threshold,
		numIterations : iterations
	}, function(html) {
			$("#topics").replaceWith(html)
		}
	)
	}
}

$("#topicModal").on("hidden.bs.modal", function(){
    $("#topics").html("");
});
</script>


</html>

