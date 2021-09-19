<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
<link rel="stylesheet" type="text/css"
	href="/resources/css/bootstrap-social.css" />
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>
	
<!-- 	<div class="container text-center">
	<h2 class="text-danger">ATENÇÃO: EM MIGRAÇÃO PARA UM NOVO SERVIDOR<br>(LOGIN TEMPORARIAMENTE DESATIVADO)</h2>
	</div>
 -->	

	<div class="container text-center">
		<c:if test="${not empty param['param.error']}">
			<div class="row text-error">
				<spring:message code="login.error" />
			</div>
		</c:if>
		<c:if test="${message != null && message.trim().length() != 0}">
			<div class="row text-error">
				<spring:message code="${message}" arguments="${argument}" />
			</div>
		</c:if>

		<div class="row">
			<div class="login-form">
				<h2>
					<spring:message code="header.title" />
				</h2>
				<form class="form-horizontal" role="form" method="POST"
					action="/signin/authenticate">
					<input type="hidden" name="_csrf" value="${_csrf.token}" />
					<div class="form-group">
						<div class="col-md-offset-4 col-md-4">
							<input type="text" class="form-control" id="username_or_email"
								name="username"
								placeholder="<spring:message code="login.username" />">
						</div>
						<br> <br>
						<div class="col-md-offset-4 col-md-4">
							<input type="password" class="form-control" id="password"
								name="password"
								placeholder="<spring:message code="login.password" />">
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-offset-5 col-md-2">
							<button class="btn btn-primary form-control" type="submit">
								<spring:message code="general.signin" />
							</button>
						</div>
					</div>
				</form>
			</div>

			<!-- TWITTER SIGNIN -->
			<div class="row">
				<form id="tw_signin" action="/signin/twitter" method="POST">
					<input type="hidden" name="_csrf" value="${_csrf.token}"></input>
					<div class="col-md-offset-5 col-md-2">
						<button class="btn btn-block btn-social btn-twitter" type="submit">
							<span class="fa fa-twitter"></span>Twitter
						</button>
					</div>
				</form>
			</div>
			<br>

			<!-- GOOGLE SIGNIN -->
			<div class="row">
				<form id="gg_signin" action="/signin/google" method="POST">
					<input type="hidden" name="_csrf" value="${_csrf.token}"></input>
					<div class="col-md-offset-5 col-md-2">
						<button class="btn btn-block btn-social btn-google-plus"
							type="submit">
							<span class="fa fa-google"></span>Google
						</button>
					</div>
				</form>
			</div>
			<br>
			
			<!-- FACEBOOK SIGNIN -->
			<div class="row">
				<form id="fb_signin" action="/signin/facebook" method="POST">
					<input type="hidden" name="_csrf" value="${_csrf.token}"></input> <input
						type="hidden" name="scope" value="public_profile"></input>
					<div class="col-md-offset-5 col-md-2">
						<button class="btn btn-block btn-social btn-facebook"
							type="submit">
							<span class="fa fa-facebook"></span>Facebook
						</button>
					</div>
				</form>
			</div>
			<br>
			
			<!-- LINKEDIN SIGNIN -->
			<div class="row">
				<form id="li_signin" action="/signin/linkedin" method="POST">
					<input type="hidden" name="_csrf" value="${_csrf.token}"></input>
					<div class="col-md-offset-5 col-md-2">
						<button class="btn btn-block btn-social btn-linkedin"
							type="submit">
							<span class="fa fa-linkedin"></span>Linkedin
						</button>
					</div>
				</form>
			</div>
			<br>
			
			<!-- SIGNUP -->
			<div class="row">
				<a href="/signup"><spring:message code="signup.message" /></a>
			</div>

		</div>
	</div>

	<!-- /container -->

</body>

</html>