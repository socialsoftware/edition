<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<div id=fragmentComparison class="row"  style="margin-left:0px;margin-right:0px">
    <div id="transcription">
        <h4>${fragment.title}
        <a id="infohighlight" class="infobutton" role="button" data-toggle="popover" data-content="<spring:message code="info.highlighting" />"> <span class="glyphicon glyphicon-info-sign"></span></a>
        </h4>
        <div class="well">
            <c:choose>
                <c:when test="${writer.showSpaces}">
                    <p style="font-family: monospace;">${writer.getTranscriptionLineByLine()}</p>
                </c:when>
                <c:otherwise>
                    <p style="font-family: georgia;">${writer.getTranscriptionLineByLine()}</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

</div>

<script type="text/javascript">
$('#infohighlight').popover()
</script>