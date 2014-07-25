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
            <spring:message code="general.edition" />
            : <a
                href="${contextPath}/edition/internalid/${taxonomy.getEdition().getExternalId()}">${taxonomy.getEdition().getReference()}</a>
            <spring:message code="general.taxonomy" />
            : <a
                href="${contextPath}/edition/taxonomy/${taxonomy.getExternalId()}">${taxonomy.getName()}</a>
        </h1>
        <br /> <br />
        <div class="row">
            <c:if test="${!category.getDeprecated()}">
                <table class="table table-hover">
                    <form class="form-horizontal" role="form"
                        method="POST"
                        action="/virtualeditions/restricted/tag/associate">
                        <div class="form-group">
                            <div class="hidden">
                                <label> <input type="hidden"
                                    name="fragInterId"
                                    value="${fragInter.getExternalId()}">
                                </label>
                            </div>
                        </div>
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
                                <th><spring:message
                                        code="fragments" /></th>
                                <th><div class="form-group">
                                        <button type="submit"
                                            class="btn btn-sm btn-primary">
                                            <spring:message
                                                code="general.associate" /></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="category"
                                items='${categories}'>
                                <tr>

                                    <td><a
                                        href="${contextPath}/edition/category/${category.getExternalId()}">
                                            ${category.getName()}</a></td>
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
                                <td><div class="form-group">
                                        <button type="submit"
                                            class="btn btn-sm btn-primary">
                                            <spring:message
                                                code="general.associate" />
                                        </button></td>
                            </tr>

                        </tbody>
                    </form>
                </table>
            </c:if>
        </div>
    </div>
</body>
</html>
