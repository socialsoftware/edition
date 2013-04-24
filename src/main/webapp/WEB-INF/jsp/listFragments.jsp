<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/ldoD.tld" prefix="ldod"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Fragmentos do LdoD</title>
<link rel="stylesheet" type="text/css" href="/static/css/bootstrap.css" />
<link rel="stylesheet" type="text/css" href="/static/css/style.css" />
<script type="text/javascript" src="/static/js/jquery.js"></script>
<script type="text/javascript" src="/static/js/bootstrap.js"></script>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$(
								'[id="fragments-details"][data-toggle="buttons-checkbox"]')
								.on(
										'click',
										function() {
											var selDetail = $(
													'input:checkbox[name=detail]')
													.is(':checked');
											$
													.get(
															"${contextPath}/fragments/list",
															{
																detail : selDetail
															},
															function(html) {
																$(
																		"#fragmentList")
																		.replaceWith(
																				html);
															});
										});
					});
</script>
</head>
<body>
	<c:set var="contextPath" value="${pageContext.request.contextPath}" />
	<%@ include file="/WEB-INF/jsp/common/header.jsp"%>

	<div class="container-fluid">
		<h1 class="text-center">Fragmentos do LdoD</h1>
		<div class="well" id="fragments-details"
			data-toggle="buttons-checkbox">
			<label class="checkbox inline"> <input type="checkbox"
				class="btn" name=detail value="Yes"> Mostrar Detalhes
			</label>
		</div>
		
		<%@ include file="/WEB-INF/jsp/listFragmentsSimple.jsp"%>

	</div>
</body>
</html>