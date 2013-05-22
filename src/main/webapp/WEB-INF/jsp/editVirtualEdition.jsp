<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="virtualeditionlist.editvirtual" /></title>
<link rel="stylesheet" type="text/css" href="/static/css/bootstrap.css" />
<link rel="stylesheet" type="text/css" href="/static/css/style.css" />
<script type="text/javascript" src="/static/js/jquery.js"></script>
<script type="text/javascript" src="/static/js/bootstrap.js"></script>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/header.jsp"%>

	<div class="container">
		<form class="form-horizontal" method="POST" action="/virtualeditions/edit/${externalId}">
			<fieldset>
				<legend><spring:message code="virtualeditionlist.editvirtual" /></legend>
				<c:forEach var="error" items='${errors}'>
					<div class="row text-error"><spring:message code="${error}" /></div>
				</c:forEach>
				<div class="control-group">
					<label class="control-label" for="acronym"><spring:message code="virtualeditionlist.acronym" />:</label>
					<div class="controls">
						<input type="text" class="input-small" name="acronym" id="acronym" 
							placeholder="<spring:message code="virtualeditionlist.acronym" />" value="${acronym}" />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="name"><spring:message code="virtualeditionlist.name" />:</label>
					<div class="controls">
						<input type="text" class="input-block-level" name="name" id="name" 
							placeholder="<spring:message code="virtualeditionlist.name" />" value="${name}" />
					</div>
				</div>
				<div class="control-group">
					<div class="controls">
						<button type="submit" class="btn"><spring:message code="general.update" /></button>
					</div>
				</div>
			</fieldset>
		</form>
	</div>
</body>
</html>
