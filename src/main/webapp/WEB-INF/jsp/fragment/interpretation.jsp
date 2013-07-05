<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<script type="text/javascript">
$(document).ready(function() {
    $('[id="interps2"][data-toggle="buttons-checkbox"]')
        .on('click', function() {
	               var fragInter1 = $('input:radio[name=inter]:checked').val();
                 var data = new Array();
                 $('#interps2 :checked').each(function() {
                        data.push(this.value);
                 });
               $.get("${contextPath}/fragments/fragment/interpretation", 
               {
                baseID : fragInter1,
  							interpsID : data
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
                        data-toggle="buttons-checkbox">
                        <c:forEach var="fragInter"
                            items='${inter.fragment.sortedInterps}'>
                            <c:if
                                test="${fragInter.sourceType=='EDITORIAL'}">
                                <label class="checkbox inline">
                                    <c:choose>
                                        <c:when
                                            test="${fragInter.externalId==inter.externalId}">
                                            <input type="checkbox"
                                                class="btn"
                                                name="${fragInter.externalId}"
                                                value="${fragInter.externalId}"
                                                checked disabled/>
							${fragInter.shortName} (${ldod:getPercentage(writer,fragInter)}%)
									</c:when>
                                        <c:otherwise>
                                            <input type="checkbox"
                                                class="btn"
                                                name="${fragInter.externalId}"
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
                                <label class="checkbox inline">
                                    <c:choose>
                                        <c:when
                                            test="${fragInter.externalId==inter.externalId}">
                                            <input type="checkbox"
                                                class="btn"
                                                name="${fragInter.externalId}"
                                                value="${fragInter.externalId}"
                                                checked disabled/>
							${fragInter.shortName} (${ldod:getPercentage(writer,fragInter)}%)
									</c:when>
                                        <c:otherwise>
                                            <input type="checkbox"
                                                class="btn"
                                                name="${fragInter.externalId}"
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

