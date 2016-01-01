<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<link
	href="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1/css/select2.min.css"
	rel="stylesheet" />
<script
	src="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1/js/select2.min.js"></script>

<div class="row" id="taxonomy">
	<c:set var="inter" value='${inters.get(0)}' />
	<c:set var="taxonomy" value='${inter.getEdition().getTaxonomy()}' />
	<c:set var="userLdoD" value='${pageContext.request.userPrincipal.principal.getUser()}'/>
	<c:set var="authorized"
		value="${pageContext.request.userPrincipal.authenticated && taxonomy.getEdition().getParticipantSet().contains(userLdoD)}" />
	<c:if test="${authorized}">
		<button class="btn btn-primary pull-right" data-toggle="modal"
			data-target="#myModal">
			<span class="glyphicon glyphicon-plus"></span>
		</button>
	</c:if>
	<table class="table table-hover">
		<thead>
			<tr>
				<th><span class="glyphicon glyphicon-tag"></span></th>
				<th><span class="glyphicon glyphicon-user"></span></th>
			</tr>

		</thead>
		<tbody>
			<c:forEach var="category"
				items='${taxonomy.getSortedCategories(inters.get(0))}'>
				<tr>
					<td><a
						href="${contextPath}/edition/category/${category.getExternalId()}">
							${category.getName()}</a> <c:if test="${category.getContributorSet(inter).contains(userLdoD)}">
							<a
								href="${contextPath}/virtualeditions/restricted/fraginter/${inter.getExternalId()}/tag/dissociate/${category.getExternalId()}"><span
								class="glyphicon glyphicon-remove"></span></a>
						</c:if></td>
					<td><c:forEach var="user"
							items='${category.getContributorSet(inter)}'>
							<a href="${contextPath}/edition/user/${user.username}">${user.username}</a>
						</c:forEach></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>

<div class="modal fade" id="myModal" tabindex="-1" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">${inter.getFragment().getTitle()}</h4>
			</div>
			<div class="modal-body">
				<div class="row text-center">
					<form class="form" role="form" method="POST"
						action="/virtualeditions/restricted/tag/associate">
						<div class="form-group">
							<div class="hidden">
								<input type="hidden" name="fragInterId" class="form-control"
									value="${inter.getExternalId()}">
							</div>
						</div>
						<div class="form-group">
							<div class="hidden">
								<input type="hidden" name="taxonomyId" class="form-control"
									value="${taxonomy.getExternalId()}">
							</div>
						</div>
						<div class="form-group">
							<select name="categories[]" id="category-select"
								class="form-control" style="width: 75%" multiple="true">
								<c:forEach var='category'
									items='${inter.getNonAssignedCategories(user)}'>
									<option value='${category.getName()}'>${category.getName()}</option>
								</c:forEach>
								<c:forEach var='category'
									items='${inter.getAssignedCategories(user)}'>
									<option value='${category.getName()}' selected='selected'>${category.getName()}</option>
								</c:forEach>
							</select>
						</div>
						<div class="form-group">
							<button type="submit" class="btn btn-sm btn-primary">
								<spring:message code="general.associate" />
							</button>
						</div>
					</form>
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



<script>
	$("#category-select").select2({
		tags : '${taxonomy.getOpenVocabulary()}' == "true" ? true : false,
		tokenSeparators : [ ',', ' ' ]
	})
</script>

