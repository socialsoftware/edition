<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<script type="text/javascript"
	src="/resources/js/openseadragon/openseadragon.js">
<script type="text/javascript">
/*$(document).ready(function() {
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
*/
</script>
 
<script type="text/javascript">
/*$(document).ready(function() {
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
*/
</script>


<script type="text/javascript">
   var viewer = OpenSeadragon({
        id: "fac",
        prefixUrl: "/resources/img/openseadragon/images/",
        autoHideControls: false,
        visibilityRatio:1.0,
        constrainDuringPan:true,
        showNavigator:  true,
        sequenceMode: true,
        tileSources: [
                      <c:forEach items="${inters.get(0).getSource().getFacsimile().getSurfaces()}" var="surface">
                  	  {type: 'image',url:  '/facs/${surface.graphic}'},
                  	  </c:forEach>
                      ]
    });
   
    $('div[title="Next page"]').on('click', function() {
    		var pbText = $('#fw').val();
   	
    		if (pbText!="") {
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
    	    			var elements = $(html);
				$("#content").html($('#content', elements).html())
				$("#fw").val($('#fw', elements).val());
				$("#bw").val($('#bw', elements).val());
    			});
    		}
    });
    
    
	$("div[title=\'Previous page\']").on('click', function() {
		var pbText = $('#bw').val();
		
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
    	    		var elements = $(html);
			$("#content").html($('#content', elements).html())
			$("#fw").val($('#fw', elements).val());
			$("#bw").val($('#bw', elements).val());
    		});
    });
</script>


<div id="fragmentTranscription">
	<h4 class="text-center">${inters.get(0).title}</h4>
	
	<input type="hidden" id="fw" value="${nextpb.externalId}"> <input
		type="hidden" id="bw" value="${prevpb.externalId}"> <br>
	<div id="facsimileTranscription" class="row" style="margin-right: 0px">
		<div class="col-md-6">
			<!-- 
            	<div style="display:block;position:absolute;">
            	
            	<c:choose>
                    <c:when test="${prevsurface != null}">
                        <button class="btn btn-sm pull-left"
                            type="button" id="backward"
                            value="${prevpb.externalId}"
                            data-toggle="button">
                            <span class="glyphicon glyphicon-arrow-left"></span>
                        </button>  
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-sm pull-left"
                            type="button" id="backward" disabled>
                            <span class="glyphicon glyphicon-arrow-left"></span>
                        </button>
                    </c:otherwise>
                </c:choose>
                
                
                <c:choose>
                    <c:when test="${nextsurface != null}">
                        <button class="btn btn-sm pull-right"
                            type="button" id="forward"
                            value="${nextpb.externalId}"
                            data-toggle="button">
                            <span class="glyphicon glyphicon-arrow-right"></span>
                        </button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-sm pull-right"
                            type="button" id="forward" disabled>
                            <span class="glyphicon glyphicon-arrow-right"></span>
                        </button>
                    </c:otherwise>
                </c:choose>
                
                </div>
                -->
			<div class="item" id="fac" style="width: 100%; height: 554px;">
			</div>
			<!-- 
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
                 -->
		</div>
		<c:choose>
			<c:when test="${inters.get(0).lastUsed.sourceType=='EDITORIAL'}">
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
</div>


