<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<script type="text/javascript">
	function validateForm() {
		$("#errorCat").empty();
		var errors = [];

		var x = document.forms["updateName"]["name"].value;
		if (x == null || x == "") {
			errors
					.push('<spring:message code="validateCreateCategoryForm.value"/>');
		}

		if (errors.length > 0) {
			$("#errorCat").html(errors.join("<br>"));
			return false;
		} else {
			return true;
		}
	}
</script>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

	<c:set var="taxonomy" value="${category.getTaxonomy()}" />
	<c:set var="userLdoD"
		value='${pageContext.request.userPrincipal.principal.getUser()}' />

	<div class="container">
		<h3 class="text-center">
					<a href="${contextPath}/virtualeditions/restricted/${category.getTaxonomy().getEdition().getExternalId()}/taxonomy"><spring:message
			code="general.taxonomy" /></a>: 
			<spring:message code="virtualedition" />
			${taxonomy.getEdition().getTitle()}
		</h3>
		
		
		
		<div class="row">
		
			<!-- 
			<h4 class="pull-right">
				<spring:message code="general.public.pages" />
				- <a
					href="${contextPath}/edition/internalid/${category.getTaxonomy().getEdition().getExternalId()}">
					<spring:message code="general.edition" />
				</a> : <a
					href="${contextPath}/edition/category/${category.getExternalId()}"><spring:message
						code="general.category" /></a>
			</h4>
			 -->
		</div>
		<h3 class="text-center">

			<spring:message code="general.category" />: 
			${category.getName()}
		</h3>
		<br/><br/>
		<c:if test="${taxonomy.canManipulateTaxonomy(userLdoD)}">
			<div class="row">
				<div class="col-md-4">
					<form name="updateName" class="form-inline" method="POST"
						action="/virtualeditions/restricted/category/update"
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
					<div class="row">
						<div class="col-md-9">
							<!-- checked by javascrip -->
							<p class="text-danger" id="errorCat"></p>
						</div>
						<!-- checked in the server -->
						<c:forEach var="categoryError" items='${errors}'>
							<div class="col-md-9 text-danger">
								<spring:message code="${categoryError}" />
							</div>
						</c:forEach>
					</div>

				</div>
				
				<div class="col-md-4" align="center">
				<span class="bg-info" style="padding:8px">
				<spring:message code="general.public.pages" />:
				<a href="${contextPath}/edition/internalid/${category.getTaxonomy().getEdition().getExternalId()}">
				<span class="glyphicon glyphicon-list-alt"></span> <spring:message code="general.edition" /></a>
				-
				<a href="${contextPath}/edition/category/${category.getExternalId()}">
				<span class="glyphicon glyphicon-tag"></span> <spring:message code="general.category" /></a> 
				</span>
				</div>
				
				<div class="col-md-3">
				</div>
				<div class="col-md-1">
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
			</div>
		</c:if>
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
						<td><c:forEach var="inter"
								items='${category.getSortedInters(taxonomy.getEdition())}'>
								<a
									href="${contextPath}/virtualeditions/restricted/fraginter/${inter.getExternalId()}">
									${inter.getTitle()}</a>
								<span style="padding-left: 2em" />
							</c:forEach></td>
					</tr>

				</tbody>
			</table>
		</div>
		<c:if test="${taxonomy.canManipulateTaxonomy(userLdoD)}">
			<div class="row pull-right">
				<button class="btn btn-primary pull-right" data-toggle="modal"
					data-target="#extractModal">
					<span class="glyphicon glyphicon-edit"></span>
					<spring:message code="general.extract" />
				</button>
			</div>
		</c:if>
	</div>
	<!-- Modal HTML -->
	<div id="myModal" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title">
						<spring:message code="general.information" />
					</h4>
				</div>
				<div class="modal-body">
					<h3 id="message" />
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">
						<spring:message code="general.close" />
					</button>
				</div>
			</div>
		</div>
	</div>
	<div id="extractModal" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header text-center">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title">
						<spring:message code="general.extract" />
						<spring:message code="general.category" />
						: ${category.getName()}
					</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form" method="POST"
						action="/virtualeditions/restricted/category/extract">
						<div class="form-group">
							<div class="hidden">
								<label> <input type="hidden" name="categoryId"
									value="${category.getExternalId()}">
								</label>
							</div>
						</div>
						<table class="table table-hover">
							<thead>
								<tr>
									<th><spring:message code="fragments" /></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="inter"
									items='${category.getSortedInters(taxonomy.getEdition())}'>
									<tr>
										<td>${inter.getTitle()}</td>
										<td class="col-centered">
											<div class="form-group">
												<div class="checkbox">
													<label> <input type="checkbox" name="inters[]"
														value="${inter.getExternalId()}">
													</label>
												</div>
											</div>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<div class="form-group text-center">
							<button type="submit" class="btn btn-sm btn-primary">
								<spring:message code="general.extract" />
							</button>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">
						<spring:message code="general.close" />
					</button>
				</div>
			</div>
		</div>
	</div>

</body>
</html>
