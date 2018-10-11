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
				<spring:message code="general.classificationGame" />
			</h3>
			<br>
			
			<div class="row">
				<a href="../classification-game" target="_blank"><spring:message code="general.classificationGame.visitWebsite" />
					<span class="glyphicon glyphicon-open"></span>
				</a>
				<br/>
			</div>
			<br>

			<div class="row">
				Explicar aqui o jogo, o que fazer, quer do lado do cliente quer do lado do servidor...
			</div>

			<br>


		</div>
	</div>
</body>

</html>