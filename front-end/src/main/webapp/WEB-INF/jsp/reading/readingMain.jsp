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


		<!-- </div> -->
	</div>

	<br><br>
</body>
<script>
	$(".tip").tooltip({
		placement : 'bottom',

	});
	$('#inforecom').popover({
		container: 'body'
	});
</script>



</html>
