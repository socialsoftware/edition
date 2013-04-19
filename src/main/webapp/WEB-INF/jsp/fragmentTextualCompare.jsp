<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/ldoD.tld" prefix="ldod"%>
<%@ page session="false"%>
<div id=fragmentTextual class="row-fluid">
	<div class="row-fluid">
		<div id="transcription" class="span6">
			<div class="addBorder">
				<p>${ldod:getTranscription(writer,inter)}</p>
			</div>
		</div>

		<div id="fragmentTranscription" class="span6">
			<div class="addBorder">
				<p>${ldod:getTranscription(writer,inter2Compare)}</p>
			</div>
		</div>
	</div>
	<br>
	<div class="row-fluid">
		<div id="metatextual" class="span6">
			<div class="addBorder">${inter.metaTextual}</div>
		</div>

		<div id="metatextual" class="span6">
			<div class="addBorder">${inter2Compare.metaTextual}</div>
		</div>


	</div>
</div>
