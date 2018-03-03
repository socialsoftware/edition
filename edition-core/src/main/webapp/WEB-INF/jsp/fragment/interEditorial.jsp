<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<script type="text/javascript">
	$(document).ready(function() {
		$('[id="visualisation-properties-editorial"][data-toggle="checkbox"]').on('click', function() {
			var data = new Array();
			$('#baseinter :checked').each(function() {
				data.push(this.value);
			});
			var selDiff = $('input:checkbox[name=diff]').is(':checked');
			$.get("${contextPath}/fragments/fragment/inter/editorial", {
				interp : data,
				diff : selDiff
			}, function(html) {
				$("#fragmentTranscription").replaceWith(html);
			});
		});
	});
</script>

<div id=fragmentInter class="row">
	<form class="form-inline" role="form">
		<div class="form-group">
			<div id="visualisation-properties-editorial" class="btn-group"
				data-toggle="checkbox">
				<div class="checkbox">
					<label> <input type="checkbox" name=diff value="Yes">
						<spring:message code="fragment.highlightdifferences" />
					</label>
				</div>
			</div>
		</div>
	</form>
	<br>
	<%@ include file="/WEB-INF/jsp/fragment/transcription.jsp"%>

	<c:set var="inter" value="${inters.get(0)}" />
	<br>
	<div class="well">
		<%@ include file="/WEB-INF/jsp/fragment/interMetaInfo.jsp"%>
	</div>
</div>