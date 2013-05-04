<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="/static/css/bootstrap-responsive.css" rel="stylesheet">
<title>Visualizar Fragmento</title>
<link rel="stylesheet" type="text/css" href="/static/css/bootstrap.css" />
<link rel="stylesheet" type="text/css" href="/static/css/style.css" />
<script type="text/javascript" src="/static/js/jquery.js"></script>
<script type="text/javascript" src="/static/js/bootstrap.js"></script>
<script type="text/javascript">
	$(document).ready(
			function() {
				$('[id="interps"][data-toggle="buttons-radio"]')
						.on(
								'click',
								function() {
									var fragInter = $(
											'input:radio[name=inter]:checked')
											.val();
									if (fragInter == null) alert("Escolha um testemunho base");
									else
									$.get("${contextPath}/fragments/fragment",
											{
												interp : fragInter
											}, function(html) {
												$("#fragmentInterpretation")
														.replaceWith(html);
											});
								});
			});
</script>
<style>
.addBorder {
	border-radius: 5px;
	border: 1px solid black;
	padding: 5px;
}
</style>

<style>
.mycontent-left {
	border-left: 1px solid #333;
	padding: 5px;
}
</style>

</head>
<body>

	<c:set var="contextPath" value="${pageContext.request.contextPath}" />
	<%@ include file="/WEB-INF/jsp/common/header.jsp"%>


	<div class="container-fluid">
		<h3 class="text-center">${fragment.title}</h3>
		<hr>
		<div class="row-fluid">
			<div class="span10">

				<div class="row-fluid">


					<form class="form-horizontal">
						<div class="control-group">
							<span class="control-label"><spring:message code="fragment.base" /></span>
							<div class="controls form-inline">

								<div class="well" id="interps" data-toggle="buttons-radio">
									<c:forEach var="fragInter" items='${fragment.sortedInterps}'>
										<c:if test="${fragInter.sourceType=='EDITORIAL'}">
											<label class="radio inline" for="${fragInter.externalId}">

												<c:choose>
													<c:when test="${fragInter.externalId==inter.externalId}">
														<input type="radio" class="btn" name="inter"
															id="${fragInter.externalId}"
															value="${fragInter.externalId}" checked /> ${fragInter.name} 
													</c:when>
													<c:otherwise>
														<input type="radio" class="btn" name="inter"
															value="${fragInter.externalId}" /> ${fragInter.name}
													</c:otherwise>
												</c:choose>
											</label>
										</c:if>
									</c:forEach>
									<br>
									<c:forEach var="fragInter" items='${fragment.sortedInterps}'>
										<c:if test="${fragInter.sourceType=='AUTHORIAL'}">
											<label class="radio inline" for="${fragInter.externalId}">
												<c:choose>
													<c:when test="${fragInter.externalId==inter.externalId}">
														<input type="radio" class="btn" name="inter"
															id="${fragInter.externalId}"
															value="${fragInter.externalId}" checked /> ${fragInter.name}
								</c:when>
													<c:otherwise>
														<input type="radio" class="btn" name="inter"
															value="${fragInter.externalId}" />
									${fragInter.name}
								</c:otherwise>
												</c:choose>
											</label>
										</c:if>
									</c:forEach>

								</div>
							</div>
						</div>
					</form>

					<%@ include file="/WEB-INF/jsp/fragmentInterpretation.jsp"%>

				</div>
			</div>
			<div class="span2">

				<%@ include file="/WEB-INF/jsp/navegation.jsp"%>



			</div>


		</div>
	</div>
</body>
</html>
