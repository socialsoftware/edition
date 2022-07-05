<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
<link href="https://afeld.github.io/emoji-css/emoji.css" rel="stylesheet">
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

	<c:set var="isAuthenticated"
		value="${pageContext.request.userPrincipal.authenticated}" />

	<div class="container">
		<input id="acronym" type="hidden" name="externalId"
			value="${virtualEdition.acronym}" />
		<h3 class="text-center">
			${virtualEdition.title}
			<c:if test="${virtualEdition.getAdminSet().contains(user)}">
				<a class="tip" role="button" data-toggle="collapse"
					title="<spring:message code="virtualedition.tt.edit" />"
					href="#collapsemenu" aria-expanded="false"
					aria-controls="collapseExample" style="font-size: 18px"> <span
					class="glyphicon glyphicon-pencil"></span>
				</a>
			</c:if>
		</h3>

		<div class="row col-md-12 has-error">
			<c:forEach var="error" items='${errors}'>
				<div class="text-error alert alert-warning col-md-12" role="alert">
					<spring:message code="${error}" />
				</div>
			</c:forEach>
		</div>
		<div class="row col-md-12">
			<div class="collapse" id="collapsemenu">
				<form class="form-inline" role="form" method="POST" id="formedition"
					action="/virtualeditions/restricted/edit/${externalId}">
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />
					<div class="row col-md-12">
						<div class="form-group pull-right"
							style="padding-right: 0px; padding-left: 0px">

							<label class="sr-only" for=submit><spring:message
									code="general.update" /></label>
							<button type="submit" class="btn btn-primary" id="submit">
								<span class="glyphicon glyphicon-ok"></span>
							</button>

							<button type="button" class="btn btn-default"
								onclick="$('#collapsemenu').collapse('hide')">
								<span class="glyphicon glyphicon-remove"></span>
							</button>

							<input type="hidden" name="fraginters" value="" id="fraginters">
						</div>
					</div>
					<br> <br>
					<div class="well" style="height: 500px">

						<div class="row">
							<div class="form-group col-md-4" style="padding-left: 0px">
								<label class="control-label for="acronym"><spring:message
										code="virtualeditionlist.acronym" /></label>
								<div class="input-group">
									<div class="input-group-addon">LdoD-</div>
									<input type="text" class="form-control tip" name="acronym"
										id="acronym"
										placeholder="<spring:message code="virtualeditionlist.acronym" />"
										value="${virtualEdition.shortAcronym}"
										title="<spring:message code="virtualedition.tt.acronym" />" />
								</div>
							</div>
							<div class="form-group col-md-3" style="padding-left: 0px">
								<label class="control-label" for="title"><spring:message
										code="virtualeditionlist.name" /></label> 
								<input type="text"
									class="form-control tip" name="title" id="title"
									placeholder="<spring:message code="virtualeditionlist.name" />"
									value="${virtualEdition.title}"
									title="<spring:message code="virtualedition.tt.title" />" />
							</div>
							<div class="form-group col-md-3" style="padding-left: 0px">
								<label class="control-label" for="date"><spring:message
										code="general.date" /></label> 
								<input class="form-control tip"
									id="disabledInput" type="text" name="date" id="date"
									value="${virtualEdition.date}" disabled
									title="<spring:message code="virtualedition.tt.date" />" />
							</div>
							<div class="form-group col-md-2" style="padding-left: 0px">
								<label class="control-label" for="pub"><spring:message
										code="general.access" /></label> 
								<select class="form-control tip"
									name="pub" id="pub"
									title="<spring:message code="virtualedition.tt.access" />">
									<c:choose>
										<c:when test="${virtualEdition.pub == false}">
											<option value="true">
												<spring:message code="general.public" />
											</option>
											<option value="false" selected><spring:message
													code="general.private" /></option>
										</c:when>
										<c:otherwise>
											<option value="true" selected><spring:message
													code="general.public" /></option>
											<option value="false"><spring:message
													code="general.private" /></option>
										</c:otherwise>
									</c:choose>
								</select>
							</div>
						</div>
						<br>
						<div class="row">
							<div class="form-group col-md-4" style="padding-left: 0px">
								<label class="control-label for="acronym"><spring:message
										code="taxonomy.manage" /></label> <select class="form-control tip"
									name="management" id="management"
									title="<spring:message code="virtualedition.tt.manage.categories" />">
									<c:choose>
										<c:when
											test="${virtualEdition.taxonomy.getOpenManagement() == true}">
											<option value="true" selected>
												<spring:message code="taxonomy.manage.members" />
											</option>
											<option value="false"><spring:message
													code="taxonomy.manage.owners" /></option>
										</c:when>
										<c:otherwise>
											<option value="true"><spring:message
													code="taxonomy.manage.members" /></option>
											<option value="false" selected><spring:message
													code="taxonomy.manage.owners" /></option>
										</c:otherwise>
									</c:choose>
								</select>
							</div>
							<div class="form-group  col-md-4" style="padding-left: 0px">
								<label class="control-label" for="pub"><spring:message
										code="taxonomy.annotation" /></label> <select
									class="form-control tip" name="annotation" id="annotation"
									title="<spring:message code="virtualedition.tt.manage.annotations" />">
									<c:choose>
										<c:when
											test="${virtualEdition.taxonomy.getOpenAnnotation() == true}">
											<option value="true" selected>
												<spring:message code="taxonomy.annotation.all" />
											</option>
											<option value="false"><spring:message
													code="taxonomy.annotation.members" /></option>
										</c:when>
										<c:otherwise>
											<option value="true"><spring:message
													code="taxonomy.annotation.all" /></option>
											<option value="false" selected><spring:message
													code="taxonomy.annotation.members" /></option>
										</c:otherwise>
									</c:choose>
								</select>
							</div>
							<div class="form-group  col-md-3" style="padding-left: 0px">
								<label class="control-label" for="title"><spring:message
										code="taxonomy.vocabulary" /></label> <select
									class="form-control tip" name="vocabulary" id="vocabulary"
									title="<spring:message code="virtualedition.tt.manage.vocabulary" />">
									<c:choose>
										<c:when
											test="${virtualEdition.taxonomy.getOpenVocabulary() == true}">
											<option value="true" selected>
												<spring:message code="taxonomy.vocabulary.open" />
											</option>
											<option value="false"><spring:message
													code="taxonomy.vocabulary.closed" /></option>
										</c:when>
										<c:otherwise>
											<option value="true"><spring:message
													code="taxonomy.vocabulary.open" /></option>
											<option value="false" selected><spring:message
													code="taxonomy.vocabulary.closed" /></option>
										</c:otherwise>
									</c:choose>
								</select>
							</div>
						</div>
						<br>
						
						<div class="row">
							<div class="form-group  col-md-4" style="padding-left: 0px">
							<label class="control-label" for="mediasource"><spring:message
										code="criteria.mediasource" /></label> <select
									class="form-control tip" name="mediasource" id="mediasource"
									title="<spring:message code="criteria.mediasource.manage" />">
									<c:choose>
										<c:when
											test="${virtualEdition.getMediaSource().getName() == 'Twitter'}">
											<option value="Twitter" selected><spring:message
												code="criteira.mediasource.twitter" /></option>
											<option value="noMediaSource"><spring:message
												code="criteira.mediasource.nomediasource" /></option>
										</c:when>
										<c:otherwise>
											<option value="Twitter"><spring:message
												code="criteira.mediasource.twitter" /></option>
											<option value="noMediaSource" selected><spring:message
												code="criteira.mediasource.nomediasource" /></option>
										</c:otherwise>
									</c:choose>		
								</select>
							</div>
							
							<div class="form-group  col-md-4" style="padding-left: 0px">
							<label class="control-label" for="begindate"><spring:message
										code="criteira.timewindow.begindate" /></label>
										<input type="date" value="${virtualEdition.getTimeWindow().getBeginDate()}" 
										name="begindate" id="begindate" min="2018-01-01" 
										title="<spring:message code="criteria.timewindow.begindate.manage"/>" />
							</div>
							
							<div class="form-group  col-md-4" style="padding-left: 0px">
							<label class="control-label" for="enddate"><spring:message
										code="criteira.timewindow.enddate" /></label>
										<input type="date" value="${virtualEdition.getTimeWindow().getEndDate()}" 
										name="enddate" id="enddate" max="2018-08-30" 
										title="<spring:message code="criteria.timewindow.enddate.manage"/>" />
							</div>
							
						</div>
						
						<br>
						<div class="row">
							<div class="form-group col-md-2" style="padding-left: 0px">
								<label class="control-label" for="geolocation">
									<spring:message code="criteria.geolocation" /></label> 
								<input type="hidden" name='geolocation' value=''> ${country}
								<br>
								
						
								<!-- selects all boxes -->
								<c:choose>
										<c:when test="${virtualEdition.getGeographicLocation().containsEveryCountry()}">
											<input type="checkbox" checked name="select-all" id="select-all" />
												<spring:message code="criteria.geolocation.everycountry" />
										</c:when>
										<c:otherwise>
											<input type="checkbox" name="select-all" id="select-all" />
												<spring:message code="criteria.geolocation.everycountry" />
										</c:otherwise>
								</c:choose>	
							
								<br>

								<c:forEach var='country' items='${countriesList}'>
									<c:choose>
										<c:when test="${virtualEdition.getGeographicLocation().containsCountry(country)}">
											<input type="checkbox" name='geolocation' value='${country}' checked> ${country}
										</c:when>
										<c:otherwise>
											<input type="checkbox" name='geolocation' value='${country}'> ${country}
										</c:otherwise>
									</c:choose>	
									<br>	
								</c:forEach> 
								
							</div>

							<div class="form-group" style="padding-left: 0px">
								<label class="control-label" for="synopsis"><spring:message
										code="virtualedition.synopsis" /></label>
								<textarea class="form-control" name="synopsis" id="synopsis"
									rows="9" cols="110" maxlength="1500">${virtualEdition.synopsis}</textarea>
								<div id="synopsis_feedback"></div>
							</div>
						</div>
						
						<br>
						<br>
						<div class="row">
							<div class="form-group col-md-2" style="padding-left: 0px">
								<label class="control-label" for="frequency"><spring:message
										code="criteria.frequency" /></label> 	
								<input type="number" min="1" value="${virtualEdition.getFrequency().getFrequency()}"
									class="form-control tip" name="frequency" id="frequency"
									placeholder="1"
									title="<spring:message code="criteria.frequency.manage" />" />
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>

		<br /> <br /> <br />

		<div class="row">
			<div>
				<table class="table table-hover">
					<tbody>
						<c:set var="isAdmin"
							value="${virtualEdition.getAdminSet().contains(user)}" />
						<c:set var="isMember"
							value="${virtualEdition.getParticipantSet().contains(user)}" />
						<c:set var="isPending"
							value="${virtualEdition.getPendingSet().contains(user)}" />
						<c:set var="isPublic" value="${virtualEdition.pub}" />
						<c:set var="isLdoDEdition"
							value="${virtualEdition.isLdoDEdition()}" />
						<c:if test="${(isPublic && !isLdoDEdition) || isMember}">
							<tr>
								<td class="text-center"><c:if test="${isMember}">
										<a
											href="${contextPath}/virtualeditions/restricted/${virtualEdition.externalId}/classificationGame"><span
											class="glyphicon glyphicon-play-circle"></span> <spring:message
												code="general.classificationGame" /></a>
									</c:if></td>
								<td class="text-center"><c:if test="${isMember}">
										<a
											href="${contextPath}/virtualeditions/restricted/${virtualEdition.externalId}/taxonomy"><span
											class="glyphicon glyphicon-tags"></span> <spring:message
												code="general.taxonomy" /></a>
									</c:if></td>
								<td class="text-center"><c:if test="${isMember}">
										<a
											href="${contextPath}/virtualeditions/restricted/editForm/${virtualEdition.externalId}"><span
											class="glyphicon glyphicon-edit"></span> <spring:message
												code="general.sort.manual" /></a>
									</c:if></td>
								<td class="text-center"><c:if test="${isMember}">
										<a
											href="${contextPath}/recommendation/restricted/${virtualEdition.externalId}"><span
											class="glyphicon glyphicon-wrench"></span> <spring:message
												code="general.sort.automatic" /></a>
									</c:if></td>
								<td class="text-center"><c:choose>
										<c:when test="${isMember}">
											<a
												href="${contextPath}/virtualeditions/restricted/${virtualEdition.externalId}/participants"><span
												class="glyphicon glyphicon-user"></span> <spring:message
													code="general.editors" /></a>
										</c:when>
										<c:when test="${isAuthenticated && !isPending}">
											<form class="form-inline" method="POST"
												action="${contextPath}/virtualeditions/restricted/${virtualEdition.externalId}/participants/submit">
												<input type="hidden" name="${_csrf.parameterName}"
													value="${_csrf.token}" /> <input type="hidden"
													name="externalId" value="${virtualEdition.externalId}" />
												<button type="submit" class="btn btn-primary btn-sm">
													<span class="glyphicon glyphicon-plus"></span>
													<spring:message code="general.submit" />
												</button>
											</form>
										</c:when>
										<c:when test="${isAuthenticated && isPending}">
											<form class="form-inline" method="POST"
												action="${contextPath}/virtualeditions/restricted/${virtualEdition.externalId}/participants/cancel">
												<input type="hidden" name="${_csrf.parameterName}"
													value="${_csrf.token}" /> <input type="hidden"
													name="externalId" value="${virtualEdition.externalId}" />
												<button type="submit" class="btn btn-primary btn-sm">
													<span class="glyphicon glyphicon-remove"></span>
													<spring:message code="general.cancel" />
												</button>
											</form>
										</c:when>
									</c:choose></td>
								<td class="text-center"><c:if
										test="${isAdmin && !isLdoDEdition}">
										<form id="formdelete" class="form-inline" method="POST"
											action="${contextPath}/virtualeditions/restricted/delete">
											<input type="hidden" name="${_csrf.parameterName}"
												value="${_csrf.token}" /> <input type="hidden"
												name="externalId" value="${virtualEdition.externalId}" />
											<button type="submit" id="btdelete"
												style="border: none; background: none !important;">
												<span class="glyphicon glyphicon-trash"></span>
											</button>
										</form>
									</c:if></td>
							</tr>
						</c:if>
					</tbody>
				</table>
			</div>
			<div id="confirm" class="modal fade">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-body">
							<spring:message code="general.deleteconfirmation" />
						</div>
						<div class="modal-footer">
							<button type="button" data-dismiss="modal"
							class="btn btn-primary" id="delete">
								<spring:message code="general.delete" />
							</button>
							<button type="button" data-dismiss="modal" class="btn">
								<spring:message code="general.cancel" />
							</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
<script>
	$(document).ready(function() {
		var text_max = 1500;
		$('#synopsis').keyup(function() {
			var text_length = $('#synopsis').val().length;
			var text_remaining = text_max - text_length;

			$('#synopsis_feedback').html('faltam ' + text_remaining + ' caracteres');
		});
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
<script>
$(function(){
    var requiredCheckboxes = $('.options :checkbox[required]');
    requiredCheckboxes.change(function(){
        if(requiredCheckboxes.is(':checked')) {
            requiredCheckboxes.removeAttr('required');
        } else {
            requiredCheckboxes.attr('required', 'required');
        }
    });
});
</script>
<script>
//Listen for click on toggle checkbox
$('#select-all').click(function(event) {   
    if(this.checked) {
        // Iterate each checkbox
        $(':checkbox').each(function() {
            this.checked = true;                        
        });
    } else {
        $(':checkbox').each(function() {
            this.checked = false;                       
        });
    }
});
</script>

<script>
function uncheckAll(){
   $('input[type="checkbox"]:checked').prop('checked',false);
}
</script>

</html>