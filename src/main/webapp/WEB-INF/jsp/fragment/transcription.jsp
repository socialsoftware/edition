<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<div id="fragmentTranscription" class="row-fluid">
        
	<div class="row-fluid span12">
    <h5>${inter.title}</h5>
    <div class="well">
		<p>${writer.getTranscription(inter)}</p>
        </div>
	</div>
</div>
