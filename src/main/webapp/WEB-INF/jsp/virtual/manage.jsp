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
		<input id="acronym" type="hidden" name="externalId"
			value="${virtualEdition.acronym}" />
		<h3 class="text-center">
			${virtualEdition.title}
			<c:if test="${virtualEdition.getAdminSet().contains(user)}">
				<a class="" role="button" data-toggle="collapse"
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
					<div class="well" style="height: 120px">

						<div class="row">
							<div class="form-group col-md-4" style="padding-left: 0px">
								<label class="control-label for="acronym"><spring:message
										code="virtualeditionlist.acronym" /></label>
								<div class="input-group">
									<div class="input-group-addon">LdoD-</div>
									<input type="text" class="form-control" name="acronym"
										id="acronym"
										placeholder="<spring:message code="virtualeditionlist.acronym" />"
										value="${virtualEdition.shortAcronym}" />
								</div>
							</div>
							<div class="form-group col-md-3" style="padding-left: 0px">
								<label class="control-label" for="title"><spring:message
										code="virtualeditionlist.name" /></label> <input type="text"
									class="form-control" name="title" id="title"
									placeholder="<spring:message code="virtualeditionlist.name" />"
									value="${virtualEdition.title}" />
							</div>
							<div class="form-group col-md-3" style="padding-left: 0px">
								<label class="control-label" for="date"><spring:message
										code="general.date" /></label> <input class="form-control"
									id="disabledInput" type="text" name="date" id="date"
									value="${virtualEdition.date}" disabled />
							</div>
							<div class="form-group col-md-2" style="padding-left: 0px">
								<label class="control-label" for="pub"><spring:message
										code="general.access" /></label> <select class="form-control"
									name="pub" id="pub">
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
										code="taxonomy.manage" /></label> <select class="form-control"
									name="management" id="management">
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
										code="taxonomy.annotation" /></label> <select class="form-control"
									name="annotation" id="annotation">
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
										code="taxonomy.vocabulary" /></label> <select class="form-control"
									name="vocabulary" id="vocabulary">
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
					</div>
				</form>
			</div>
		</div>

<br/>
<br/>
<br/>

		<div class="row">
			<div>
				<table class="table table-hover">
<%-- 					<thead>
						<tr>
							<th class="text-center"><span class="tip"
								title="<spring:message code="virtualedition.tt.taxonomies" />"><spring:message
										code="general.taxonomies" /></span></th>
							<th class="text-center"><span class="tip"
								title="<spring:message code="virtualedition.tt.edit" />"><spring:message
										code="general.edit" /></span></th>
							<th class="text-center"><span class="tip"
								title="<spring:message code="virtualedition.tt.recommendations" />"><spring:message
										code="general.recommendations" /></span></th>
							<th class="text-center"><span class="tip"
								title="<spring:message code="virtualedition.tt.participants" />"><spring:message
										code="general.participants" /></span></th>
							<th class="text-center"><span class="tip"
								title="<spring:message code="virtualedition.tt.delete" />"><spring:message
										code="general.delete" /></span></th>
						</tr>
					</thead>
 --%>					<tbody>
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
								<td class="text-center"><c:if
										test="${isAdmin && !isLdoDEdition}">
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
					</tbody>
				</table>
			</div>
		</div>

	</div>

</body>