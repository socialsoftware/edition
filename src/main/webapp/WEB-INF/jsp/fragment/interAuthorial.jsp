<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<script type="text/javascript">
$(document)
	.ready(
		function() {
		    $(
			    '[id="visualisation-properties-authorial"][data-toggle="checkbox"]')
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
					var selDel = $(
						'input:checkbox[name=del]').is(
						':checked');
					var selIns = $(
						'input:checkbox[name=ins]').is(
						':checked');
					var selSubst = $(
						'input:checkbox[name=subst]')
						.is(':checked');
					var selNotes = $(
						'input:checkbox[name=notes]')
						.is(':checked');
					var selFacs = $(
						'input:checkbox[name=facs]')
						.is(':checked');
					$
						.get(
							"${contextPath}/fragments/fragment/inter/authorial",
							{
							    interp : data,
							    diff : selDiff,
							    del : selDel,
							    ins : selIns,
							    subst : selSubst,
							    notes : selNotes,
							    facs : selFacs
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

<div id=fragmentInter class="row">
    <form class="form-inline" role="form">
        <div class="form-group">
            <div id="visualisation-properties-authorial"
                class="btn-group" data-toggle="checkbox">
                <div class="checkbox">
                    <label> <input type="checkbox" class="btn"
                        name=diff value="Yes"> <spring:message
                            code="fragment.highlightdifferences" />
                    </label>
                </div>
                <div class="checkbox">
                    <label> <input type="checkbox" class="btn"
                        name=del value="Yes"> <spring:message
                            code="fragment.showdeleted" />
                    </label>
                </div>
                <div class="checkbox">
                    <label> <input type="checkbox" class="btn"
                        name=ins value="Yes" checked> <spring:message
                            code="fragment.highlightinserted" />
                    </label>
                </div>
                <div class="checkbox">
                    <label> <input type="checkbox" class="btn"
                        name=subst value="Yes"> <spring:message
                            code="fragment.highlightsubstitutions" />
                    </label>
                </div>
                <div class="checkbox">
                    <label> <input type="checkbox" class="btn"
                        name=notes value="Yes" checked> <spring:message
                            code="fragment.shownotes" />
                    </label>
                </div>
                <div class="checkbox">
                    <label> <input type="checkbox" class="btn"
                        name=facs value="Yes"> <spring:message
                            code="fragment.showfacs" />
                    </label>
                </div>
            </div>
        </div>
    </form>


    <%@ include file="/WEB-INF/jsp/fragment/transcription.jsp"%>

    <br>
    <div id="interMeta" class="row">
        <div class="well row">${inters.get(0).metaTextual}</div>
    </div>
</div>