<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/ldoD.tld" prefix="ldod"%>
<%@ page session="false"%>
<div id=fragmentComparison class="row-fluid">
	<div class="row-fluid">
		<div id="transcription" class="span12">
			<div class="addBorder">
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