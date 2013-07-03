<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<script type="text/javascript">
$(document).ready(function() {
    $('[id="interps2"][data-toggle="buttons-radio"]')
        .on('click', function() {
											 var fragInter1 = $('input:radio[name=inter]:checked').val();
	           var fragInter2 = $('input:radio[name=inter2]:checked').val();
               $.get("${contextPath}/fragments/fragment/interpretation", 
               {
                interp : fragInter1,
																interp2Compare : fragInter2
															},
															function(html) {
																$("#fragmentTextual").replaceWith(html);});
    });
});
</script>

<div id="fragmentInterpretation" class="row-fluid">

    <c:if test="${inter!=null}">
        <form class="form-horizontal">
            <div class="control-group">
                <span class="control-label"><spring:message
                        code="fragment.compare" /></span>
                <div class="controls form-inline">
                    <div class="well" id="interps2"
                        data-toggle="buttons-radio">
                        <c:forEach var="fragInter"
                            items='${inter.fragment.sortedInterps}'>
                            <c:if
                                test="${fragInter.sourceType=='EDITORIAL'}">
                                <label class="radio inline"> <c:choose>
                                        <c:when
                                            test="${fragInter.externalId==inter.externalId}">
                                            <input type="radio"
                                                class="btn"
                                                name="inter2"
                                                value="${fragInter.externalId}"
                                                checked />
							${fragInter.shortName} (${ldod:getPercentage(writer,fragInter)}%)
									</c:when>
                                        <c:otherwise>
                                            <input type="radio"
                                                class="btn"
                                                name="inter2"
                                                value="${fragInter.externalId}" />
							${fragInter.shortName} (${ldod:getPercentage(writer,fragInter)}%)
									</c:otherwise>
                                    </c:choose>
                                </label>
                            </c:if>
                        </c:forEach>
                        <c:forEach var="fragInter"
                            items='${inter.fragment.sortedInterps}'>
                            <c:if
                                test="${fragInter.sourceType=='AUTHORIAL'}">
                                <label class="radio inline"> <c:choose>
                                        <c:when
                                            test="${fragInter.externalId==inter.externalId}">
                                            <input type="radio"
                                                class="btn"
                                                name="inter2"
                                                value="${fragInter.externalId}"
                                                checked />
							${fragInter.shortName} (${ldod:getPercentage(writer,fragInter)}%)
									</c:when>
                                        <c:otherwise>
                                            <input type="radio"
                                                class="btn"
                                                name="inter2"
                                                value="${fragInter.externalId}" />
							${fragInter.shortName} (${ldod:getPercentage(writer,fragInter)}%)
									</c:otherwise>
                                    </c:choose>
                                </label>
                            </c:if>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </form>

        <%@ include file="/WEB-INF/jsp/fragment/textual.jsp"%>

    </c:if>
</div>

