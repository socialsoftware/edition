<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
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
											var selSpaces = $(
											'input:checkbox[name=spaces]')
											.is(':checked');
											$
													.get(
															"${contextPath}/fragments/fragment/interpretation/mode",
															{
																interp : fragInter1,
																interp2Compare : fragInter2,
																line : selLine,
																spaces : selSpaces
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
				<span class="control-label"><spring:message code="fragment.visualization" /></span>
				<div class="controls form-inline">
					<div class="well" id="visualisation-properties-comparison"
						data-toggle="buttons-checkbox">
						<label class="checkbox inline"> <input type="checkbox"
							class="btn" name=line value="Yes"> <spring:message code="fragment.linebyline" />
						</label>
						<label class="checkbox inline"> <input type="checkbox"
							class="btn" name=spaces value="Yes"> <spring:message code="fragment.alignspace" />
						</label>
					</div>
				</div>
			</div>
		</form>
	</div>

	<%@ include file="/WEB-INF/jsp/fragment/textualCompareSideBySide.jsp"%>

</div>
