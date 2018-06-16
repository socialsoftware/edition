<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<div>
	<c:if test="${inter != null}">
		<h4 class="text-center">${inter.title}</h4>
		<br>
		<c:choose>
			<c:when test="${inter.sourceType=='EDITORIAL'}">
				<div class="well" style="font-family: georgia; font-size: large;">
					<p>${writer.getTranscription()}</p>
				</div>
			</c:when>
			<c:otherwise>
				<div class="well" style="font-family: courier;">
					<p>${writer.getTranscription()}</p>
				</div>
			</c:otherwise> 
		</c:choose>
	</c:if>
</div>


