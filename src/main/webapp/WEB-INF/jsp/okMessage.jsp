<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Mensagens</title>
		<link rel="stylesheet" type="text/css" href="/static/css/bootstrap.css" />
		<link rel="stylesheet" type="text/css" href="/static/css/style.css" />
		<script type="text/javascript" src="/static/js/bootstrap.js"></script>
	</head>
	<body>
		<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
		<%@ include file="/WEB-INF/jsp/common/header.jsp" %>
		
		<div class="container">
			<div class="hero-unit">
				<h1>Sucesso:</h1>
				<br>
				<div class="alert alert-success">  
  					<a class="close" data-dismiss="alert"></a>  
  					<strong>${message} </strong> 
				</div> 
			</div>
		</div>
	</body>
</html>