<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Carregar Fragmentos</title>
		<link rel="stylesheet" type="text/css" href="/static/css/bootstrap.css" />
		<link rel="stylesheet" type="text/css" href="/static/css/style.css" />
		<script type="text/javascript" src="/static/js/bootstrap.js"></script>
	</head>
	<body>
		<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
		<%@ include file="/WEB-INF/jsp/common/header.jsp" %>
				
		<div class="container">
				<form method="POST" action="${contextPath}/load/fragments" enctype="multipart/form-data">
						<form:errors path="*" />
				
				    <fieldset>
	    			<legend>Carregar Ficheiro TEI com Fragmentos</legend>	    			
	    			<input type="file" class="input-block-level" name="file" />
	    			<button type="submit" class="btn pull-left">Submeter</button>
	  			    </fieldset>
	  		    </form>
	    </div>
	</body>
</html>