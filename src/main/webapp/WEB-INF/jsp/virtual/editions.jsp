<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

	<c:set var="isAuthenticated"
		value="${pageContext.request.userPrincipal.authenticated}" />

	<div class="container">
		<div class="row">
			<h3 class="text-center">
				<spring:message code="virtual.editions" />
			</h3>

			<c:if test="${isAuthenticated}">
				<div class="row">
					<div class="col-xs-9 col-md-9"></div>
					<div class="col-xs-3 col-md-3" align="right"
						style="margin-top: 20px; margin-bottom: 10px">
						<a class="btn btn-success tipleft"
							title="<spring:message code="virtualedition.tt.create" />"
							role="button" data-toggle="collapse" href="#collapse"
							aria-expanded="false" aria-controls="collapse"> <span
							class="glyphicon glyphicon-plus"></span> <spring:message
								code="virtualeditionlist.createtitle" />
						</a>
					</div>
				</div>
				<br>
				<div class="row">
					<c:forEach var="error" items='${errors}'>
						<div class="text-error alert alert-warning col-md-12" role="alert">
							<spring:message code="${error}" />
						</div>
					</c:forEach>
				</div>
				<div class="row">
					<div class="collapse" id="collapse">
						<div class="well">
							<form class="form-inline" method="POST"
								action="/virtualeditions/restricted/create">
								<div class="form-group">
									<input type="text" class="form-control tip" name="acronym"
										placeholder="<spring:message code="virtualeditionlist.acronym" />"
										value="${acronym}"
										title="<spring:message code="virtualedition.tt.acronym" />" />
								</div>
								<div class="form-group">
									<input type="text" class="form-control tip" name="title"
										placeholder="<spring:message code="virtualeditionlist.name" />"
										value="${title}"
										title="<spring:message code="virtualedition.tt.title" />" />
								</div>
								<div class="form-group">
									<select class="form-control tip" name="pub"
										title="<spring:message code="virtualedition.tt.access" />">
										<c:choose>
											<c:when test="${pub == false}">
												<option value="true">
													<spring:message code="general.public" />
												</option>
												<option value="false" selected>
													<spring:message code="general.private" />
												</option>
											</c:when>
											<c:otherwise>
												<option value="true" selected>
													<spring:message code="general.public" />
												</option>
												<option value="false">
													<spring:message code="general.private" />
												</option>
											</c:otherwise>
										</c:choose>
									</select>
								</div>
								<div class="form-group">
									<select class="form-control tip" name="use"
										title="<spring:message code="virtualedition.tt.use" />"><option
											value="no"><spring:message
												code="tableofcontents.usesEdition" /></option>
										<c:forEach var='expertEdition' items='${expertEditions}'>
											<option value='${expertEdition.getExternalId()}'>${expertEdition.getEditor()}</option>
										</c:forEach>
										<option value='${ldod.getArchiveEdition().getExternalId()}'>Arquivo
											do LdoD</option>
										<c:forEach var='virtualEdition' items='${virtualEditions}'>
											<c:if test="${!virtualEdition.isLdoDEdition()}">
												<option value='${virtualEdition.getExternalId()}'>${virtualEdition.getAcronym()}</option>
											</c:if>
										</c:forEach></select>
								</div>
								<button type="submit" class="btn btn-primary">
									<span class="glyphicon glyphicon-edit"></span>
									<spring:message code="general.create" />
								</button>
							</form>
						</div>
					</div>
				</div>
			</c:if>
			<div class="row"></div>
			<br />
			<div class="row">
				<div>
					<table class="table table-hover">
						<thead>
							<tr>
								<th><span class="tip"
									title="<spring:message code="virtualedition.tt.select" />"><span
										style="padding-bottom: 3px"
										class="glyphicon glyphicon glyphicon-eye-open"></span></span></th>
								<th><span class="tip"
									title="<spring:message code="virtualedition.tt.acronym" />">
										<spring:message code="virtualeditionlist.acronym" />
								</span></th>
								<th><span class="tip"
									title="<spring:message code="virtualedition.tt.title" />">
										<spring:message code="virtualeditionlist.name" />
								</span></th>
								<th><span class="tip"
									title="<spring:message code="virtualedition.tt.date" />">
										<spring:message code="general.date" />
								</span></th>
								<th><span class="tip"
									title="<spring:message code="virtualedition.tt.access" />"><spring:message
											code="general.access" /></span></th>
								<th><span class="tip"
									title="<spring:message code="virtualedition.tt.edit" />"><spring:message
											code="general.edit" /></span></th>
								<th><span class="tip"
									title="<spring:message code="virtualedition.tt.participants" />"><spring:message
											code="general.participants" /></span></th>
								<th><span class="tip"
									title="<spring:message code="virtualedition.tt.taxonomies" />"><spring:message
											code="general.taxonomies" /></span></th>
								<th><span class="tip"
									title="<spring:message code="virtualedition.tt.recommendations" />"><spring:message
											code="general.recommendations" /></span></th>
								<th><span class="tip"
									title="<spring:message code="virtualedition.tt.delete" />"><spring:message
											code="general.delete" /></span></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="virtualEdition" items='${virtualEditions}'>
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
										<td><c:if test="${!isLdoDEdition}">
												<form class="form-inline" method="POST"
													action="${contextPath}/virtualeditions/toggleselection">
													<input type="hidden" name="externalId"
														value="${virtualEdition.externalId}" /> <input
														type="checkbox" onChange="this.form.submit()"
														<c:choose>
													<c:when
														test="${ldoDSession.materializeVirtualEditions().contains(virtualEdition)}">
														checked
													</c:when>
												</c:choose>>
													<!-- 
											<button type="submit" class="btn btn-primary btn-sm">
												<span class="glyphicon glyphicon-check"></span>
												<c:choose>
													<c:when
														test="${ldoDSession.materializeVirtualEditions().contains(virtualEdition)}">
														<spring:message code="general.deselect" />
													</c:when>
													<c:otherwise>
														<spring:message code="general.select" />
													</c:otherwise>
												</c:choose>
											</button>
											 -->
												</form>
											</c:if></td>
										<td>${virtualEdition.acronym}</td>
										<td><a href="/edition/acronym/${virtualEdition.acronym}">${virtualEdition.title}</a></td>
										<td>${virtualEdition.getDate().toString("dd-MM-yyyy")}</td>
										<td><c:choose>
												<c:when test="${isPublic}">
													<spring:message code="general.public" />
												</c:when>
												<c:otherwise>
													<spring:message code="general.private" />
												</c:otherwise>
											</c:choose></td>

										<td><c:if test="${isMember}">
												<a
													href="${contextPath}/virtualeditions/restricted/editForm/${virtualEdition.externalId}"><span
													class="glyphicon glyphicon-edit"></span> <spring:message
														code="general.edit" /></a>
											</c:if></td>
										<td><c:choose>
												<c:when test="${isMember}">
													<a
														href="${contextPath}/virtualeditions/restricted/${virtualEdition.externalId}/participants"><span
														class="glyphicon glyphicon-user"></span> <spring:message
															code="general.participants" /></a>
												</c:when>
												<c:when test="${isAuthenticated && !isPending}">
													<form class="form-inline" method="POST"
														action="${contextPath}/virtualeditions/restricted/${virtualEdition.externalId}/participants/submit">
														<input type="hidden" name="externalId"
															value="${virtualEdition.externalId}" />
														<button type="submit" class="btn btn-primary btn-sm">
															<span class="glyphicon glyphicon-plus"></span>
															<spring:message code="general.submit" />
														</button>
													</form>
												</c:when>
												<c:when test="${isAuthenticated && isPending}">
													<form class="form-inline" method="POST"
														action="${contextPath}/virtualeditions/restricted/${virtualEdition.externalId}/participants/cancel">
														<input type="hidden" name="externalId"
															value="${virtualEdition.externalId}" />
														<button type="submit" class="btn btn-primary btn-sm">
															<span class="glyphicon glyphicon-remove"></span>
															<spring:message code="general.cancel" />
														</button>
													</form>
												</c:when>
											</c:choose></td>
										<td><c:if test="${isMember}">
												<a
													href="${contextPath}/virtualeditions/restricted/${virtualEdition.externalId}/taxonomy"><span
													class="glyphicon glyphicon-tags"></span> <spring:message
														code="general.taxonomy" /></a>
											</c:if></td>
										<td><c:if test="${isMember}">
												<a
													href="${contextPath}/recommendation/restricted/${virtualEdition.externalId}"><span
													class="glyphicon glyphicon-wrench"></span> <spring:message
														code="general.recommendations" /></a>
											</c:if></td>
										<td><c:if test="${isAdmin}">
												<form id="formdelete" class="form-inline" method="POST"
													action="${contextPath}/virtualeditions/restricted/delete">
													<input type="hidden" name="externalId"
														value="${virtualEdition.externalId}" />
													<button type="submit" id="btdelete"
														style="border: none; background: none !important;">
														<span class="glyphicon glyphicon-trash"></span>
													</button>
												</form>
											</c:if></td>
									</tr>
								</c:if>
							</c:forEach>
						</tbody>
					</table>
				</div>
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
</body>
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