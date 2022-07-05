<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
<script src="/resources/js/spin-v2.3.2.js"></script>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

	<c:set var="taxonomy" value="${virtualEdition.getTaxonomy()}" />
	<c:set var="userLdoD"
		value='${pageContext.request.userPrincipal.principal.getUser()}' />

	<div class="container">
		<br />
		<div class="row col-md-1">
			<form class="form-inline" method="GET"
				action="${contextPath}/virtualeditions/restricted/manage/${virtualEdition.externalId}">
				<button type="submit" class="btn btn-default">
					<span class="glyphicon glyphicon-arrow-left"></span>
					<spring:message code="general.back" />
				</button>
			</form>
		</div>

		<div class="row col-md-12">
			<h3 class="text-center">
				${virtualEdition.title} -
				<spring:message code="general.taxonomy" />
			</h3>
		</div>
		<div class="row">
			<div class="row pull-right" align="center" style="margin-top: 8px">
				<c:if test="${not empty taxonomy.getUsedIn()}">
					<span class="bg-warning" style="padding: 10px; line-height: 20px">
						<spring:message code="general.usedIn" />: <c:forEach
							var='edition' items='${taxonomy.getUsedIn()}'>
							<a
								href="${contextPath}/edition/acronym/${edition.getAcronym()}">${edition.getAcronym()}</a>
						</c:forEach>
					</span>
				</c:if>
				<span>&nbsp</span> <span class="bg-info"
					style="padding: 10px; line-height: 20px"> <spring:message
						code="general.public.pages" />: <a
					href="${contextPath}/edition/acronym/${virtualEdition.getAcronym()}">
						<span class="glyphicon glyphicon glyphicon-list-alt"></span> <spring:message
							code="general.edition" />
				</a></span>
			</div>
		</div>
		<br />
		<c:if test="${taxonomy.canManipulateTaxonomy(userLdoD)}">
			<div class="row">
				<div class="col-md-4">
					<form name="createCategory" class="form-inline" method="POST"
						action="/virtualeditions/restricted/category/create"
						onsubmit="return validateCreateCategoryForm()">
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
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
					<div class="row">
						<div class="col-md-9">
							<!-- checked by javascrip -->
							<p class="text-danger" id="errorCat"></p>
						</div>
						<!-- checked in the server -->
						<c:forEach var="categoryError" items='${categoryErrors}'>
							<div class="col-md-9 text-danger">
								<spring:message code="${categoryError}" />
							</div>
						</c:forEach>
					</div>
				</div>
				<div class="pull-right">
					<button class="btn btn-primary pull-right tipleft"
						data-toggle="modal" data-target="#topicModal"
						title="<spring:message code="taxonomies.tt.create" />">
						<span class="glyphicon glyphicon-plus"></span>
						<spring:message code="topics.generate.short" />
					</button>
				</div>
			</div>
		</c:if>
		<br />
		<div class="row col-md-12">
			<div class="row">
				<form class="form-horizontal" role="form" method="POST"
					action="/virtualeditions/restricted/category/mulop" id="mulopForm">
					<div class="form-group">
						<div class="hidden">
							<input type="hidden" name="${_csrf.parameterName}"
								value="${_csrf.token}" /> 
							<label> <input type="hidden"
								name="taxonomyId" value="${taxonomy.getExternalId()}">
							</label> <label> <input type="hidden" name="type" id="type">
							</label>
						</div>
					</div>
					<table class="table table-hover">
						<thead>
							<tr>
								<th><spring:message code="general.category" /></th>
								<th><span class="tip"
									title="<spring:message code='taxonomies.tt.fragments' />"><spring:message
											code="fragments" /></span></th>
								<c:if test="${taxonomy.canManipulateTaxonomy(userLdoD)}">
									<th class="text-center">
										<div class="dropdown">
											<button class="btn btn-primary dropdown-toggle" type="button"
												id="dropdownMenu" data-toggle="dropdown"
												aria-haspopup="true" aria-expanded="true">
												<spring:message code="general.operation" />
												<span class="caret"></span>
											</button>
											<ul class="dropdown-menu" aria-labelledby="dropdownMenu">
												<li>
													<button class="btn btn-link" role="link"
														onclick="$('#type').val('merge'); submit()">
														<spring:message code="general.merge" />
													</button>
												</li>
												<li>
													<button class="btn btn-link" role="link"
														onclick="$('#type').val('delete'); submit()">
														<spring:message code="general.delete" />
													</button>
												</li>
											</ul>
										</div>
									</th>
								</c:if>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="category"
								items='${taxonomy.getSortedCategories()}'>
								<tr>
									<td><a
										href="${contextPath}/virtualeditions/restricted/category/${category.getExternalId()}">${category.getName()}</a></td>
									<td><c:forEach var="inter"
											items='${category.getSortedInters(virtualEdition)}'>
											<a
												href="${contextPath}/virtualeditions/restricted/fraginter/${inter.getExternalId()}">${inter.getTitle()}</a>
											<br />
										</c:forEach></td>
									<c:if test="${taxonomy.canManipulateTaxonomy(userLdoD)}">
										<td class="text-center">
											<div class="form-group">
												<div class="checkbox text-center">
													<label> <input type="checkbox" name="categories[]"
														value="${category.getExternalId()}">
													</label>
												</div>
											</div>
										</td>
									</c:if>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</form>
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
				</div>
				<div class="modal-body">
					<div class="form-group" id="generationForm">
						<div class="col-md-2">
							<input type="text" class="form-control tip" id="numTopics"
								placeholder="<spring:message code="general.taxonomies.number.topics" />"
								title="<spring:message code="taxonomies.tt.nroftopics" />">
						</div>
						<div class="col-md-2">
							<input type="text" class="form-control tip" id="numWords"
								placeholder="<spring:message code="general.taxonomies.number.words" />"
								title="<spring:message code="taxonomies.tt.nrofwords" />">
						</div>
						<div class="col-md-2">
							<input type="text" class="form-control tip"
								id="thresholdCategories"
								placeholder="<spring:message code="general.taxonomies.threshold.categories" />"
								title="<spring:message code="taxonomies.tt.catcut" />">
						</div>
						<div class="col-md-2">
							<input type="text" class="form-control tip" id="numIterations"
								placeholder="<spring:message code="general.taxonomies.number.iterations" />"
								title="<spring:message code="taxonomies.tt.nrofiterations" />">
						</div>
						<div class="col-md-2">
							<button type="submit" class="btn btn-primary"
								onclick="generate()">
								<span class="glyphicon glyphicon-cog"></span>
								<spring:message code="general.generate" />
							</button>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<p class="text-danger" id="error"></p>
						</div>
					</div>
					<div id="topics"></div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">
						<spring:message code="general.close" />
					</button>
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
	$("#errorCat").empty();
    var errors = [];
    
    var x = document.forms["createCategory"]["name"].value;
   if (x == null || x == "") {
    	errors.push('<spring:message code="validateCreateCategoryForm.value"/>');
    }
    
    if (errors.length > 0) {
   	 	$("#errorCat").html(errors.join("<br>"));
    	return false;
    } else {
    	return true;
    }

}
</script>

