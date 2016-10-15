<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<h3 class="text-center">
	<spring:message code="tableofcontents.editionof" />
	${edition.editor} (${edition.getSortedInterps().size()})
</h3>
<c:if test="${heteronym != null}">
	<h3 class="text-left">
		<spring:message code="tableofcontents.fragmentsof" />
		${heteronym.name}
	</h3>
	<br>
</c:if>
<br>
<table id="tableexperts" data-pagination="false">
	<!-- <table class="table table-hover table-condensed"> -->
	<thead>
		<tr>
			<th><span class="tip"
				title="<spring:message code="tableofcontents.tt.number" />"><spring:message
						code="tableofcontents.number" /></span></th>
			<th><span class="tip"
				title="<spring:message code="tableofcontents.tt.title" />"><spring:message
						code="tableofcontents.title" /></span></th>
			<th><span class="tip"
				title="<spring:message code="tableofcontents.tt.reading" />"><spring:message
						code="general.reading" /></span></th>
			<c:if
				test='${edition.getAcronym().equals("JPC")}'>
				<th><span class="tip"
					title="<spring:message code="tableofcontents.tt.volume" />"><spring:message
							code="tableofcontents.volume" /></span></th>
			</c:if>
			<th><span class="tip"
				title="<spring:message code="tableofcontents.tt.page" />"><spring:message
						code="tableofcontents.page" /></span></th>
			<th><span class="tip"
				title="<spring:message code="tableofcontents.tt.date" />"><spring:message
						code="general.date" /></span></th>
			<th><span class="tip"
				title="<spring:message code="tableofcontents.tt.heteronym" />"><spring:message
						code="general.heteronym" /></span></th>
		</tr>
	<tbody>
		<c:forEach var="interp" items='${edition.sortedInterps}'>
			<c:if
				test="${(heteronym == null) || (interp.heteronym == heteronym)}">
				<tr>
					<td class="text-center"><c:if test="${interp.number!=0}">${interp.number}</c:if></td>
					<td><a
						href="${contextPath}/fragments/fragment/inter/${interp.externalId}">${interp.title}</a></td>
					<td class="text-center"><a
						href="${contextPath}/reading/inter/first/inter/${interp.externalId}"><span
							class="glyphicon glyphicon-eye-open"></span></a></td>
					<c:if
						test='${edition.getAcronym().equals("JPC")}'>
						<td class="text-center">${interp.volume}</td>
					</c:if>
					<td class="text-center">${interp.startPage}</td>
					<td class="text-center">${interp.getLdoDDate().getDate().toString("dd-MM-yyyy")}</td>
					<td>${interp.getHeteronym().getName()}</td>
				</tr>
			</c:if>
		</c:forEach>

	</tbody>
</table>
<br>
<script>
$('#tableexperts').attr("data-search","true");
$('#tableexperts').bootstrapTable();
$(".tip").tooltip({placement: 'bottom'});
</script>
