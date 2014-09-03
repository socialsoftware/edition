<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<script type="text/javascript">
$(document).ready(function() {
    $('[id="backward"][data-toggle="button"]').on('click', function() {
	var data = new Array();
	$('#baseinter :checked').each(function() {
	    data.push(this.value);
	});
	var selDiff = $('input:checkbox[name=diff]').is(':checked');
	var selDel = $('input:checkbox[name=del]').is(':checked');
	var selIns = $('input:checkbox[name=ins]').is(':checked');
	var selSubst = $('input:checkbox[name=subst]').is(':checked');
	var selNotes = $('input:checkbox[name=notes]').is(':checked');
	var selFacs = $('input:checkbox[name=facs]').is(':checked');
	var pbText = $('#backward').val();
	$.get("${contextPath}/fragments/fragment/inter/authorial", {
	    interp : data,
	    diff : selDiff,
	    del : selDel,
	    ins : selIns,
	    subst : selSubst,
	    notes : selNotes,
	    facs : selFacs,
	    pb : pbText
	}, function(html) {
	    $("#fragmentTranscription").replaceWith(html);
	});
    });
});
</script>
<script type="text/javascript">
$(document).ready(function() {
    $('[id="forward"][data-toggle="button"]').on('click', function() {
	var data = new Array();
	$('#baseinter :checked').each(function() {
	    data.push(this.value);
	});
	var selDiff = $('input:checkbox[name=diff]').is(':checked');
	var selDel = $('input:checkbox[name=del]').is(':checked');
	var selIns = $('input:checkbox[name=ins]').is(':checked');
	var selSubst = $('input:checkbox[name=subst]').is(':checked');
	var selNotes = $('input:checkbox[name=notes]').is(':checked');
	var selFacs = $('input:checkbox[name=facs]').is(':checked');
	var pbText = $('#forward').val();
	$.get("${contextPath}/fragments/fragment/inter/authorial", {
	    interp : data,
	    diff : selDiff,
	    del : selDel,
	    ins : selIns,
	    subst : selSubst,
	    notes : selNotes,
	    facs : selFacs,
	    pb : pbText
	}, function(html) {
	    $("#fragmentTranscription").replaceWith(html);
	});
    });
});
</script>
<div id="fragmentTranscription" class="row col-md-12">
    <div class="row">
        <h4>${inters.get(0).title}</h4>
        <div class="row">
            <div class="col-md-6">
                <c:choose>
                    <c:when test="${prevsurface != null}">
                        <button class="btn btn-sm pull-left"
                            type="button" id="backward"
                            value="${prevpb.externalId}"
                            data-toggle="button">
                            <span class="glyphicon glyphicon-backward"></span>
                        </button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-sm pull-left"
                            type="button" id="backward" disabled>
                            <span class="glyphicon glyphicon-backward"></span>
                        </button>
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${nextsurface != null}">
                        <button class="btn btn-sm pull-right"
                            type="button" id="forward"
                            value="${nextpb.externalId}"
                            data-toggle="button">
                            <span class="glyphicon glyphicon-forward"></span>
                        </button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-sm pull-right"
                            type="button" id="forward" disabled>
                            <span class="glyphicon glyphicon-forward"></span>
                        </button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <div id="facsimileTranscription" class="row">
            <div class="col-md-6">
                <div class="item">
                    <img src="/facs/${surface.graphic}"
                        class="img-responsive" alt="Responsive image" />
                </div>
            </div>
            <c:choose>
                <c:when
                    test="${inters.get(0).lastUsed.sourceType=='EDITORIAL'}">
                    <div class="well col-md-6" id="content"
                        style="font-family: georgia;">
                        <p>${writer.getTranscription()}</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="well col-md-6" id="content"
                        style="font-family: courier;">
                        <p>${writer.getTranscription()}</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="row">
            <div class="col-md-6">
                <c:choose>
                    <c:when test="${prevsurface != null}">
                        <button class="btn btn-sm pull-left"
                            type="button" id="backward"
                            value="${prevpb.externalId}"
                            data-toggle="button">
                            <span class="glyphicon glyphicon-backward"></span>
                        </button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-sm pull-left"
                            type="button" id="backward" disabled>
                            <span class="glyphicon glyphicon-backward"></span>
                        </button>
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${nextsurface != null}">
                        <button class="btn btn-sm pull-right"
                            type="button" id="forward"
                            value="${nextpb.externalId}"
                            data-toggle="button">
                            <span class="glyphicon glyphicon-forward"></span>
                        </button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-sm pull-right"
                            type="button" id="forward" disabled>
                            <span class="glyphicon glyphicon-forward"></span>
                        </button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>