<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
<link rel="stylesheet" type="text/css"
	href="/resources/css/bootstrap-table.min.css">
<script src="/resources/js/bootstrap-table.min.js"></script>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

	<div class="container">
		<h3 class="text-center">
			<spring:message code="authorial.source" />
			(${sources.size()}) <a id="infosources" class="infobutton"
				data-placement="bottom" role="button" data-toggle="popover"
				data-content="<spring:message code="sourcelist.tt.sources" />">
				<span class="glyphicon glyphicon-info-sign"></span>
			</a>
		</h3>

		<table class="table table-striped table-bordered"
			id="tablelistsources" data-pagination=false style="display: none;">
			<!--  <table class="table table-bordered table-condensed">-->
			<thead>
				<tr>
					<th><span class="tip"
						title="<spring:message code="header.documents" />"><spring:message
								code="header.documents" /></span></th>
					<th><span class="tip"
						title="<spring:message code="sourcelist.tt.transcription" />"><spring:message
								code="general.transcription" /></span></th>
					<th class="text-center"><span class="tip"
						title="<spring:message code="sourcelist.tt.date" />"><spring:message
								code="general.date" /></span></th>
					<th class="text-center"><span class="tip"
						title="<spring:message code="sourcelist.tt.type" />"><spring:message
								code="general.type" /></span></th>
					<th class="text-center"><span class="tip"
						title="<spring:message code="sourcelist.tt.LdoDLabel" />"><spring:message
								code="general.LdoDLabel" /></span></th>
					<!--<th><span class="tip"
						title="<spring:message code="general.format" />"><spring:message
								code="general.format" /></span></th>
					<th><span class="tip"
						title="<spring:message code="general.material" />"><spring:message
								code="general.material" /></span></th>
					<th><span class="tip"
						title="<spring:message code="sourcelist.tt.columns" />"><spring:message
								code="general.columns" /></span></th> -->
					<th class="text-center"><span class="tip"
						title="<spring:message code="sourcelist.tt.dimensions" />"><spring:message
								code="general.dimensions" /></span></th>
					<th><span class="tip"
						title="<spring:message code="sourcelist.tt.facsimiles" />"><spring:message
								code="general.facsimiles" /></span></th>
				</tr>
			<tbody>
				<c:forEach var="source" items='${sources}'>
					<tr>
						<td>${source.name}</td>
						<td><c:forEach var='inter'
								items='${source.getSourceIntersSet()}'>
								<a
									href="${contextPath}/fragments/fragment/${inter.getFragment().getXmlId()}/inter/${inter.getUrlId()}">
									${inter.title}</a>
							</c:forEach></td>
						<td>${source.getLdoDDate().print()}</td>
						<td><c:choose>
								<c:when test='${source.getType()=="MANUSCRIPT"}'>
									<c:forEach var='handNote' items='${source.getHandNoteSet()}'>
										<spring:message code="general.manuscript" />(${handNote.getMedium()})
                                        <br>
									</c:forEach>
									<c:forEach var='typeNote' items='${source.getTypeNoteSet()}'>
										<spring:message code="general.typescript" />(${typeNote.getMedium()})
                                        <br>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<spring:message code="general.printed" />
								</c:otherwise>
							</c:choose></td>
						<td><c:if test='${source.getType()=="MANUSCRIPT"}'>
								<c:choose>
									<c:when test='${source.getHasLdoDLabel()}'>
										<spring:message code="general.yes" />
									</c:when>
									<c:otherwise>
										<spring:message code="general.no" />
									</c:otherwise>
								</c:choose>
							</c:if></td>
						<!--
						<td><c:if test='${source.getType()=="MANUSCRIPT"}'>
								<c:choose>
									<c:when test='${source.getForm()=="LEAF"}'>
										<spring:message code="general.leaf" />
									</c:when>
									<c:otherwise></c:otherwise>
								</c:choose>
							</c:if></td>
						<td><c:if test='${source.getType()=="MANUSCRIPT"}'>
								<c:choose>
									<c:when test='${source.getMaterial()=="PAPER"}'>
										<spring:message code="general.paper" />
									</c:when>
									<c:otherwise></c:otherwise>
								</c:choose>
							</c:if></td>
						<td><c:if test='${source.getType()=="MANUSCRIPT"}'>${source.getColumns()}</c:if></td>
						-->
						<td><c:if
								test='${source.getType()=="MANUSCRIPT" && source.getDimensionsSet().size() != 0}'>
								<c:forEach var="dimensions"
									items="${source.getSortedDimensions()}" varStatus="loop">${dimensions.getHeight()}cm
				X ${dimensions.getWidth()}cm<c:if test="${!loop.last}">
										<br>
									</c:if>
								</c:forEach>
							</c:if></td>
						<td><c:forEach var='surface'
								items='${source.getFacsimile().getSurfaces()}'
								varStatus="counter">
								<a href="/facs/${surface.getGraphic()}">(${counter.index+1})
									${source.name} </a>
								<br>
							</c:forEach></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<br>
</body>
<script>
	$('#tablelistsources').attr("data-search", "true");
	$('#tablelistsources').bootstrapTable();
	$(".tip").tooltip({
		placement : 'bottom'
	});
	$('#infosources').popover()
</script>
<script>
	$(document).ready(function() {
		$('#tablelistsources').show();
	});
</script>
</html>