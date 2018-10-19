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
		<form class="form-inline" method="POST"
			action="${contextPath}/admin/tweets/removeTweets">
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
			<button type="submit" class="btn btn-danger btn-sm">
				<span class="glyphicon glyphicon-remove"></span> Delete Tweets
				(${tweets.size()})
			</button>
		</form>
		<br /> <br />
		<form class="form-inline" method="POST"
			action="${contextPath}/admin/tweets/removeTweetsWithoutCitation">
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
			<button type="submit" class="btn btn-danger btn-sm">
				<span class="glyphicon glyphicon-remove"></span> Delete Tweets
				without Citation (${tweetsWithoutCitation.size()})
			</button>
		</form>
		<br /> <br />
		<p>Citations: ${citations.size()}, Citations with info range ${numberOfCitationsWithInfoRange}</p>
		<table class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<td><strong>Date</strong></td>
					<td><strong>Fragment</strong></td>
					<td><strong>Source Link</strong></td>
					<td><strong>Info Ranges</strong></td>
					<td><strong>Number of Annotations</strong></td>
					<td><strong>Number of Retweets</strong></td>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="citation" items='${citations}'>
					<tr>
						<td>${citation.getDate()}</td>
						<td><a href="${contextPath}/fragments/fragment/${citation.getFragment().xmlId}">${citation.getFragment().getTitle()}</a></td>
						<td><a href="${citation.getSourceLink()}" target="_blank">Tweet</a></td>
						<td>
							<c:forEach var="range" items='${citation.getInfoRangeSet()}'>
								<b>Text:</b> ${range.getText()}, <b>Quote:</b> ${range.getQuote()} <br />
							</c:forEach>
						</td>
						<td>${citation.getAwareAnnotationSet().size()}</td>
						<td>${citation.getNumberOfRetweets()}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</body>
</html>

