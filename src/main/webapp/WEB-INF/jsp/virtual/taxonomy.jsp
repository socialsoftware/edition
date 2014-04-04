<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
    <%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

    <div class="container">
        <h1 class="text-center">
            <spring:message code="general.edition" />: ${taxonomy.getEdition().getReference()}
            <spring:message code="general.taxonomy" />: ${taxonomy.getName()}
        </h1>
        <h4 class="pull-right">
            <spring:message code="general.public.pages" />
            - <a
                href="${contextPath}/edition/internalid/${taxonomy.getEdition().getExternalId()}">
                ${taxonomy.getEdition().getReference()}</a> :
            <a
                href="${contextPath}/edition/taxonomy/${taxonomy.getExternalId()}">${taxonomy.getName()}</a>
        </h4>
        <br />
        <div class="row">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th><spring:message code="general.category" /></th>
                        <th><spring:message code="general.words" /></th>
                        <th><spring:message code="fragments" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="category"
                        items='${taxonomy.getCategoriesSet()}'>
                        <tr>
                            <td><a
                                href="${contextPath}/virtualeditions/restricted/category/${category.getExternalId()}">${category.getName()}</a></td>
                            <td><c:forEach var="fragWordInCategory"
                                    items='${category.getSortedFragWordInCategory()}'> ${fragWordInCategory.getFragWord().getWord()} (${fragWordInCategory.getWeight()})</c:forEach></td>
                            <td><c:forEach
                                    var="categoryInFragInter"
                                    items='${category.getSortedCategoryInFragInter()}'>
                                    <a
                                        href="${contextPath}/virtualeditions/restricted/fraginter/${categoryInFragInter.getFragInter().getExternalId()}">${categoryInFragInter.getFragInter().getTitle()}</a> (${categoryInFragInter.getPercentage()})</c:forEach></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
