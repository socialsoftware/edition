<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="loadcorpus.header" /></title>
<link rel="stylesheet" type="text/css" href="/static/css/bootstrap.css" />
<link rel="stylesheet" type="text/css" href="/static/css/style.css" />
<script type="text/javascript" src="/static/js/jquery.js"></script>
<script type="text/javascript" src="/static/js/bootstrap.js"></script>
</head>
<body>
	<c:set var="contextPath" value="${pageContext.request.contextPath}" />
	<%@ include file="/WEB-INF/jsp/common/header.jsp"%>

	<security:authorize access="!isAuthenticated()">

		<div class="container">
			<div class="content span12 pagination-centered">
				<c:if test="${loginFailed}">
					<div class="row text-error">
						<spring:message code="login.error" />
					</div>
				</c:if>

				<div class="row">
					<div class="login-form">
						<h2>
							<spring:message code="header.title" />
						</h2>
						<form method="POST" action="j_spring_security_check">
							<fieldset>
								<div class="clearfix">
									<input type="text" id="username_or_email" name="j_username" placeholder="<spring:message code="login.username" />">
								</div>
								<div class="clearfix">
									<input type="password" id="password" name="j_password" placeholder="<spring:message code="login.password" />">
								</div>
								<button class="btn primary" type="submit">
									<spring:message code="general.signin" />
								</button>
							</fieldset>
						</form>
					</div>
				</div>
			</div>

		</div>
		<!-- /container -->

	</security:authorize>

</body>
</html>