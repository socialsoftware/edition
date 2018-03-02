<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>


	<div class="container text-center">

		<div class="row">
			<h3>
				<spring:message code="signup" />
			</h3>
		</div>

		<div class="row">
			<c:if test="${message != null && message.trim().length()!=0}">
				<spring:message code="${message}" arguments="${account}" />
			</c:if>
			<br> <br>

			<form:form class="form-horizontal" id="signup" action="/signup"
				method="post" modelAttribute="signupForm" commandNme="signupForm">

				<input type="hidden" name="_csrf" value="${_csrf.token}" />
				<form:input type="hidden" path="socialMediaService"
					value="${signupForm.socialMediaService}" />
				<form:input type="hidden" path="socialMediaId"
					value="${signupForm.socialMediaId}" />

				<div class="form-group row">
					<label class="col-sm-4 control-label"><spring:message
							code="user.firstName" /></label>
					<div class="col-sm-4">
						<form:input class="form-control" path="firstName" />
					</div>
					<div class="col-sm-2">
						<form:errors class="has-error" path="firstName" />
					</div>
				</div>
				<div class="form-group row">
					<label class="col-sm-4 control-label"><spring:message
							code="user.lastName" /></label>
					<div class="col-sm-4">
						<form:input class="form-control" path="lastName" />
					</div>
					<div class="col-sm-2">
						<form:errors class="has-error" path="lastName" />
					</div>
				</div>
				<div class="form-group row">
					<label class="col-sm-4 control-label"><spring:message
							code="login.username" /></label>
					<div class="col-sm-4">
						<form:input class="form-control" path="username" />
					</div>
					<div class="col-sm-2">
						<form:errors class="has-error" path="username" />
					</div>
				</div>
				<div class="form-group row">
					<label class="col-sm-4 control-label"><spring:message
							code="login.password" /></label>
					<div class="col-sm-4">
						<form:password class="form-control" path="password" />
					</div>
					<div class="col-sm-2">
						<form:errors class="has-error" path="password" />
					</div>
				</div>
				<div class="form-group row">
					<label class="col-sm-4 control-label"><spring:message
							code="user.email" /></label>
					<div class="col-sm-4">
						<form:input class="form-control" path="email" />
					</div>
					<div class="col-sm-2">
						<form:errors class="has-error" path="email" />
					</div>
				</div>
				<div class="form-group row">
					<label class="col-sm-4 control-label"><spring:message
							code="header.conduct" /></label>
					<div class="col-sm-4">
						<label class="form-check-label"><form:checkbox
								class="form-check-input" path="conduct" /> <spring:message
								code="header.conduct.accept" /></label>
					</div>
					<div class="col-sm-2">
						<form:errors class="has-error" path="conduct" />
					</div>
				</div>

				<div class="col-md-8 col-md-offset-2 text-left row">
					<c:choose>
						<c:when
							test='${pageContext.response.locale.getLanguage().equals("en")}'>
							<%@ include file="/WEB-INF/jsp/about/conduct-en.jsp"%>
						</c:when>
						<c:when
							test='${pageContext.response.locale.getLanguage().equals("es")}'>
							<%@ include file="/WEB-INF/jsp/about/conduct-es.jsp"%>
						</c:when>
						<c:otherwise><%@ include
								file="/WEB-INF/jsp/about/conduct-pt.jsp"%></c:otherwise>
					</c:choose>
				</div>

				<div class="form-group row">
					<div class="col-sm-12">
						<button type="submit" class="btn btn-primary">
							<spring:message code="signup" />
						</button>
					</div>
				</div>
			</form:form>
		</div>

	</div>
</body>
</html>