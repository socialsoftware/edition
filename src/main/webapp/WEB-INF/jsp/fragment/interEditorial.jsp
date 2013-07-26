<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<script type="text/javascript">
$(document)
	.ready(
		function() {
		    $(
			    '[id="visualisation-properties-editorial"][data-toggle="buttons-checkbox"]')
			    .on(
				    'click',
				    function() {
					var data = new Array();
					$('#baseinter :checked').each(function() {
					    data.push(this.value);
					});
					var selDiff = $(
						'input:checkbox[name=diff]')
						.is(':checked');
					$
						.get(
							"${contextPath}/fragments/fragment/inter/editorial",
							{
							    interp : data,
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

<div id=fragmentInter class="row-fluid span12">
    <form class="form-horizontal">
        <div class="controls form-inline">
            <div id="visualisation-properties-editorial"
                data-toggle="buttons-checkbox">
                <label class="checkbox inline"> <input
                    type="checkbox" class="btn" name=diff value="Yes">
                    <spring:message code="fragment.highlightdifferences" />
                </label>
            </div>
        </div>
    </form>

    <%@ include file="/WEB-INF/jsp/fragment/transcription.jsp"%>

    <br>
    <div id="interMeta" class="row-fluid">
        <div class="well row-fluid span12">${inters.get(0).metaTextual}</div>
    </div>
</div>