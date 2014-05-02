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

<div id=fragmentInter class="row">
    <h3>${inters.get(0).edition.title}</h3>
    <h4>
        <spring:message code="general.uses" />
        ${inters.get(0).uses.edition.getReference()}(${inters.get(0).uses.reference})
    </h4>

    <%@ include file="/WEB-INF/jsp/fragment/transcription.jsp"%>

    <div class="row">
        <table class="table table-hover">
            <thead>

                <tr>
                    <th><spring:message code="general.taxonomy" /></th>
                    <th><span class="glyphicon glyphicon-tag"></span></th>
                    <th><span class="glyphicon glyphicon-user"></span></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="taxonomy"
                    items='${inters.get(0).getEdition().getTaxonomiesSet()}'>
                    <tr>
                        <td><a
                            href="${contextPath}/edition/taxonomy/${taxonomy.externalId}">${taxonomy.getName()}</a>
                        </td>
                        <td><c:forEach var="tag"
                                items='${taxonomy.getSortedActiveTags(inters.get(0))}'>
                                <a
                                    href="${contextPath}/edition/category/${tag.getActiveCategory().getExternalId()}">
                                    ${tag.getActiveCategory().getName()}</a>
                                (${tag.getWeight()})</c:forEach></td>
                        <td><c:forEach var="user"
                                items='${inters.get(0).getTagContributorSet(taxonomy)}'>
                                <a
                                    href="${contextPath}/edition/user/${user.username}">${user.username}</a>
                            </c:forEach></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>