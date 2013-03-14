<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Recipes</title>
		<link rel="stylesheet" type="text/css" href="/static/css/bootstrap.css" />
		<link rel="stylesheet" type="text/css" href="/static/css/style.css" />
		<script type="text/javascript" src="/static/js/bootstrap.js"></script>
	</head>
	<body>
		<%@ include file="/WEB-INF/jsp/common/header.jsp" %>
	
		<div class="container">
			<div class="hero-unit">
				<h1>Pesquisa</h1>
				<br>
				<p>
					<a class="btn btn-large btn-primary" href="/search/fragments"><i class="icon-list icon-white"></i> Fragmentos</a><br><br>
					<a class="btn btn-large btn-primary" href="/search/sources"><i class="icon-list icon-white"></i> Testemunhos Autorais</a>
					
				</p>
			</div>
		</div>

	</body>
</html>