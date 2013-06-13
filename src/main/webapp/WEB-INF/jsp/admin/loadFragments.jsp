<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/ldod-header.jsp"%>

	<div class="container">
		<form method="POST" action="${contextPath}/manager/load/fragments"
			enctype="multipart/form-data">
			<form:errors path="*" />

			<fieldset>
				<legend><spring:message code="loadfragment.title"/></legend>
				<input type="file" class="input-block-level" name="file" />
				<button type="submit" class="btn pull-left"><spring:message code="general.submit"/></button>
			</fieldset>
		</form>
	</div>
</body>
</html>