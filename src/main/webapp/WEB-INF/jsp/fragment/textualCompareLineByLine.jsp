<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<div id=fragmentComparison class="row-fluid">
	<div class="row-fluid">
		<div id="transcription" class="span12">
			<div class="well">
				<c:choose>
					<c:when test="${writer.showSpaces}">
						<p style="font-family: monospace;">${ldod:getTranscriptionLineByLine(writer,inter,inter2Compare)}</p>
					</c:when>
					<c:otherwise>
						<p>${ldod:getTranscriptionLineByLine(writer,inter,inter2Compare)}</p>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
</div>