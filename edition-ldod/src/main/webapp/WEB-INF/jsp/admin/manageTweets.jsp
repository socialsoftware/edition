<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

	<div class="container">
		<h3 class="text-center">Manage Tweets</h3>
		<br /> <br />
		<form class="form-inline" 
			method="POST"
			action="${contextPath}/admin/tweets/removeTweets">
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /> 
			<button type="submit" class="btn btn-danger btn-sm">
				<span class="glyphicon glyphicon-remove"></span>
				Delete Tweets
			</button>
		</form>
		<br />
		<br />
		<form class="form-inline" 
			method="POST"
			action="${contextPath}/admin/tweets/removeTweetsWithoutCitation">
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /> 
			<button type="submit" class="btn btn-danger btn-sm">
				<span class="glyphicon glyphicon-remove"></span>
				Delete Tweets without Citation
			</button>
		</form>
	</div>
</body>
</html>

