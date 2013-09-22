<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!--
<script type="text/javascript">
$(document)
    .ready(
        function() {
            $(
                '[id="visualisation-properties-virtual"][data-toggle="buttons-checkbox"]')
                .on(
                    'click',
                    function() {
                    var data = new Array();
                    $('#virtualinter :checked').each(function() {
                        data.push(this.value);
                    });
                    var showTags = $(
                        'input:checkbox[name=tags]')
                        .is(':checked');
                    var showAnnotations = $(
                        'input:checkbox[name=annotations]')
                        .is(':checked');
                    var enableEdit = $(
                        'input:checkbox[name=edit]')
                        .is(':checked');
                    alert("Underdevelopment!");
                    });
        });
</script>
 -->
<script type="text/javascript">
jQuery(function ($) {
    var content = $('#content').annotator();
    content.annotator('addPlugin', 'Tags');   
    content.annotator('addPlugin', 'Unsupported'); 
    content.annotator('addPlugin', 'Store', {
      // The endpoint of the store on your server.
      prefix: '${contextPath}/fragments/fragment',

      // Attach the uri of the current page to all annotations to allow search.
      annotationData: {
        'uri': '${inters.get(0).externalId}'
      },

      // This will perform a "search" action rather than "read" when the plugin
      // loads. Will request all the annotations for the current url.
      loadFromSearch: {
        'limit': 0,
        'all_fields': 1,
        'uri': '${inters.get(0).externalId}'
      },
      
      urls: {
        // These are the default URLs.
        create:  '/annotations',
        read:    '/annotations/:id',
        update:  '/annotations/:id',
        destroy: '/annotations/:id',
        search:  '/search'
      }
    });
});
</script>

<div id=fragmentInter class="row-fluid span12">
<!-- 
    <form class="form-horizontal">
        <div class="controls form-inline">
            <div id="visualisation-properties-virtual"
                data-toggle="buttons-checkbox">
                <label class="checkbox inline"> <input
                    type="checkbox" class="btn" name=tags
                    value="Yes"> <spring:message code="general.tags" />
                </label> <label class="checkbox inline"> <input
                    type="checkbox" class="btn" name=annotations
                    value="Yes"> <spring:message
                        code="general.annotations" />
                </label> <label class="checkbox inline"> <input
                    type="checkbox" class="btn" name=edit value="Yes">
                    <spring:message code="general.edit" />
                </label>
            </div>
        </div>
    </form>
-->
    <%@ include file="/WEB-INF/jsp/fragment/transcription.jsp"%>

    <br>
    <div id="interMeta" class="row-fluid">
        <div class="well row-fluid span12">${inters.get(0).metaTextual}</div>
    </div>
</div>