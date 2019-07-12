<%@ include file="/WEB-INF/jsp/common/tags-head.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- class="table table-hover table-condensed" -->
<div>

    <table id="tablesearchresults" pageSize=100 data-pagination="true"
           data-page-list="[100, 250, 500, All]">
        <thead>
        <tr>
            <th><spring:message code="fragment"/> (${fragCount})</th>
            <th><spring:message code="interpretations"/> (${interCount})</th>

            <%-- 			<c:forEach items="${search}" var="optionTitle"> --%>
            <%-- 				<td>${ optionTitle.toString()}</td> --%>
            <%-- 			</c:forEach> --%>

            <c:forEach begin="1" end="${searchLenght}" var="val">
                <th>C${val}</th>
            </c:forEach>
            <c:if test="${showSource}">
                <th><spring:message code="search.source"/></th>
            </c:if>
            <c:if test="${showSourceType}">
                <th><spring:message code="authorial.source"/></th>
            </c:if>
            <c:if test="${showLdoD}">
                <th><spring:message code="general.LdoDLabel"/></th>
            </c:if>
            <c:if test="${showPubPlace}">
                <th><spring:message code="general.published"/></th>
            </c:if>
            <c:if test="${showEdition}">
                <th><spring:message code="navigation.edition"/></th>
            </c:if>
            <c:if test="${showHeteronym}">
                <th><spring:message code="general.heteronym"/></th>
            </c:if>
            <c:if test="${showDate}">
                <th><spring:message code="general.date"/></th>
            </c:if>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${results}" var="fragmentEntry">
            <c:forEach items="${fragmentEntry.value }" var="fragInterEntry">
                <c:choose>
                    <c:when test="${fragInterEntry.value.size()>0 }">
                        <tr>
                            <td><a
                                    href="/fragments/fragment/${fragmentEntry.key}">${frontendRequiresInterface.getFragmentTitle(fragmentEntry.key)}</a>
                            </td>
                            <c:choose>
                                <c:when
                                        test="${fragInterEntry.key.type == 'SCHOLAR_INTER' &&
                                        !frontendRequiresInterface.isExpertInter(fragInterEntry.key.getXmlId()) &&
										frontendRequiresInterface.getSourceInterType(fragInterEntry.key.getXmlId()) == 'MANUSCRIPT'}">
                                    <td><a
                                            href="/fragments/fragment/${fragInterEntry.key.getFragmentXmlId()}/inter/${fragInterEntry.key.getUrlId()}">${fragInterEntry.key.getShortName()}</a>
                                    </td>
                                </c:when>
                                <c:when
                                        test="${fragInterEntry.key.type == 'SCHOLAR_INTER' &&
                                        frontendRequiresInterface.isExpertInter(fragInterEntry.key.getXmlId())}">
                                    <td><a
                                            href="/fragments/fragment/${fragInterEntry.key.getFragmentXmlId()}/inter/${fragInterEntry.key.getUrlId()}">${fragInterEntry.key.getShortName()}
                                        (${frontendRequiresInterface.getExpertEditionEditor(fragInterEntry.key.getXmlId())})</a>
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td><a
                                            href="/fragments/fragment/${fragInterEntry.key.getFragmentXmlId()}/inter/${fragInterEntry.key.getUrlId()}">${fragInterEntry.key.getTitle()}</a>
                                    </td>
                                </c:otherwise>
                            </c:choose>

                            <c:forEach items="${search }" var="searchOption">
                                <%
                                    String result = "";
                                %>
                                <c:forEach items="${ fragInterEntry.value }" var="option">
                                    <c:if test="${option == searchOption}">
                                        <%
                                            result = "X";
                                        %>
                                    </c:if>
                                </c:forEach>
                                <td class="text-center"><%=result%>
                                </td>
                            </c:forEach>

                            <c:choose>
                                <c:when test="${showSource}">
                                    <td>${frontendRequiresInterface.getTypeOfFragInter(fragInterEntry.key.xmlId)}</td>
                                </c:when>
                            </c:choose>

                            <c:if test="${showSourceType}">
                                <td><c:if
                                        test="${fragInterEntry.key.type == 'SCHOLAR_INTER' &&
                                        !frontendRequiresInterface.isExpertInter(fragInterEntry.key.getXmlId()) &&
                                        frontendRequiresInterface.getSourceInterType(fragInterEntry.key.getXmlId()) == 'MANUSCRIPT' &&
                                        frontendRequiresInterface.hasTypeNoteSet(fragInterEntry.key.getXmlId())}">
                                    <spring:message code="general.typescript"/>
                                </c:if> <c:if
                                        test="${fragInterEntry.key.type == 'SCHOLAR_INTER' &&
                                        !frontendRequiresInterface.isExpertInter(fragInterEntry.key.getXmlId()) &&
                                        frontendRequiresInterface.getSourceInterType(fragInterEntry.key.getXmlId()) == 'MANUSCRIPT' &&
                                        frontendRequiresInterface.hasHandNoteSet(fragInterEntry.key.getXmlId())}">
                                    <spring:message code="general.manuscript"/>
                                </c:if></td>
                            </c:if>

                            <c:if test="${showLdoD}">
                                <td><c:if
                                        test="${fragInterEntry.key.type == 'SCHOLAR_INTER' &&
                                        !frontendRequiresInterface.isExpertInter(fragInterEntry.key.getXmlId()) &&
                                        frontendRequiresInterface.getSourceInterType(fragInterEntry.key.getXmlId()) == 'MANUSCRIPT'}">
                                    <c:choose>
                                        <c:when
                                                test="${frontendRequiresInterface.getSourceOfSourceInter(fragInterEntry.key.getXmlId()).hasLdoDLabel()}">
                                            <spring:message code="general.yes"/>
                                        </c:when>
                                        <c:otherwise>
                                            <spring:message code="general.no"/>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if></td>
                            </c:if>

                            <c:if test="${showPubPlace}">
                                <c:choose>
                                    <c:when test="${fragInterEntry.key.type == 'SCHOLAR_INTER' &&
                                        !frontendRequiresInterface.isExpertInter(fragInterEntry.key.getXmlId()) &&
                                        frontendRequiresInterface.getSourceInterType(fragInterEntry.key.getXmlId()) == 'PRINTED' }">
                                        <td>${frontendRequiresInterface.getSourceOfSourceInter(fragInterEntry.key.getXmlId()).getTitle()}</td>
                                    </c:when>
                                    <c:otherwise>
                                        <td></td>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>

                            <c:if test="${showEdition}">
                                <c:choose>
                                    <c:when
                                            test="${frontendRequiresInterface.isExpertInter(fragInterEntry.key.getXmlId())}">
                                        <td>${frontendRequiresInterface.getExpertEditionEditor(fragInterEntry.key.getXmlId())}</td>
                                    </c:when>
                                    <c:otherwise>
                                        <td></td>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                            <c:choose>
                                <c:when test="${showHeteronym}">
                                    <td>${frontendRequiresInterface.getHeteronymName(fragInterEntry.key.getXmlId())}</td>
                                </c:when>
                                <c:otherwise>
                                </c:otherwise>
                            </c:choose>


                            <c:if test="${showDate}">
                                <c:choose>
                                    <c:when test="${fragInterEntry.key.type == 'SCHOLAR_INTER' &&
                                        !frontendRequiresInterface.isExpertInter(fragInterEntry.key.getXmlId()) &&
                                        frontendRequiresInterface.getSourceInterType(fragInterEntry.key.getXmlId()) == 'MANUSCRIPT'}">
                                        <td>${frontendRequiresInterface.getSourceOfSourceInter(fragInterEntry.key.getXmlId()).getLdoDDate()}</td>
                                    </c:when>
                                    <c:when
                                            test="${showDate && frontendRequiresInterface.isExpertInter(fragInterEntry.key.getXmlId())}">
                                        <td>${frontendRequiresInterface.getScholarInterDate(fragInterEntry.key.getXmlId())}</td>
                                    </c:when>
                                    <c:otherwise>
                                        <td></td>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                        </tr>
                    </c:when>
                </c:choose>
            </c:forEach>
        </c:forEach>
        </tbody>
    </table>
</div>

