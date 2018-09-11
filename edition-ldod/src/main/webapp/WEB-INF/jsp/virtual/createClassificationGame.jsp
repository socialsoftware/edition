<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
<link rel="stylesheet" type="text/css"
	href="/resources/css/bootstrap-datetimepicker.min.css">
<script type="text/javascript"
	src="/resources/js/bootstrap-datetimepicker.min.js"></script>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

	<c:set var="user"
		value='${pageContext.request.userPrincipal.principal.getUser()}' />
	<c:set var="isAdmin"
		value="${virtualEdition.getAdminSet().contains(user)}" />

	<div class="container">
		<br />
		<div class="row col-md-12">
			<c:forEach var="error" items='${errors}'>
				<div class="row text-error">
					<spring:message code="${error}" />
				</div>
			</c:forEach>
			<c:if test="${isAdmin}">
				<form class="form" method="POST"
					action="${contextPath}/virtualeditions/restricted/${virtualEdition.externalId}/classificationGame/create">
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />
					<div class="form-group">
						<input type="hidden" class="form-control" name="externalId"
							value="${virtualEdition.externalId}" />
					</div>
					<div class="form-group">
						<label class="control-label"><spring:message
								code="general.description" />: </label> <input type="text"
							class="form-control" name="description"
							placeholder="<spring:message code="general.description" />"
							value="${description}" />
					</div>
					<div class="form-group">
						<label class="control-label"><spring:message
								code="general.players" /></label> <select class="form-control"
							name="players" id="players">
							<option value="true" selected>
								<spring:message code="taxonomy.annotation.all" />
							</option>
							<option value="false"><spring:message
									code="taxonomy.annotation.members" /></option>
						</select>
					</div>
					<div class="form-group">
						<label class="control-label"><spring:message
								code="general.date" />: </label>
						<div id="datetimepicker1" class="input-append date">
							<input data-format="dd/MM/yyyy hh:mm" type="text" name="date"></input> <span
								class="glyphicon glyphicon-calendar add-on"></span>
						</div>
					</div>
					<div class="form-group">
						<c:forEach var="inter"
							items='${virtualEdition.getAllDepthVirtualEditionInters()}'>
							<div class="form-check">
								<input class="form-check-input" name="interExternalId" type="radio"
									value="${inter.externalId}"> <label
									class="form-check-label"> ${inter.title} </label>
							</div>
						</c:forEach>
					</div>
					<button type="submit" class="btn btn-primary">
						<span class="glyphicon glyphicon-plus"></span>
						<spring:message code="general.create" />
					</button>
				</form>
			</c:if>
		</div>
	</div>
</body>
<script type="text/javascript">
 $(function() {
 	$('#datetimepicker1').datetimepicker({language: 'pt-BR'});
 });
</script>
</html>