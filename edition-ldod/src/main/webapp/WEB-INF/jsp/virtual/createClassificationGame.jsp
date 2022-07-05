<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
	<link rel="stylesheet" href="/webjars/bootstrap/3.3.7-1/css/bootstrap.min.css" />
	<link rel="stylesheet" type="text/css" href="/resources/css/bootstrap-datetimepicker.min.css">
	<script src="/resources/js/moment-with-locales.js"></script>
	<script src="/webjars/jquery/1.11.3/jquery.min.js"></script>
	<script src="/webjars/bootstrap/3.3.7-1/js/bootstrap.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.37/js/bootstrap-datetimepicker.min.js"></script>

</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

	<c:set var="user"
		value='${pageContext.request.userPrincipal.principal.getUser()}' />
	<c:set var="isAdmin"
		value="${virtualEdition.getAdminSet().contains(user)}" />
	<c:set var="locale"
		value="${pageContext.response.locale.getLanguage()}"/>

	<div class="container">
		<br />
		<div class="row col-md-1">
			<form class="form-inline" method="GET"
				  action="${contextPath}/virtualeditions/restricted/${virtualEdition.externalId}/classificationGame">
				<button type="submit" class="btn btn-default">
					<span class="glyphicon glyphicon-arrow-left"></span>
					<spring:message code="general.back" />
				</button>
			</form>
		</div>
		<br /> <br />
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
					<div class="form-group form-group-sm">
						<label class="control-label"><spring:message
								code="general.players" />: </label> 
							<c:choose>
        						<c:when test="${virtualEdition.getTaxonomy().getOpenAnnotation()}">
        							<em><spring:message code="taxonomy.annotation.all" /></em>
        						</c:when>
        						<c:otherwise>
        							<em><spring:message code="taxonomy.annotation.members" /></em>
        						</c:otherwise>
    						</c:choose>
					</div>
					<div class="form-group">
						<label class="control-label"><spring:message
								code="general.date" />: </label>
						<div class='input-group date' id='datetimepicker1'>
							<input type='text' class="form-control" name="date" />
							<span class="input-group-addon">
                        		<span class="glyphicon glyphicon-calendar"></span>
                    		</span>
						</div>
					</div>
					<button type="submit" class="btn btn-primary">
						<span class="glyphicon glyphicon-plus"></span>
						<spring:message code="general.create" />
					</button>
					<div class="form-group form-group-sm">
						<c:forEach var="inter"
							items='${virtualEdition.getAllDepthVirtualEditionInters()}'>
							<div class="col-sm-3">
								<label class="radio-inline">
								<input class="form-check-label" name="interExternalId" type="radio"
									value="${inter.externalId}">
									 ${inter.title} </label>
							</div>
						</c:forEach>
					</div>
				</form>
			</c:if>
		</div>
	</div>
</body>
<script type="text/javascript">
    $(function() {
        $('#datetimepicker1').datetimepicker({
            minDate: moment().add(2, 'm'),
            format: 'DD/MM/YYYY HH:mm',
            locale: "${locale}",
        });
    });
</script>
</html>