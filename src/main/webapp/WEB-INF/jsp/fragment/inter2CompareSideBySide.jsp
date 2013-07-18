<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<div id=fragmentComparison class="row-fluid">
    <div class="row-fluid">
        <c:forEach var="inter" items="${inters}">
            <div id="fragmentTranscription" class="span6">
                <h4>${inter.title}</h4>
                <div class="well">
                    <c:choose>
                        <c:when test="${writer.showSpaces}">
                            <p style="font-family: monospace;">${ldod:getTranscription(writer,inter)}</p>
                        </c:when>
                        <c:otherwise>
                            <p>${ldod:getTranscription(writer,inter)}</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </c:forEach>
    </div>
    <br>
    <div class="row-fluid">
        <c:forEach var="inter" items="${inters}">
            <div id="interMeta" class="span6">
                <div class="well">
                    <p>${inter.metaTextual}</p>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
