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
            <a
                href="${contextPath}/edition//internalid/${fragInter.getEdition().getExternalId()}">
                ${fragInter.getEdition().getReference()}</a> : <a
                href="${contextPath}/fragments/fragment/inter/${fragInter.getExternalId()}">
                ${fragInter.getTitle()}</a>
        </h1>
        <br />
        <div class="row">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th><spring:message code="general.taxonomy" /></th>
                        <th><spring:message code="general.category" /></th>
                        <th><spring:message code="general.words" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="categoryInFragInter"
                        items='${fragInter.getCategoryInFragInter()}'>
                        <tr>
                            <td><a
                                href="${contextPath}/virtualeditions/restricted/taxonomy/${categoryInFragInter.getCategory().getTaxonomy().getExternalId()}">${categoryInFragInter.getCategory().getTaxonomy().getName()}</a>
                            </td>
                            <td><a
                                href="${contextPath}/virtualeditions/restricted/category/${categoryInFragInter.getCategory().getExternalId()}">${categoryInFragInter.getCategory().getName()}</a>
                                (${categoryInFragInter.getPercentage()})
                            </td>
                            <td><c:forEach var="fragWordInCategory"
                                    items='${categoryInFragInter.getCategory().getSortedFragWordInCategory()}'> ${fragWordInCategory.getFragWord().getWord()} (${fragWordInCategory.getWeight()})
                                </c:forEach></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
