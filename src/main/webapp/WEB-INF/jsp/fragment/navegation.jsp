<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<script type="text/javascript">
$(document).ready(function() {
    $('[id="baseinter"][data-toggle="buttons-checkbox"]').on('click', function() {
	var frag = $('#fragment div:first-child').attr("id");
	var data = new Array();
	$('#baseinter :checked').each(function() {
	    data.push(this.value);
	});
	$.get("${contextPath}/fragments/fragment/inter", {
	    fragment : frag,
	    inters : data
	}, function(html) {
       var newDoc = document.open("text/html", "replace");
       newDoc.write(html);
       newDoc.close();
	});
    });
});
</script>
<script type="text/javascript">
$(document).ready(function() {
    $('[id="virtualinter"][data-toggle="buttons-checkbox"]').on('click', function() {
    var frag = $('#fragment div:first-child').attr("id");
    var data = new Array();
    $('#virtualinter :checked').each(function() {
        data.push(this.value);
    });
    $.get("${contextPath}/fragments/fragment/inter", {
        fragment : frag,
        inters : data
    }, function(html) {
       var newDoc = document.open("text/html", "replace");
       newDoc.write(html);
       newDoc.close();
    });
    });
});
</script>
<div id="fragment" class="row-fluid">
    <div id="${fragment.externalId}"></div>
    <div class="pull-right" id="baseinter"
        data-toggle="buttons-checkbox">
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
                                    test="${inters.contains(sourceInter)}">
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
                        </thead>
                        <c:forEach var="expertEditionInter"
                            items="${expertEdition.getSortedInter4Frag(fragment)}">
                            <tr>
                                <td><c:choose>
                                        <c:when
                                            test="${inters.contains(expertEditionInter)}">
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
    </div>
    <!-- VIRTUAL -->
    <div class="pull-right" id="virtualinter"
        data-toggle="buttons-checkbox">
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
                        <thead>
                            <tr>
                                <th style="width: 20%"></th>
                                <th style="width: 10%"></th>
                                <th style="width: 10%"></th>
                                <th style="width: 10%"></th>
                            </tr>
                        </thead>
                        <c:forEach var="virtualEditionInter"
                            items="${virtualEdition.getSortedInter4Frag(fragment)}">
                            <tr>
                                <td><c:choose>
                                        <c:when
                                            test="${inters.contains(virtualEditionInter)}">
                                            <input type="checkbox"
                                                class="btn"
                                                name="${virtualEditionInter.externalId}"
                                                value="${virtualEditionInter.externalId}"
                                                checked />
                                        </c:when>
                                        <c:otherwise>
                                            <input type="checkbox"
                                                class="btn"
                                                name="${virtualEditionInter.externalId}"
                                                value="${virtualEditionInter.externalId}" />
                                        </c:otherwise>
                                    </c:choose></td>
                                <td><a class="btn btn-mini"
                                    href="${contextPath}/fragments/fragment/inter/prev/number/${virtualEditionInter.externalId}"><i
                                        class="icon-backward"></i></a></td>
                                <td>${virtualEditionInter.number}</td>
                                <td><a class="btn btn-mini"
                                    href="${contextPath}/fragments/fragment/inter/next/number/${virtualEditionInter.externalId}"><i
                                        class="icon-forward"></i></a></td>
                            </tr>
                        </c:forEach>
                        <c:if
                            test="${virtualEdition.participantSet.contains(user) && (inters.size() == 1) && virtualEdition.canAddFragInter(inters.get(0))}">
                            <tr>
                                <td></td>
                                <td><form class="form-horizontal"
                                        method="POST"
                                        action="/virtualeditions/restricted/addinter/${virtualEdition.externalId}/${inters.get(0).externalId}">
                                        <fieldset>
                                            <button type="submit"
                                                class="btn btn-mini">
                                                <i class="icon-plus"></i>
                                                <spring:message
                                                    code="general.add" />
                                            </button>
                                        </fieldset>
                                    </form></td>
                                <td></td>
                                <td></td>
                            </tr>
                        </c:if>
                    </table>
                </div>
            </c:forEach>
        </c:if>
    </div>
</div>