<script>
function validateGenerateTopics(topics, words, threshold, iterations) {
	$("#error").empty();
	$("#topics").html("");
	
	var errors = [];
    if (topics == null || topics == "") {
    		errors.push('<spring:message code="validateGenerateTopics.topics.number"/>');
    } else if (isNaN(topics) || topics < 1 || topics > 100) {
    	errors.push('<spring:message code="validateGenerateTopics.topics.positive"/>');
    }
    if (words == null || words == "") {
    		errors.push('<spring:message code="validateGenerateTopics.words.number"/>');
    } else if (isNaN(words) || words < 1 || words > 10) {
    		errors.push('<spring:message code="validateGenerateTopics.words.positive"/>');
    }
    if (threshold == null || threshold == "") {
    		errors.push('<spring:message code="validateGenerateTopics.threshold.value"/>');
    } else if (isNaN(threshold) || threshold < 0 || threshold > 100) {
    		errors.push('<spring:message code="validateGenerateTopics.threshold.range"/>');
    }
    if (iterations == null || iterations == "") {
    		errors.push('<spring:message code="validateGenerateTopics.iterations.value"/>');
    } else if (isNaN(iterations) || iterations < 1) {
    		errors.push('<spring:message code="validateGenerateTopics.iterations.positive"/>');
    }
    
    if (errors.length > 0) {
   	 	$("#error").html(errors.join("<br>"));
    	return false;
    } else {
    	return true;
    }
}

function generate() {
	var topics = $("#numTopics").val();
	var words = $("#numWords").val();
	var threshold = $("#thresholdCategories").val();
	var iterations = $("#numIterations").val();
	
	if (validateGenerateTopics(topics, words, threshold, iterations)) {
		
		var opts = {
				  lines: 11 // The number of lines to draw
				, length: 28 // The length of each line
				, width: 17 // The line thickness
				, radius: 46 // The radius of the inner circle
				, scale: 0.35 // Scales overall size of the spinner
				, corners: 1 // Corner roundness (0..1)
				, color: '#000' // #rgb or #rrggbb or array of colors
				, opacity: 0.25 // Opacity of the lines
				, rotate: 40 // The rotation offset
				, direction: 1 // 1: clockwise, -1: counterclockwise
				, speed: 0.8 // Rounds per second
				, trail: 64 // Afterglow percentage
				, fps: 20 // Frames per second when using setTimeout() as a fallback for CSS
				, zIndex: 2e9 // The z-index (defaults to 2000000000)
				, className: 'spinner' // The CSS class to assign to the spinner
				, top: '45%' // Top position relative to parent
				, left: '85%' // Left position relative to parent
				, shadow: false // Whether to render a shadow
				, hwaccel: false // Whether to use hardware acceleration
				, position: 'absolute' // Element positioning
				};
				var target = document.getElementById('generationForm');
				var spinner = new Spinner(opts).spin(target);
	
	$.get("${contextPath}/virtualeditions/restricted/${virtualEdition.getExternalId()}/taxonomy/generateTopics", 
	{
		numTopics : topics,
		numWords : words,
		thresholdCategories : threshold,
		numIterations : iterations
	}, function(html) {
			spinner.stop();
			
			$("#topics").replaceWith(html)
		}
	)
	}
}

$("#topicModal").on("hidden.bs.modal", function(){
    $("#topics").html("");
});
</script>
<script>
	$(".tipleft").tooltip({
		placement : 'left'
	});
	$(".tip").tooltip({
		placement : 'bottom'
	});

	$('#collapse').on('show.bs.collapse', function() {
		$('.text-error').hide();
	});

	$('#btdelete').on('click', function(e) {
		var $form = $('#formdelete');
		e.preventDefault();
		$('#confirm').modal({
			backdrop : 'static',
			keyboard : false
		}).one('click', '#delete', function(e) {
			$form.trigger('submit');
		});
	});
</script>

</html>

