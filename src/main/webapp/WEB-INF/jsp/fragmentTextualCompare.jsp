<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/ldoD.tld" prefix="ldod"%>
<%@ page session="false"%>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$(
								'[id="visualisation-properties-comparison"][data-toggle="buttons-checkbox"]')
								.on(
										'click',
										function() {
											var fragInter1 = $(
											'input:radio[name=inter]:checked')
											.val();
											var fragInter2 = $(
											'input:radio[name=inter2]:checked')
											.val();
											var selLine = $(
													'input:checkbox[name=line]')
													.is(':checked');
											$
													.get(
															"${contextPath}/fragments/fragment/interpretation/mode",
															{
																interp : fragInter1,
																interp2Compare : fragInter2,
																line : selLine
															},
															function(html) {
																$(
																		"#fragmentComparison")
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
				<span class="control-label">Atributos de Visualização:</span>
				<div class="controls form-inline">
					<div class="well" id="visualisation-properties-comparison"
						data-toggle="buttons-checkbox">
						<label class="checkbox inline"> <input type="checkbox"
							class="btn" name=line value="Yes"> Linha-a-linha
						</label>
					</div>
				</div>
			</div>
		</form>
	</div>

	<%@ include file="/WEB-INF/jsp/fragmentTextualCompareSideBySide.jsp"%>

</div>
