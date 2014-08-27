<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<script type="text/javascript">
function validateForm() {
    var x = document.forms["updateName"]["name"].value;
    if (x == null || x == "") {
        $("#message").html("A categoria deve ter um nome.");
        $("#myModal").modal('show');
        return false;
    }
}
</script>
<body>
    <%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

    <div class="container">
        <h1 class="text-center">
            <spring:message code="general.edition" />
            : ${category.getTaxonomy().getEdition().getReference()}
            <spring:message code="general.taxonomy" />
            : <a
                href="${contextPath}/virtualeditions/restricted/taxonomy/${category.getTaxonomy().getExternalId()}">${category.getTaxonomy().getName()}</a>
            <spring:message code="general.category" />
            : ${category.getName()}
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
            <c:choose>
                <c:when test="${!category.getDeprecated()}">
                    <c:forEach var="error" items='${errors}'>
                        <div class="row text-error">${error}</div>
                    </c:forEach>
                    <div class="col-md-11">
                        <form name="updateName" class="form-inline"
                            method="POST"
                            action="/virtualeditions/restricted/category"
                            onsubmit="return validateForm()">
                            <div class="form-group">
                                <input type="hidden"
                                    class="form-control"
                                    name="categoryId"
                                    value="${category.externalId}" />
                            </div>
                            <div class="form-group col-md-4">
                                <input type="text" class="form-control"
                                    name="name"
                                    placeholder="<spring:message code="general.name" />"
                                    value="${category.getName()}" />
                            </div>
                            <button type="submit"
                                class="btn btn-primary">
                                <span class="glyphicon glyphicon-edit"></span>
                                <spring:message code="general.update" />
                            </button>
                        </form>
                    </div>
                    <div class="col-md-1">
                        <c:if
                            test="${(category.getType() =='GENERATED') || (category.getType()=='ADHOC') }">
                            <form class="form-inline" method="POST"
                                action="/virtualeditions/restricted/category/delete">
                                <div class="form-group">
                                    <input type="hidden"
                                        class="form-control"
                                        name="categoryId"
                                        value="${category.externalId}" />
                                </div>
                                <button type="submit"
                                    class="btn btn-primary">
                                    <span
                                        class="glyphicon glyphicon-remove"></span>
                                    <spring:message
                                        code="general.delete" />
                                </button>
                            </form>
                        </c:if>
                    </div>
                </c:when>
                <c:otherwise>
                    <b>${category.getName()}</b>
                </c:otherwise>
            </c:choose>
        </div>
        <br />
        <div class="row">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th><spring:message
                                code="general.deprecated" /></th>
                        <th><spring:message code="fragments" /></th>
                        <c:if test="${!category.getDeprecated()}">
                            <th>
                                <form class="form-inline" method="GET"
                                    action="/virtualeditions/restricted/category/extractForm">
                                    <div class="form-group">
                                        <input type="hidden"
                                            class="form-control"
                                            name="categoryId"
                                            value="${category.getExternalId()}" />
                                    </div>
                                    <button type="submit"
                                        class="btn btn-primary">
                                        <span
                                            class="glyphicon glyphicon-edit"></span>
                                        <spring:message
                                            code="general.extract" />
                                    </button>
                                </form>
                            </th>
                        </c:if>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><c:choose>
                                <c:when
                                    test="${category.getDeprecated()}">
                                    <spring:message code="general.yes" />
                                </c:when>
                                <c:otherwise>
                                    <spring:message code="general.no" />
                                </c:otherwise>
                            </c:choose></td>
                        <td><c:choose>
                                <c:when
                                    test="${category.getDeprecated()}">
                                    <c:forEach var="tag"
                                        items='${category.getSortedTags()}'>
                                        <a
                                            href="${contextPath}/virtualeditions/restricted/fraginter/${tag.getFragInter().getExternalId()}">
                                            ${tag.getFragInter().getTitle()}</a> (${tag.getWeight()})
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="tag"
                                        items='${category.getSortedActiveTags()}'>
                                        <a
                                            href="${contextPath}/virtualeditions/restricted/fraginter/${tag.getFragInter().getExternalId()}">
                                            ${tag.getFragInter().getTitle()}</a> (${tag.getWeight()})
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose></td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div class="row">
            <c:choose>
                <c:when test="${category.getType()=='GENERATED'}">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th><spring:message
                                        code="general.words" /></th>
                                <th><spring:message
                                        code="general.category" /></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="fragWordInCategory"
                                items='${category.getSortedFragWordInCategory()}'>
                                <tr>
                                    <td>
                                        ${fragWordInCategory.getFragWord().getWord()}
                                        (${fragWordInCategory.getWeight()})
                                    </td>
                                    <td><c:forEach
                                            var="fragWordInCategory2"
                                            items='${fragWordInCategory.getFragWord().getSortedFragWordInCategory()}'>
                                            <a
                                                href="${contextPath}/virtualeditions/restricted/category/${fragWordInCategory2.getGeneratedCategory().getExternalId()}">${fragWordInCategory2.getGeneratedCategory().getName()}</a>
                                        </c:forEach></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:when test="${category.getType()=='MERGED'}">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th><spring:message
                                        code="general.category.merged" /></th>
                                <th><spring:message
                                        code="general.deprecated" /></th>
                                <th><spring:message
                                        code="fragments" /></th>
                                <c:if
                                    test="${!category.getDeprecated()}">
                                    <th>
                                        <form class="form-inline"
                                            method="POST"
                                            action="/virtualeditions/restricted/category/merge/undo">
                                            <div class="form-group">
                                                <input type="hidden"
                                                    class="form-control"
                                                    name="categoryId"
                                                    value="${category.externalId}" />
                                            </div>
                                            <button type="submit"
                                                class="btn btn-primary">
                                                <span
                                                    class="glyphicon glyphicon-edit"></span>
                                                <spring:message
                                                    code="general.undo" />
                                            </button>
                                        </form>
                                    </th>
                                </c:if>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="mergedCategory"
                                items='${category.getMergedCategoriesSet()}'>
                                <tr>
                                    <td><a
                                        href="${contextPath}/virtualeditions/restricted/category/${mergedCategory.getExternalId()}">
                                            ${mergedCategory.getName()}</a></td>
                                    <td><c:choose>
                                            <c:when
                                                test="${mergedCategory.getDeprecated()}">
                                                <spring:message
                                                    code="general.yes" />
                                            </c:when>
                                            <c:otherwise>
                                                <spring:message
                                                    code="general.no" />
                                            </c:otherwise>
                                        </c:choose></td>
                                    <td><c:forEach var="mergedTag"
                                            items='${mergedCategory.getSortedTags()}'>
                                            <a
                                                href="${contextPath}/virtualeditions/restricted/fraginter/${mergedTag.getFragInter().getExternalId()}">${mergedTag.getFragInter().getTitle()}</a>(${mergedTag.getWeight()})</c:forEach></td>

                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:when test="${category.getType()=='EXTRACTED'}">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th><spring:message
                                        code="general.category.extracted" /></th>
                                <th><spring:message
                                        code="general.deprecated" /></th>
                                <th><spring:message
                                        code="fragments" /></th>
                                <c:if
                                    test="${!category.getDeprecated()}">
                                    <th>
                                        <form class="form-inline"
                                            method="POST"
                                            action="/virtualeditions/restricted/category/extract/undo">
                                            <div class="form-group">
                                                <input type="hidden"
                                                    class="form-control"
                                                    name="categoryId"
                                                    value="${category.externalId}" />
                                            </div>
                                            <button type="submit"
                                                class="btn btn-primary">
                                                <span
                                                    class="glyphicon glyphicon-edit"></span>
                                                <spring:message
                                                    code="general.undo" />
                                            </button>
                                        </form>
                                    </th>
                                </c:if>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <c:set var="origin"
                                    value="${category.getOriginSplitCategory()}" />
                                <td><a
                                    href="${contextPath}/virtualeditions/restricted/category/${origin.getExternalId()}">
                                        ${origin.getName()}</a></td>
                                <td><c:choose>
                                        <c:when
                                            test="${origin.getDeprecated()}">
                                            <spring:message
                                                code="general.yes" />
                                        </c:when>
                                        <c:otherwise>
                                            <spring:message
                                                code="general.no" />
                                        </c:otherwise>
                                    </c:choose></td>
                                <td><c:forEach var="extractedTag"
                                        items='${origin.getSortedTags()}'>
                                        <a
                                            href="${contextPath}/virtualeditions/restricted/fraginter/${extractedTag.getFragInter().getExternalId()}">${extractedTag.getFragInter().getTitle()}</a>(${extractedTag.getWeight()})</c:forEach></td>

                            </tr>
                        </tbody>
                    </table>
                </c:when>
            </c:choose>
        </div>
    </div>
    <!-- Modal HTML -->
    <div id="myModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close"
                        data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">Informação</h4>
                </div>
                <div class="modal-body">
                    <h3 id="message" />
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default"
                        data-dismiss="modal">Fechar</button>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
