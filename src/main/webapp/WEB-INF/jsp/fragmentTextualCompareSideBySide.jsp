<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/ldoD.tld" prefix="ldod"%>
<div id=fragmentComparison class="row-fluid">
	<div class="row-fluid">
		<div id="transcription" class="span6">
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

		<div id="fragmentTranscription" class="span6">
			<div class="well"><c:choose>
					<c:when test="${writer.showSpaces}">
						<p style="font-family: monospace;">${ldod:getTranscription(writer,inter2Compare)}</p></c:when>
					<c:otherwise>
						<p>${ldod:getTranscription(writer,inter2Compare)}</p>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
	<br>
	<div class="row-fluid">
		<div id="metatextual" class="span6">
			<div class="well">
				<p>${inter.metaTextual}</p>
			</div>
		</div>

		<div id="metatextual" class="span6">
			<div class="well">
				<p>${inter2Compare.metaTextual}</p>
			</div>
		</div>


	</div>
</div>
