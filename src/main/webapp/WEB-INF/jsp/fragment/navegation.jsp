<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<script type="text/javascript">
$(document).ready(function() {
    $('[id="inter"][data-toggle="buttons-checkbox"]').on('click', 
        function() {
            var frag = $('#fragment div:first-child').attr("id");
            var data = new Array();
            $('#inter :checked').each(function() {
                data.push(this.value);
            }); 
            $.get("${contextPath}/fragments/fragment/inter",
            {
                fragment : frag,
                inters : data
            },
            function(html) {$("#fragmentInter").replaceWith(html);});
    });
});
</script>
<div id="fragment" class="row-fluid">
    <div id="${fragment.externalId}"></div>
    <div class="pull-right" id="inter" data-toggle="buttons-checkbox">
        <!-- EDITORIAL -->
        <h5 class="text-center">
            <spring:message code="edition.experts" />
        </h5>
        <c:forEach var="expertEdition"
            items='${ldoD.sortedExpertEdition}'>
            <c:if
                test="${expertEdition.getSortedInter4Frag(fragment).size() != 0}">
                <div class="text-center">
                    <table>
                        <caption>
                            <a
                                href="${contextPath}/edition/internalid/${expertEdition.externalId}">
                                ${expertEdition.editor}</a>
                        </caption>
                        <thead>
                            <tr>
                                <th style="width: 20%"></th>
                                <th style="width: 10%"></th>
                                <th style="width: 10%"></th>
                                <th style="width: 10%"></th>
                            </tr>

                            <c:forEach var="expertEditionInter"
                                items="${expertEdition.getSortedInter4Frag(fragment)}">
                                <tr>
                                    <td><c:choose>
                                            <c:when
                                                test="${inter==expertEditionInter}">
                                                <input type="checkbox"
                                                    class="btn"
                                                    name="${expertEditionInter.externalId}"
                                                    value="${expertEditionInter.externalId}"
                                                    checked />
                                            </c:when>
                                            <c:otherwise>
                                                <input type="checkbox"
                                                    class="btn"
                                                    name="${expertEditionInter.externalId}"
                                                    value="${expertEditionInter.externalId}" />
                                            </c:otherwise>
                                        </c:choose></td>
                                    <td><a class="btn btn-mini"
                                        href="${contextPath}/fragments/fragment/inter/prev/number/${expertEditionInter.externalId}"><i
                                            class="icon-backward"></i></a></td>
                                    <td>${expertEditionInter.number}</td>
                                    <td><a class="btn btn-mini"
                                        href="${contextPath}/fragments/fragment/inter/next/number/${expertEditionInter.externalId}"><i
                                            class="icon-forward"></i></a></td>
                                </tr>
                            </c:forEach>
                    </table>
                </div>
            </c:if>
        </c:forEach>
        <br>
        <!-- AUTHORIAL -->
        <h5 class="text-center">
            <spring:message code="authorial.source" />
        </h5>
        <div class="text-center">
            <table>
                <tr>
                    <th style="width: 20%"></th>
                    <th style="width: 30%"></th>
                </tr>
                <c:forEach var="sourceInter"
                    items='${fragment.sortedSourceInter}'>
                    <tr>
                        <td><c:choose>
                                <c:when
                                    test="${sourceInter==expertEditionInter}">
                                    <input type="checkbox" class="btn"
                                        name="${sourceInter.externalId}"
                                        value="${sourceInter.externalId}"
                                        checked />
                                </c:when>
                                <c:otherwise>
                                    <input type="checkbox" class="btn"
                                        name="${sourceInter.externalId}"
                                        value="${sourceInter.externalId}" />
                                </c:otherwise>
                            </c:choose></td>
                        <td class="pull-left">${sourceInter.shortName}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        <br>
        <!-- VIRTUAL -->
        <c:if
            test="${(ldoDSession != null) && (ldoDSession.getSelectedVEs().size() != 0)}">
            <h5 class="text-center">
                <spring:message code="virtual.editions" />
            </h5>
            <c:forEach var="virtualEdition"
                items='${ldoDSession.selectedVEs}'>
                <div class="text-center">
                    <table>
                        <caption>
                            <a
                                href="${contextPath}/edition/internalid/${virtualEdition.externalId}">
                                ${virtualEdition.acronym}</a>
                        </caption>
                        <tr>
                            <th style="width: 30%"></th>
                        </tr>
                        <tr>
                            <td>${virtualEdition.acronym}</td>
                        </tr>
                    </table>
                </div>
            </c:forEach>
        </c:if>
    </div>
</div>

