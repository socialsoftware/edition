<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false"%>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$(
								'[id="visualisation-properties-authorial"][data-toggle="buttons-checkbox"]')
								.on(
										'click',
										function() {
											var fragInter2 = $(
													'input:radio[name=inter2]:checked')
													.val();
											var selDel = $(
													'input:checkbox[name=del]')
													.is(':checked');
											var selIns = $(
													'input:checkbox[name=ins]')
													.is(':checked');
											var selSubst = $(
													'input:checkbox[name=subst]')
													.is(':checked');
											var selNotes = $(
													'input:checkbox[name=notes]')
													.is(':checked');
											$
													.get(
															"${contextPath}/fragments/fragment/textualauthorial",
															{
																interp : fragInter2,
																del : selDel,
																ins : selIns,
																subst : selSubst,
																notes : selNotes
															},
															function(html) {
																$(
																		"#fragmentTranscription")
																		.replaceWith(
																				html);
															});
										});
					});
</script>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$(
								'[id="visualisation-properties-editorial"][data-toggle="buttons-checkbox"]')
								.on(
										'click',
										function() {
											var fragInter2 = $(
													'input:radio[name=inter2]:checked')
													.val();
											var selDiff = $(
													'input:checkbox[name=diff]')
													.is(':checked');
											$
													.get(
															"${contextPath}/fragments/fragment/textualeditorial",
															{
																interp : fragInter2,
																diff : selDiff
															},
															function(html) {
																$(
																		"#fragmentTranscription")
																		.replaceWith(
																				html);
																
															});
											
										});
					});
</script>

<div id=fragmentTextual class="row-fluid">
	<div class="row-fluid">

		<form class="form-horizontal">
			<div class="control-group">
				<span class="control-label"><spring:message code="fragment.visualization" /></span>
				<div class="controls form-inline">


					<c:choose>
						<c:when test="${inter.sourceType=='EDITORIAL'}">
							<div class="well" id="visualisation-properties-editorial"
								data-toggle="buttons-checkbox">
								<label class="checkbox inline"> <input type="checkbox"
									class="btn" name=diff value="Yes"> <spring:message code="fragment.highlightdifferences" />
								</label>
							</div>
						</c:when>
						<c:otherwise>
							<div class="well" id="visualisation-properties-authorial"
								data-toggle="buttons-checkbox">
								<label class="checkbox inline"> <input type="checkbox"
									class="btn" name=del value="Yes"> <spring:message code="fragment.showdeleted" />
								</label> <label class="checkbox inline"> <input type="checkbox"
									class="btn" name=ins value="Yes" checked> <spring:message code="fragment.highlightinserted" />
								</label> <label class="checkbox inline"> <input type="checkbox"
									class="btn" name=subst value="Yes"> <spring:message code="fragment.highlightsubstitutions" />
								</label> <label class="checkbox inline"> <input type="checkbox"
									class="btn" name=notes value="Yes" checked> <spring:message code="fragment.shownotes" />
								</label>
							</div>
						</c:otherwise>
					</c:choose>

				</div>
			</div>
		</form>

	</div>



	<%@ include file="/WEB-INF/jsp/fragmentTranscription.jsp"%>

	<br>
	<div id="metatextual" class="row-fluid">
		<div class="well row-fluid span12">${inter.metaTextual}</div>
	</div>
</div>