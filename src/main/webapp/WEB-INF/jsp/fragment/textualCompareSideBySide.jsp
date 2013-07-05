<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<div id=fragmentComparison class="row-fluid">
    <div class="row-fluid">
        <div id="transcription" class="span6">
            <h5>${inter.title}</h5>
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
        <c:forEach var="inter2" items="${inter2Compare}">
            <c:if test="${inter2!=inter}">
                <div id="fragmentTranscription" class="span6">
                    <h5>${inter2.title}</h5>
                    <div class="well">
                        <c:choose>
                            <c:when test="${writer.showSpaces}">
                                <p style="font-family: monospace;">${ldod:getTranscription(writer,inter2)}</p>
                            </c:when>
                            <c:otherwise>
                                <p>${ldod:getTranscription(writer,inter2)}</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:if>
        </c:forEach>
    </div>
    <br>
    <div class="row-fluid">
        <div id="metatextual" class="span6">
            <div class="well">
                <p>${inter.metaTextual}</p>
            </div>
        </div>

        <c:forEach var="inter2" items="${inter2Compare}">
            <c:if test="${inter2!=inter}">
                <div id="metatextual" class="span6">
                    <div class="well">
                        <p>${inter2.metaTextual}</p>
                    </div>
                </div>
            </c:if>
        </c:forEach>


    </div>
</div>
