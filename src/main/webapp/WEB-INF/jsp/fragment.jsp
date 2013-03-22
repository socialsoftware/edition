<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Visualizar Fragmento</title>
		<link rel="stylesheet" type="text/css" href="/static/css/bootstrap.css" />
		<link rel="stylesheet" type="text/css" href="/static/css/style.css" />
		<script type="text/javascript" src="/static/js/bootstrap.js"></script>
	</head>
	<body>
		<%@ include file="/WEB-INF/jsp/common/header.jsp" %>
		
		<div class="container">
			<h1>${fragment.title}</h1>
				<c:forEach var="fragInter" items='${fragment.fragmentInter}'>
				
				<a href="/fragments/${fragment.externalId}/${fragInter.externalId}"> ${fragInter.name}</a><br></br>
					</c:forEach>
				
				<br>	</br>
				<p>${transcription}</p>
		</div>
	</body>
</html>