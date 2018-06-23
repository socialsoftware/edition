<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<div id=fragmentComparison class="row"
	style="margin-left: 0px; margin-right: 0px">

	<div class="row">
		<c:forEach var="inter" items="${inters}">
			<div id="fragmentTranscription" class="col-md-6">
				<h4>${inter.title}</h4>
				<div class="well">
					<c:choose>
						<c:when test="${writer.showSpaces}">
							<p style="font-family: monospace;">${writer.getTranscription(inter)}</p>
						</c:when>
						<c:otherwise>
							<p style="font-family: georgia;">${writer.getTranscription(inter)}</p>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</c:forEach>
	</div>
	<br>
	<div class="row">
		<c:forEach var="inter" items="${inters}">
			<div id="interMeta" class="col-md-6">
				<div class="well">
					<%@ include file="/WEB-INF/jsp/fragment/interMetaInfo.jsp"%>
				</div>
			</div>
		</c:forEach>
	</div>
</div>
