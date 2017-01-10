<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>

</head>

<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

	<div class="container">
		<div class="col-md-8 col-md-offset-2 text-justify">
			<h1 class="text-center">
				<spring:message code="header.faq" />
			</h1>
			<c:choose>
				<c:when
					test='${pageContext.response.locale.getLanguage().equals("en")}'>
					<%@ include file="/WEB-INF/jsp/about/faq-en.jsp"%>
				</c:when>
				<c:when
					test='${pageContext.response.locale.getLanguage().equals("es")}'>
					<%@ include file="/WEB-INF/jsp/about/faq-es.jsp"%>
				</c:when>
				<c:otherwise><%@ include file="/WEB-INF/jsp/about/faq-pt.jsp"%></c:otherwise>
			</c:choose>
		</div>
	</div>
</body>
</html>