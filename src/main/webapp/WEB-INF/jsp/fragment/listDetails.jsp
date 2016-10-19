<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<div id="fragmentList" class="row">
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th><spring:message code="tableofcontents.title" /></th>
				<th><spring:message code="navigation.edition" /> Jacinto do
					Prado Coelho</th>
				<th><spring:message code="navigation.edition" /> Teresa Sobral
					Cunha</th>
				<th><spring:message code="navigation.edition" /> Richard
					Zenith</th>
				<th><spring:message code="navigation.edition" /> Jerónimo
					Pizarro</th>
				<th><spring:message code="header.authorialsources" /></th>
				<th><spring:message code="header.authorialsources" /></th>
				<th><spring:message code="header.authorialsources" /></th>
			</tr>
		<tbody>
			<c:forEach var="fragment" items='${fragments}'>
				<tr>
					<td><a
						href="${contextPath}/fragments/fragment/${fragment.externalId}">${fragment.title}</a>
					</td>
					<td><c:forEach var="fragInter"
							items='${fragment.getExpertEditionInters(jpcEdition)}'>${fragInter.getMetaTextual()}</c:forEach>
					</td>
					<td><c:forEach var="fragInter"
							items='${fragment.getExpertEditionInters(tscEdition)}'>${fragInter.getMetaTextual()}</c:forEach>
					</td>
					<td><c:forEach var="fragInter"
							items='${fragment.getExpertEditionInters(rzEdition)}'>${fragInter.getMetaTextual()}</c:forEach>
					</td>
					<td><c:forEach var="fragInter"
							items='${fragment.getExpertEditionInters(jpEdition)}'>${fragInter.getMetaTextual()}</c:forEach>
					</td>
					<c:forEach var="fragInter"
						items='${fragment.getSortedSourceInter()}'>
						<td>${fragInter.metaTextual}</td>
					</c:forEach>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
