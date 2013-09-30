<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

	<div class="container">
		<div class="hero-unit">
			<h1>
				<spring:message code="exceptions.error" />
			</h1>
			<br>
			<div class="alert alert-error">
				<a class="close" data-dismiss="alert"></a> <strong><c:choose>
						<c:when test="${i18n}"><spring:message code="${message}" /></c:when>
						<c:otherwise>${message}</c:otherwise>
					</c:choose> </strong>
			</div>
		</div>
	</div>

</body>
</html>