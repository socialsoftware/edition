<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>

<c:set var="isEditorial" value="${inter.getSourceType() == 'EDITORIAL'}" />
<c:set var="isManuscript" value="false" />
<c:set var="isPublication" value="false" />
<c:if
	test="${inter.getSourceType() == 'AUTHORIAL' && inter.getSource().getType() == 'MANUSCRIPT'}">
	<c:set var="isManuscript" value="true" />
</c:if>
<c:if
	test="${inter.getSourceType() == 'AUTHORIAL' && inter.getSource().getType() == 'PRINTED'}">
	<c:set var="isPublication" value="true" />
</c:if>

<c:choose>
	<c:when test="${isEditorial}">
		<c:set var="title" value="${inter.getTitle()}" />
	</c:when>
	<c:when test="${isManuscript}">
		<c:set var="title" value="" />
	</c:when>
	<c:when test="${isPublication}">
		<c:set var="title" value="${inter.getSource().getTitle()}" />
	</c:when>
</c:choose>
<c:if test='${!title.equals("")}'>
	<strong><spring:message code="general.title" />:</strong> ${title}	</c:if>

<c:if test="${isManuscript}">
	<strong><spring:message code="general.identification" />:</strong>
	${inter.getSource().getIdno()}</c:if>

<br>
<strong><spring:message code="general.heteronym" />:</strong>
<c:choose>
	<c:when test="${!inter.getHeteronym().isNullHeteronym()}">${inter.getHeteronym().getName()}</c:when>
	<c:otherwise>
		<spring:message code="general.heteronym.notassigned" />
	</c:otherwise>
</c:choose>

<c:if test="${isManuscript}">
	<c:if test="${inter.getSource().getForm() == 'LEAF'}">
		<br>
		<strong><spring:message code="general.format" />:</strong>
		<spring:message code="general.leaf" />
		<c:if test='${inter.getSource().getDimensionsSet().size() != 0}'>
			<small>(<c:forEach var="dimensions"
					items="${inter.getSource().getSortedDimensions()}" varStatus="loop">${dimensions.getHeight()}cm
				X ${dimensions.getWidth()}cm<c:if test="${!loop.last}">, </c:if></c:forEach>)</small>
		</c:if>
	</c:if>

	<c:if test="${inter.getSource().getMaterial() == 'PAPER'}">
		<br>
		<strong><spring:message code="general.material" />:</strong>
		<spring:message code="general.paper" />
	</c:if>

	<c:if test="${inter.getSource().getColumns() != 0}">
		<br>
		<strong><spring:message code="general.columns" />:</strong> ${inter.getSource().getColumns()}
		</c:if>

	<br>
	<strong>LdoD Mark:</strong>
	<c:if test="${inter.getSource().getHasLdoDLabel()}">
		<spring:message code="search.ldod.with" />
	</c:if>
	<c:if test="${!inter.getSource().getHasLdoDLabel()}">
		<spring:message code="search.ldod.without" />
	</c:if>

	<c:forEach var="handNote" items="${inter.getSource().getHandNoteSet()}">
		<br>
		<strong><spring:message code="general.manuscript" /></strong>
		<c:if test="${handNote.getMedium() != null}"> (<em>${handNote.getMedium().getDesc()}</em>)</c:if>
		<strong>:</strong> ${handNote.getNote()} 
		</c:forEach>

	<c:forEach var="typeNote" items="${inter.getSource().getTypeNoteSet()}">
		<br>
		<strong><spring:message code="general.typescript" /></strong>
		<c:if test="${typeNote.getMedium() != null}"> (<em>${typeNote.getMedium().getDesc()}</em>)</c:if>
		<strong>:</strong> ${typeNote.getNote()} 
		</c:forEach>
</c:if>

<c:if test='${isEditorial && !inter.getVolume().equals("")}'>
	<br>
	<strong><spring:message code="tableofcontents.volume" />:</strong>
	${inter.getVolume()}</c:if>

<c:if test="${isPublication}">
	<br>
	<strong><spring:message code="general.journal" />:</strong>
	${inter.getSource().getJournal()}</c:if>

<c:choose>
	<c:when test="${isEditorial}">
		<c:set var="number" value="${inter.getCompleteNumber()}" />
	</c:when>
	<c:when test="${isManuscript}">
		<c:set var="number" value="" />
	</c:when>
	<c:when test="${isPublication}">
		<c:set var="number" value="${inter.getSource().getIssue()}" />
	</c:when>
</c:choose>
<c:if test='${!number.equals("")}'> 
	<br>
	<strong><spring:message code="tableofcontents.number" />:</strong>
	${number}</c:if>

<c:choose>
	<c:when test="${isEditorial}">
		<c:set var="startPage" value="${inter.getStartPage()}" />
		<c:set var="endPage" value="${inter.getEndPage()}" />
	</c:when>
	<c:when test="${isManuscript}">
		<c:set var="startPage" value="0" />
	</c:when>
	<c:when test="${isPublication}">
		<c:set var="startPage" value="${inter.getSource().getStartPage()}" />
		<c:set var="endPage" value="${inter.getSource().getEndPage()}" />
	</c:when>
</c:choose>
<c:if test="${startPage != 0}">
	<br>
	<strong><spring:message code="tableofcontents.page" />:</strong> ${startPage}
	<c:if test="${startPage != endPage}"> - ${endPage}</c:if>
</c:if>

<c:if test="${isPublication}">
	<br>
	<strong><spring:message code="general.published.place" />:</strong>
	${inter.getSource().getPubPlace()}</c:if>

<c:choose>
	<c:when test="${isEditorial}">
		<c:set var="date" value="${inter.getLdoDDate()}" />
	</c:when>
	<c:when test="${isManuscript}">
		<c:set var="date" value="${inter.getSource().getLdoDDate()}" />
	</c:when>
	<c:when test="${isPublication}">
		<c:set var="date" value="${inter.getSource().getLdoDDate()}" />
	</c:when>
</c:choose>
<c:if test="${date != null}">
	<br>
	<strong><spring:message code="general.date" />:</strong>
	${date.print()}<c:if test="${date.getPrecision() != null}"> (<em>${date.getPrecision().getDesc()}</em>)</c:if>
</c:if>

<c:choose>
	<c:when test="${isEditorial}">
		<c:set var="notes" value="${inter.getNotes()}" />
	</c:when>
	<c:when test="${isManuscript}">
		<c:set var="notes" value="${inter.getSource().getNotes()}" />
	</c:when>
	<c:when test="${isPublication}">
		<c:set var="notes" value="" />
	</c:when>
</c:choose>
<c:if test='${!notes.equals("")}'>
	<br>
	<strong><spring:message code="general.note" />:</strong>
	${notes}</c:if>

<c:forEach var="note" items='${inter.getSortedAnnexNote()}'>
	<br>
	<strong><spring:message code="general.note" />:</strong> ${note.getNoteText().generatePresentationText()}
	</c:forEach>

<c:if test="${isManuscript || isPublication}">
	<br>
	<strong><spring:message code="general.facsimiles" />:</strong>
	<c:forEach var="surf"
		items="${inter.getSource().getFacsimile().getSurfaces()}"
		varStatus="loop">
		<a href=/facs/${surf.getGraphic()}>${inter.getSource().getAltIdentifier()}.${loop.index+1}</a>
		<c:if test="${!loop.last}">,</c:if>
	</c:forEach>
</c:if>
