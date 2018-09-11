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
				<a id="infovirtualeditions" data-placement="bottom"
					class="infobutton" role="button" data-toggle="popover"
					data-content="<spring:message code="info.virtualeditions.authenticate" />">
					<span class="glyphicon glyphicon-info-sign"></span>
				</a>
			</h3>

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
							<input type="hidden" name="${_csrf.parameterName}"
								value="${_csrf.token}" />
							<div class="form-group">
								<div class="input-group">
									<div class="input-group-addon">LdoD-</div>
									<input type="text" class="form-control tip" name="acronym"
										placeholder="<spring:message code="virtualeditionlist.acronym" />"
										value="${acronym}"
										title="<spring:message code="virtualedition.tt.acronym" />" />
								</div>
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
			<div class="row"></div>
			<br />
			<div class="row">
				<div>
					<table class="table table-hover">
						<thead>
							<tr>
								<th class="text-center"><span class="tip"
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
								<th class="text-center"><span class="tip"
									title="<spring:message code="virtualedition.tt.date" />">
										<spring:message code="general.date" />
								</span></th>
								<th class="text-center"><span class="tip"
									title="<spring:message code="virtualedition.tt.access" />"><spring:message
											code="general.access" /></span></th>
								<th class="text-center"><span class="tip"
									title="<spring:message code="virtualedition.tt.manage" />"><spring:message
											code="general.manage" /></span></th>
								<th class="text-center"><span class="tip"
									title="<spring:message code="virtualedition.tt.join" />"><spring:message
											code="general.join" /></span></th>
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
										<td class="text-center"><c:if test="${!isLdoDEdition}">
												<form class="form-inline" method="POST"
													action="${contextPath}/virtualeditions/toggleselection">
													<input type="hidden" name="${_csrf.parameterName}"
														value="${_csrf.token}" /> <input type="hidden"
														name="externalId" value="${virtualEdition.externalId}" />
													<input type="checkbox" onChange="this.form.submit()"
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
										<td class="text-center">${virtualEdition.getDate().toString("dd-MM-yyyy")}</td>
										<td class="text-center"><c:choose>
												<c:when test="${isPublic}">
													<spring:message code="general.public" />
												</c:when>
												<c:otherwise>
													<spring:message code="general.private" />
												</c:otherwise>
											</c:choose></td>

										<td class="text-center"><c:if test="${isMember}">
												<a
													href="${contextPath}/virtualeditions/restricted/manage/${virtualEdition.externalId}"><span
													class="glyphicon glyphicon-edit"></span> <spring:message
														code="general.manage" /></a>
											</c:if></td>
										<td class="text-center"><c:choose>
												<c:when test="${isMember}">
													<!-- do nothing -->
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
									</tr>
								</c:if>
							</c:forEach>
						</tbody>
					</table>
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
</script>
<script type="text/javascript">
	$('#infovirtualeditions').popover();
</script>

</html>