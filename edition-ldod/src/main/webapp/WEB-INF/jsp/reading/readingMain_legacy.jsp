<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body class="ldod-default">
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>
	<div class="container">
		<!-- <div class="jumbotron"> -->

			<div class="row">
			<%@ include file="/WEB-INF/jsp/reading/readingGrid.jsp"%>
			</div>

			
			<br><br><br><br><br><br>
			<h2 class="text-center"><spring:message code="general.reading"/></h2>
			<div class="row col-md-offset-2">
				<%@ include file="/WEB-INF/jsp/reading/readingNavigation.jsp" %>
			</div>
			<br>
			<div class="row">
				<%@ include file="/WEB-INF/jsp/reading/readingText.jsp"%>
			</div>
			

		<!-- </div> -->
	</div>
</body>
<script>
	$(".tip").tooltip({
		placement : 'bottom'
	});
</script>
</html>
