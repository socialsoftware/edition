<%@ include file="/WEB-INF/jsp/common/tags-head.jsp" %>

<div>
    <table id="tablesearchresults" data-pagination="false">
        <thead>
        <tr>
            <th><spring:message code="fragment"/>
                (${fragCount})
            </th>
            <th><spring:message code="interpretations"/>
                (${interCount})
            </th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${results}" var="fragmentEntry">
        <c:forEach items="${ fragmentEntry.value }"
                   var="fragInterEntry">
        <tr>
            <td>
                <a href="/fragments/fragment/${fragmentEntry.key}">${frontendRequiresInterface.getFragmentTitle(fragmentEntry.key)}</a>
            </td>
            <c:choose>
            <c:when
                    test="${fragInterEntry.getType() =='EDITORIAL'}">
            <td><a
                    href="/fragments/fragment/${fragInterEntry.getFragmentXmlId()}/inter/${fragInterEntry.getUrlId()}">${fragInterEntry.getTitle()}
                (${fragInterEntry.getEditionReference()})</a>
            </td>
            </c:when>
            <c:otherwise>
            <td><a
                    href="/fragments/fragment/${fragInterEntry.getFragmentXmlId()}/inter/${fragInterEntry.getUrlId()}">${fragInterEntry.getShortName()}</a>
            </td>
            </c:otherwise>
            </c:choose>
            </c:forEach>
            </c:forEach>
        </tbody>
    </table>

</div>
