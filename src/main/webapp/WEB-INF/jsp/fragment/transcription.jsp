<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<div id="fragmentTranscription" class="row-fluid">
        
	<div class="row-fluid span12">
    <h4 class="text-center">${inters.get(0).title}</h4>
    <div class="well">
		<p>${writer.getTranscription(inter)}</p>
        </div>
	</div>
</div>
