<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<h3 class="text-center">
    <spring:message code="tableofcontents.editionof" />
    ${edition.editor}
</h3>
<c:if test="${heteronym != null}">
    <h4 class="text-left">
        <spring:message code="tableofcontents.fragmentsof" />
        ${heteronym.name}
    </h4>
</c:if>

<table class="table table-bordered table-condensed">
    <thead>
        <tr>
            <th><spring:message code="tableofcontents.number" /></th>
            <th><spring:message code="tableofcontents.title" /></th>
            <th><spring:message code="tableofcontents.volume" /></th>
            <th><spring:message code="tableofcontents.page" /></th>
        </tr>
    <tbody>
        <c:forEach var="interp" items='${edition.sortedInterps}'>
            <c:if
                test="${(heteronym == null) || (interp.heteronym == heteronym)}">
                <tr>
                    <td><c:if test="${interp.number!=0}">${interp.number}</c:if></td>
                    <td><a
                        href="${contextPath}/fragments/fragment/inter/${interp.externalId}">${interp.title}</a></td>
                    <td>${interp.volume}</td>
                    <td>${interp.startPage}</td>
                </tr>
            </c:if>
        </c:forEach>
    </tbody>
</table>
