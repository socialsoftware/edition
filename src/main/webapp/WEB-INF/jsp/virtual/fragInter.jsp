<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

	<div class="container">
	
		<div class="row">
		
		<div class="col-md-12">
		<h3 class="text-center">
			<a
				href="${contextPath}/virtualeditions/restricted/${fragInter.getEdition().getExternalId()}/taxonomy"><spring:message
					code="general.taxonomy" /></a>: <spring:message code="virtualedition" />
			${fragInter.getEdition().getTitle()}
		</h3>
		</div>
		</div>
	
	 	<!--
	 			<div class="row">
			<h4 class="pull-right">
				<spring:message code="general.public.pages" />
				- <a
					href="${contextPath}/edition/internalid/${fragInter.getEdition().getExternalId()}">
					<spring:message code="general.edition" />
				</a> : <a
					href="${contextPath}/fragments/fragment/inter/${fragInter.getExternalId()}">
					<spring:message code="fragment" />
				</a>
			</h4>
		</div>
		  -->
		
	
		<h3 class="text-center">
			<!-- <a
				href="${contextPath}/virtualeditions/restricted/${fragInter.getEdition().getExternalId()}/taxonomy"><spring:message
					code="general.taxonomy" /></a> - -->
			<spring:message code="fragment" />: ${fragInter.getTitle()}

		</h3>
		<br><br>
		<div class="row" align="center">
		<span class="bg-info" style="padding:8px">
		<spring:message code="general.public.pages" />:
		<a href="${contextPath}/edition/internalid/${fragInter.getEdition().getExternalId()}">
		<span class="glyphicon glyphicon-list-alt"></span> <spring:message code="general.edition" /></a>
		-
		<a href="${contextPath}/fragments/fragment/inter/${fragInter.getExternalId()}">
		<span class="glyphicon glyphicon-align-left"></span> <spring:message code="fragment" /></a> 
		</span>
		</div>
		
		
		<br>
		 
		<div class="row">
			<table class="table table-hover">
				<thead>
					<tr>
						<th><spring:message code="general.category" /></th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td><c:forEach var="category"
								items='${fragInter.getSortedCategories(fragInter.getEdition())}'>
								<a
									href="${contextPath}/virtualeditions/restricted/category/${category.getExternalId()}">${category.getName()}
								</a><span style="padding-left:2em"/>
							</c:forEach></td>
					</tr>

				</tbody>
			</table>
		</div>
	</div>
</body>
</html>
