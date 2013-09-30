<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<script type="text/javascript">
jQuery(function ($) {
    var content = $('#content').annotator({
        readOnly: ${!inters.get(0).getEdition().getParticipantSet().contains(user)}
    });
    content.annotator('addPlugin', 'Filter');   
    content.annotator('addPlugin', 'Tags');   
    content.annotator('addPlugin', 'Permissions', {
        user : '${user.username}', 
        showViewPermissionsCheckbox : 'false',
        showEditPermissionsCheckbox : 'false'
    });   
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
    <h3>${inters.get(0).edition.title}</h3>
    <h4>Uses:
        ${inters.get(0).uses.edition.getReference()}(${inters.get(0).uses.reference})</h4>
    <c:forEach var="tag" items='${inters.get(0).getTagSet()}'>
        <span class="badge">${tag.tag}</span>
    </c:forEach>
    <%@ include file="/WEB-INF/jsp/fragment/transcription.jsp"%>
<!-- 
    <div class="row-fluid">
        <div class="row-fluid span12">
            <table class="table table-striped table-condensed">
                <thead>
                    <tr>
                        <th>Quote</th>
                        <th>Text</th>
                        <th>User</th>
                        <th>Tags</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="annotation"
                        items='${inters.get(0).getAnnotationSet()}'>
                        <tr>
                            <td>${annotation.quote}</td>
                            <td>${annotation.text}</td>
                            <td>${annotation.user.username}</td>
                            <td><c:forEach var="tag"
                                    items='${annotation.getTagSet()}'>
                                    <span class="badge">${tag.tag}</span>
                                </c:forEach></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
 -->
</div>