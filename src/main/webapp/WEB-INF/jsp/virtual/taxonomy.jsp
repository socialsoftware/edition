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
            <spring:message code="general.edition" /> <a
                href="${contextPath}/virtualeditions/restricted/${taxonomy.getEdition().getExternalId()}/taxonomy">
            : ${taxonomy.getEdition().getReference()}</a>
            <spring:message code="general.taxonomy" />
            : ${taxonomy.getName()}
        </h1>
        <h4 class="pull-right">
            <spring:message code="general.public.pages" />
            - <a
                href="${contextPath}/edition/internalid/${taxonomy.getEdition().getExternalId()}">
                ${taxonomy.getEdition().getReference()}</a> : <a
                href="${contextPath}/edition/taxonomy/${taxonomy.getExternalId()}">${taxonomy.getName()}</a>
        </h4>
        <br />
        <div class="row">
            <table class="table table-hover">
                <form class="form-horizontal" role="form" method="POST"
                    action="/virtualeditions/restricted/category/merge">
                    <div class="form-group">
                        <div class="hidden">
                            <label> <input type="hidden"
                                name="taxonomyId"
                                value="${taxonomy.getExternalId()}">
                            </label>
                        </div>
                    </div>
                <thead>
                    <tr>
                        <th><spring:message code="general.category" /></th>
                        <th><spring:message code="fragments" /></th>
                        <th>
                            <div class="form-group">
                                <button type="submit"
                                    class="btn btn-sm btn-primary"><spring:message code="general.merge"/></button>
                            </div>
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="category"
                        items='${taxonomy.getActiveCategorySet()}'>
                        <tr>
                            <td><a
                                href="${contextPath}/virtualeditions/restricted/category/${category.getExternalId()}">${category.getName()}</a></td>
                            <td><c:forEach var="tag"
                                    items='${category.getSortedActiveTags()}'>
                                    <a
                                        href="${contextPath}/virtualeditions/restricted/fraginter/${tag.getFragInter().getExternalId()}">${tag.getFragInter().getTitle()}</a> (${tag.getWeight()})</c:forEach></td>
                            <td class="col-centered">
                                <div class="form-group">
                                    <div class="checkbox">
                                        <label> <input
                                            type="checkbox"
                                            name="categories[]"
                                            value="${category.getExternalId()}">
                                        </label>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td></td>
                        <td></td>
                        <td><div class="form-group">
                                <button type="submit"
                                    class="btn btn-sm btn-primary"><spring:message code="general.merge"/></button></td>
                    </tr>
                </tbody>
                </form>
            </table>
        </div>
    </div>
</body>
</html>
