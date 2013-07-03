<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$('[id="interps"][data-toggle="buttons-radio"]')
								.on(
										'click',
										function() {
											var fragInter = $(
													'input:radio[name=inter]:checked')
													.val();
											if (fragInter == null)
												alert("Escolha um testemunho base");
											else
												$.get("${contextPath}/fragments/fragment",
								        {
										      interp : fragInter
											  },
																function(html) {
																	$(
																			"#fragmentInterpretation")
																			.replaceWith(
																					html);
																});
										});
					});
</script>
</head>
<body>
    <%@ include file="/WEB-INF/jsp/common/ldod-header.jsp"%>

    <div class="container-fluid">
        <h3 class="text-center">${fragment.title}</h3>
        <hr>
        <div class="row-fluid">
            <div class="span10">

                <div class="row-fluid">


                    <form class="form-horizontal">
                        <div class="control-group">
                            <span class="control-label"><spring:message
                                    code="fragment.base" /></span>
                            <div class="controls form-inline">

                                <div class="well" id="interps"
                                    data-toggle="buttons-radio">
                                    <c:forEach var="fragInter"
                                        items='${fragment.sortedInterps}'>
                                        <c:if
                                            test="${fragInter.sourceType=='EDITORIAL'}">
                                            <label class="radio inline"
                                                for="${fragInter.externalId}">

                                                <c:choose>
                                                    <c:when
                                                        test="${fragInter.externalId==inter.externalId}">
                                                        <input
                                                            type="radio"
                                                            class="btn"
                                                            name="inter"
                                                            id="${fragInter.externalId}"
                                                            value="${fragInter.externalId}"
                                                            checked /> ${fragInter.shortName} 
													</c:when>
                                                    <c:otherwise>
                                                        <input
                                                            type="radio"
                                                            class="btn"
                                                            name="inter"
                                                            value="${fragInter.externalId}" /> ${fragInter.shortName}
													</c:otherwise>
                                                </c:choose>
                                            </label>
                                        </c:if>
                                    </c:forEach>
                                    <br>
                                    <c:forEach var="fragInter"
                                        items='${fragment.sortedInterps}'>
                                        <c:if
                                            test="${fragInter.sourceType=='AUTHORIAL'}">
                                            <label class="radio inline"
                                                for="${fragInter.externalId}">
                                                <c:choose>
                                                    <c:when
                                                        test="${fragInter.externalId==inter.externalId}">
                                                        <input
                                                            type="radio"
                                                            class="btn"
                                                            name="inter"
                                                            id="${fragInter.externalId}"
                                                            value="${fragInter.externalId}"
                                                            checked /> ${fragInter.shortName}
													</c:when>
                                                    <c:otherwise>
                                                        <input
                                                            type="radio"
                                                            class="btn"
                                                            name="inter"
                                                            value="${fragInter.externalId}" /> ${fragInter.shortName}
													</c:otherwise>
                                                </c:choose>
                                            </label>
                                        </c:if>
                                    </c:forEach>

                                </div>
                            </div>
                        </div>
                    </form>
                    <%@ include
                        file="/WEB-INF/jsp/fragment/interpretation.jsp"%>
                </div>
            </div>
            <div class="span2">
                <%@ include file="/WEB-INF/jsp/fragment/navegation.jsp"%>
            </div>
        </div>
    </div>
</body>
</html>
