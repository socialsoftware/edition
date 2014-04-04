<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
    <%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

    <div class="container">
        <h3 class="text-center">
            <spring:message code="virtualedition" /> <a href="${contextPath}/edition/internalid/${taxonomy.getEdition().getExternalId()}"> ${taxonomy.getEdition().title}</a>
            <spring:message code="general.taxonomy" />: ${taxonomy.getName()} (${taxonomy.getSortedFragInter().size()})
        </h3>

        <table class="table table-hover table-condensed">
            <thead>
                <tr>
                    <th><spring:message
                            code="tableofcontents.title" /></th>
                    <th><spring:message code="general.category" /></th>
                    <th><spring:message
                            code="tableofcontents.number" /></th>
                    <th><spring:message
                            code="tableofcontents.usesEditions" /></th>
                </tr>
            <tbody>
                <c:forEach var="inter"
                    items='${taxonomy.getSortedFragInter()}'>
                    <tr>
                        <td><a
                            href="${contextPath}/fragments/fragment/inter/${inter.getExternalId()}">${inter.getTitle()}</a></td>
                        <td>
                            <c:forEach var="categoryInFragInter" items='${inter.getSortedCategoryInFragInter(taxonomy)}'> 
                                <a href="${contextPath}/edition/category/${categoryInFragInter.getCategory().getExternalId()}">
                                    ${categoryInFragInter.getCategory().getName()}
                                </a> (${categoryInFragInter.getPercentage()})
                            </c:forEach>
                        </td>
                        <td>${inter.getNumber()}</td>
                        <td><c:forEach var="used"
                                items="${inter.getListUsed()}">-><a
                                    href="${contextPath}/fragments/fragment/inter/${used.externalId}">${used.shortName}</a>
                            </c:forEach></td>
                    </tr>
                </c:forEach>

            </tbody>
        </table>
    </div>
</body>
</html>

