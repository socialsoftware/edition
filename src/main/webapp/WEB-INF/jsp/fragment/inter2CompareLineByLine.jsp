<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<div id=fragmentComparison class="row-fluid">
	<div class="row-fluid">
		<div id="transcription" class="span12">
      <h4>${fragment.title}</h4>
			<div class="well">
				<c:choose>
					<c:when test="${writer.showSpaces}">
						<p style="font-family: monospace;">${writer.getTranscriptionLineByLine()}</p>
					</c:when>
					<c:otherwise>
						<p>${writer.getTranscriptionLineByLine()}</p>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
</div>