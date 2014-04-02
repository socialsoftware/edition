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
    <div class="col-md-6">
        <c:forEach var="tag" items='${inters.get(0).getTagSet()}'>
            <span class="glyphicon glyphicon-tag"></span>
            <a href="${contextPath}/edition/tag/${tag.tag}">${tag.tag}</a>
        </c:forEach>
    </div>
    <div>
        <c:forEach var="user"
            items='${inters.get(0).getContributorSet()}'>
            <span class="glyphicon glyphicon-user"></span>
            <a href="${contextPath}/edition/user/${user.username}">${user.username}</a>
        </c:forEach>
    </div>
    <br />

    <%@ include file="/WEB-INF/jsp/fragment/transcription.jsp"%>

    <div class="row">
        <table class="table table-hover">
            <thead>

                <tr>
                    <th><spring:message code="general.taxonomy" /></th>
                    <th><spring:message code="general.category" /></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="taxonomy"
                    items='${inters.get(0).getEdition().getTaxonomiesSet()}'>
                    <tr>
                        <td><a
                            href="${contextPath}/edition/taxonomy/${taxonomy.externalId}">${taxonomy.getName()}</a>
                        </td>
                        <td><c:forEach var="categoryInFragInter"
                                items='${taxonomy.getSortedCategoryInFragInter(inters.get(0))}'>
                                <a
                                    href="${contextPath}/edition/category/${categoryInFragInter.getCategory().getExternalId()}">${categoryInFragInter.getCategory().getName()}</a>
                                (${categoryInFragInter.getPercentage()})</c:forEach>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    <!-- 
    <div class="row">
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