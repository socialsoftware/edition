<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>

<div>
	<table id="tablesearchresults" data-pagination="false">
        <thead>
            <tr>
                <th data-field="state" data-checkbox="true" data-visible="false">
                <th data-visible="false"></th>
                <th data-visible="false"></th>
                <th data-visible="false"></th>
                <th data-visible="false"></th>
                <th><spring:message code="fragment" />
                    (${fragCount})</th>
                <th><spring:message code="interpretations" />
                    (${interCount})</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${results}" var="fragmentEntry">
                <c:forEach items="${ fragmentEntry.value }"
                    var="fragInterEntry">
                    <tr>
                        <td></td>
                        <td>${fragInterEntry.getExternalId()}</td>
                        <td>${fragmentEntry.key.getTitle()}</td>
                        <td>${fragInterEntry.getShortName()}</td>
                        <td>${fragmentEntry.key.getExternalId()}</td>
                        <!--  <td><input type="checkbox" name="selectedinters" value="${fragInterEntry.getExternalId()}" /></td>-->
                        <td><a
                            href="/fragments/fragment/${fragmentEntry.key.getXmlId()}">${fragmentEntry.key.getTitle()}</a>
                            <input type="hidden" name="title"
                            value="${fragmentEntry.key.getTitle()}"
                            id="title${fragInterEntry.getExternalId()}">
                        </td>                     
                        <c:choose>
                            <c:when
                                test="${fragInterEntry.getClass().getSimpleName().equals('SourceInter') && 
										fragInterEntry.getSource().getType() == 'MANUSCRIPT'}">
                                <td><a
                                    href="/fragments/fragment/${fragInterEntry.getFragment().getXmlId()}/inter/${fragInterEntry.getUrlId()}">${fragInterEntry.getShortName()}</a>
                                </td>
                            </c:when>
                            <c:when
                                test="${fragInterEntry.getClass().getSimpleName().equals('ExpertEditionInter')}">
                                <td><a
                                    href="/fragments/fragment/${fragInterEntry.getFragment().getXmlId()}/inter/${fragInterEntry.getUrlId()}">${fragInterEntry.getTitle()}
                                        (${fragInterEntry.getEdition().getEditor()})</a>

                                </td>
                            </c:when>
                            <c:otherwise>
                                <td><a
                                    href="/fragments/fragment/${fragInterEntry.getFragment().getXmlId()}/inter/${fragInterEntry.getUrlId()}">${fragInterEntry.getTitle()}</a></td>
                            </c:otherwise>
                        </c:choose>
                </c:forEach>
            </c:forEach>
        </tbody>
    </table>

</div>
