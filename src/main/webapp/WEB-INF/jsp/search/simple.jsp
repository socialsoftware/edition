<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>
	<div class='container'>
		<h1 class="text-center"><spring:message code="header.search.simple" /></h1>
		
		
		<form>
		<input></input>
		<button><spring:message code="search"/></button>
		</form>
	</div>
</body>
</html>