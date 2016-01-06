<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<h3 class="text-center">
    <spring:message code="virtualedition" /> 
    ${edition.title} (${edition.getSortedInterps().size()})
</h3>
<c:if test="${heteronym != null}">
    <h4 class="text-left">
        <spring:message code="tableofcontents.fragmentsof" />
        ${heteronym.name}
    </h4>
</c:if>
<br>
<table id="tablevirtual" data-pagination="false">
<!-- <table class="table table-hover table-condensed"> -->
    <thead>
        <tr>
        	<th><span class="tip" title="<spring:message code="tableofcontents.tt.number" />"><spring:message code="tableofcontents.number" /></span></th>
            <th><span class="tip" title="<spring:message code="tableofcontents.tt.title" />"><spring:message code="tableofcontents.title" /></span></th>
            <th><span class="tip" title="<spring:message code="tableofcontents.tt.taxonomy" />"><spring:message code="general.taxonomy" /></span></th>
            <th><span class="tip" title="<spring:message code="tableofcontents.tt.usesEditions" />"><spring:message code="tableofcontents.usesEditions" /></span></th>
        </tr>
    <tbody>
        <c:forEach var="inter" items='${edition.sortedInterps}'>
            <c:if
                test="${(heteronym == null) || (inter.heteronym == heteronym)}">
                <tr>
                    <td><c:if test="${inter.number!=0}">${inter.number}</c:if></td>
                    <td><a
                        href="${contextPath}/fragments/fragment/inter/${inter.externalId}">${inter.title}</a></td>
                    <td>
                        <c:forEach var="taxonomy" items="${edition.getTaxonomies()}">
                            <a href="${contextPath}/edition/taxonomy/${taxonomy.getExternalId()}">${taxonomy.getName()}</a>
                        </c:forEach>
                    </td>
                    <td><c:forEach var="used"
                            items="${inter.getListUsed()}">-><a
                        href="${contextPath}/fragments/fragment/inter/${used.externalId}">${used.shortName}</a>
                    </c:forEach></td>
                </tr>
            </c:if>
        </c:forEach>
    </tbody>
</table>

<script>
$('#tablevirtual').attr("data-search","true");
$('#tablevirtual').bootstrapTable();
$(".tip").tooltip({placement: 'bottom'});
</script>
