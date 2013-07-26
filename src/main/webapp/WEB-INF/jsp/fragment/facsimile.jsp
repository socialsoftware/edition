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
	var surface = $('#backward').val();
	$.get("${contextPath}/fragments/fragment/inter/authorial", {
	    interp : data,
	    diff : selDiff,
	    del : selDel,
	    ins : selIns,
	    subst : selSubst,
	    notes : selNotes,
	    facs : selFacs,
	    surf : surface
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
	var surface = $('#forward').val();
	$.get("${contextPath}/fragments/fragment/inter/authorial", {
	    interp : data,
	    diff : selDiff,
	    del : selDel,
	    ins : selIns,
	    subst : selSubst,
	    notes : selNotes,
	    facs : selFacs,
	    surf : surface
	}, function(html) {
	    $("#fragmentTranscription").replaceWith(html);
	});
    });
});
</script>
<div id="fragmentTranscription" class="row-fluid">
    <div class="row-fluid span12">
        <h4>${inters.get(0).title}</h4>
        <div class="row-fluid">
            <div class="span6">
                <c:choose>
                    <c:when test="${surface.prev != null}">
                        <button class="btn pull-left" type="button"
                            id="backward"
                            value="${surface.prev.externalId}"
                            data-toggle="button">
                            <i class="icon-backward"></i>
                        </button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn pull-left" type="button"
                            id="backward" disabled>
                            <i class="icon-backward"></i>
                        </button>
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${surface.next != null}">
                        <button class="btn pull-right" type="button"
                            id="forward"
                            value="${surface.next.externalId}"
                            data-toggle="button">
                            <i class="icon-forward"></i>
                        </button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn pull-right" type="button"
                            id="forward" disabled>
                            <i class="icon-forward"></i>
                        </button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <div id="facsimileTranscription" class="row-fluid">
            <div class="span6">
                <div class="item">
                    <img src="/facs/${surface.graphic}" />
                </div>
            </div>
            <div class="well span6">
                <p>${writer.getTranscription(inter)}</p>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <c:choose>
                    <c:when test="${surface.prev != null}">
                        <button class="btn pull-left" type="button"
                            id="backward"
                            value="${surface.prev.externalId}"
                            data-toggle="button">
                            <i class="icon-backward"></i>
                        </button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn pull-left" type="button"
                            id="backward" disabled>
                            <i class="icon-backward"></i>
                        </button>
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${surface.next != null}">
                        <button class="btn pull-right" type="button"
                            id="forward"
                            value="${surface.next.externalId}"
                            data-toggle="button">
                            <i class="icon-forward"></i>
                        </button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn pull-right" type="button"
                            id="forward" disabled>
                            <i class="icon-forward"></i>
                        </button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>