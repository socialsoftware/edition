<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>${recipe.name}</title>
		<link rel="stylesheet" type="text/css" href="/static/css/bootstrap.css" />
		<link rel="stylesheet" type="text/css" href="/static/css/style.css" />
		<script type="text/javascript" src="/static/js/bootstrap.js"></script>
	</head>
	<body>
		<%@ include file="/WEB-INF/jsp/common/header.jsp" %>
		
		<div class="container">
			<ul class="breadcrumb">
			  <li><a href="/">Home</a> <span class="divider">/</span></li>
			  <li><a href="/recipes">Receitas</a> <span class="divider">/</span></li>
			  <li class="active">${recipe.name}</li>
			</ul>
			<h1>${recipe.name}</h1>
			<hr/>
			<h3>Problema</h3>
			<p>${recipe.problem}</p>
			<h3>Solução</h3>
			<p>${recipe.solution}</p>
			<a class="btn btn-warning" href="/recipes/${recipe.externalId}/edit"><i class="icon-pencil icon-white"></i> Editar</a>
		</div>
	</body>
</html>