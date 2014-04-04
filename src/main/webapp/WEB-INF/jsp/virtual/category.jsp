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
            <spring:message code="general.edition" />: ${category.getTaxonomy().getEdition().getReference()}
            <spring:message code="general.taxonomy" />: <a
                href="${contextPath}/virtualeditions/restricted/taxonomy/${category.getTaxonomy().getExternalId()}">${category.getTaxonomy().getName()}</a>
            <spring:message code="general.category" />: ${category.getName()}
        </h1>
        <h4 class="pull-right">
            <spring:message code="general.public.pages" />
            - <a
                href="${contextPath}/edition/internalid/${category.getTaxonomy().getEdition().getExternalId()}">
                ${category.getTaxonomy().getEdition().getReference()}</a> :
            <a
                href="${contextPath}/edition/taxonomy/${category.getTaxonomy().getExternalId()}">${category.getTaxonomy().getName()}</a>
            : <a
                href="${contextPath}/edition/category/${category.getExternalId()}">${category.getName()}</a>
        </h4>
        <br /> <br />
        <div class="row">
            <form class="form-inline" method="POST"
                action="/virtualeditions/restricted/category">
                <div class="form-group">
                    <input type="hidden" class="form-control"
                        name="categoryId" value="${category.externalId}" />
                </div>
                <div class="form-group col-md-4">
                    <input type="text" class="form-control" name="name"
                        placeholder="<spring:message code="general.name" />"
                        value="${category.getName()}" />
                </div>
                <button type="submit" class="btn btn-primary">
                    <span class="glyphicon glyphicon-edit"></span>
                    <spring:message code="general.update" />
                </button>
            </form>
        </div>
        <br />
        <div class="row">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th><spring:message code="general.words" /></th>
                        <th><spring:message code="fragments" /></th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><c:forEach var="fragWordInCategory"
                                items='${category.getSortedFragWordInCategory()}'> ${fragWordInCategory.getFragWord().getWord()} (${fragWordInCategory.getWeight()})</c:forEach></td>
                        <td><c:forEach var="categoryInFragInter"
                                items='${category.getSortedCategoryInFragInter()}'>
                                <a
                                    href="${contextPath}/virtualeditions/restricted/fraginter/${categoryInFragInter.getFragInter().getExternalId()}">${categoryInFragInter.getFragInter().getTitle()}</a> (${categoryInFragInter.getPercentage()})</c:forEach></td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div class="row">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th><spring:message code="general.words" /></th>
                        <th><spring:message code="general.category" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="fragWordInCategory"
                        items='${category.getSortedFragWordInCategory()}'>
                        <tr>
                            <td>
                                ${fragWordInCategory.getFragWord().getWord()}
                            </td>
                            <td><c:forEach
                                    var="fragWordInCategory2"
                                    items='${fragWordInCategory.getFragWord().getSortedFragWordInCategory()}'>
                                    <a
                                        href="${contextPath}/virtualeditions/restricted/category/${fragWordInCategory2.getCategory().getExternalId()}">${fragWordInCategory2.getCategory().getName()}</a>
                                </c:forEach></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
