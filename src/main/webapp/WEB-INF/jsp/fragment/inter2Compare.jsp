<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<script type="text/javascript">
$(document)
	.ready(
		function() {
		    $(
			    '[id="visualisation-properties-comparison"][data-toggle="buttons-checkbox"]')
			    .on(
				    'click',
				    function() {
					var data = new Array();
					$('#baseinter :checked').each(function() {
					    data.push(this.value);
					});
					var selLine = $(
						'input:checkbox[name=line]')
						.is(':checked');
					var selSpaces = $(
						'input:checkbox[name=spaces]')
						.is(':checked');
					$
						.get(
							"${contextPath}/fragments/fragment/inter/compare",
							{
							    inters : data,
							    line : selLine,
							    spaces : selSpaces
							},
							function(html) {
							    $(
								    "#fragmentComparison")
								    .replaceWith(
									    html);

							});

				    });
		});
</script>
<div id=fragmentInter class="row-fluid">
    <div class="row-fluid">
        <form class="form-horizontal">
            <div class="control-group">
                <div class="controls form-inline">
                    <div id="visualisation-properties-comparison"
                        data-toggle="buttons-checkbox">
                        <c:if test="${!lineByLine}">
                            <label class="checkbox inline"> <input
                                type="checkbox" class="btn" name=line
                                value="Yes"> <spring:message
                                    code="fragment.linebyline" />
                            </label>
                        </c:if>
                        <label class="checkbox inline"> <input
                            type="checkbox" class="btn" name=spaces
                            value="Yes"> <spring:message
                                code="fragment.alignspace" />
                        </label>
                    </div>
                </div>
            </div>
        </form>
    </div>
    
    <c:choose>
        <c:when test="${!lineByLine}">
            <%@ include
                file="/WEB-INF/jsp/fragment/inter2CompareSideBySide.jsp"%>
        </c:when>
        <c:otherwise><%@ include
                file="/WEB-INF/jsp/fragment/inter2CompareLineByLine.jsp"%></c:otherwise>
    </c:choose>

</div>
