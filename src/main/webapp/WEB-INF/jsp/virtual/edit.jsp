<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/ldod-header.jsp"%>

	<div class="container">
		<form class="form-horizontal" method="POST"
			action="/virtualeditions/restricted/edit/${externalId}">
			<fieldset>
				<legend>
					<spring:message code="virtualeditionlist.editvirtual" />
				</legend>
				<c:forEach var="error" items='${errors}'>
					<div class="row text-error">
						<spring:message code="${error}" />
					</div>
				</c:forEach>
				<div class="control-group">
					<label class="control-label" for="acronym"><spring:message
							code="virtualeditionlist.acronym" />:</label>
					<div class="controls">
						<input type="text" class="input-small" name="acronym" id="acronym"
							placeholder="<spring:message code="virtualeditionlist.acronym" />"
							value="${acronym}" />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="title"><spring:message
							code="virtualeditionlist.name" />:</label>
					<div class="controls">
						<input type="text" class="input-block-level" name="title"
							id="title"
							placeholder="<spring:message code="virtualeditionlist.name" />"
							value="${title}" />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="date"><spring:message
							code="general.date" />:</label>
					<div class="controls">
						<input type="text" class="input-small uneditable-input"
							name="date" id="date" value="${date}" />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="pub"><spring:message code="general.access" />:</label>
					<div class="controls">
						<select class="selectpicker" name="pub" id="pub">
							<c:choose>
								<c:when test="${pub == false}">
									<option value="true">
										<spring:message code="general.public" />
									</option>
									<option value="false" selected><spring:message code="general.private" /></option>
								</c:when>
								<c:otherwise>
									<option value="true" selected><spring:message code="general.public" /></option>
									<option value="false"><spring:message code="general.private" /></option>
								</c:otherwise>
							</c:choose>
						</select>
					</div>
				</div>
				<div class="control-group">
					<div class="controls">
						<button type="submit" class="btn">
							<i class="icon-edit"></i>
							<spring:message code="general.update" />
						</button>
					</div>
				</div>
			</fieldset>
		</form>
	</div>
</body>
</html>
