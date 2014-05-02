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
            <spring:message code="general.taxonomies" />
            : <a
                href="${contextPath}/edition/acronym/${virtualEdition.acronym}">
                ${virtualEdition.title}</a>
            
            <br>
        </h1>
        <br>
        <div class="row pull-right">
            <form class="form-inline" method="POST"
                action="${contextPath}/virtualeditions/restricted/regenerateCorpus">
                <input type="hidden" class="form-control"
                    name="externalId"
                    value="${virtualEdition.externalId}" />
                <button type="submit" class="btn btn-primary">
                    <span class="glyphicon glyphicon-file"></span>
                    <spring:message code="corpus.regenerate" />
                </button>
            </form>
        </div>
        <br /> <br /> <br />
        <div class="row">
            <form class="form-inline" method="POST"
                action="/virtualeditions/restricted/taxonomy/createTopics">
                <div class="form-group">
                    <input type="hidden" class="form-control"
                        name="externalId"
                        value="${virtualEdition.externalId}" />
                </div>
                <div class="form-group">
                    <input type="text" class="form-control" name="name"
                        placeholder="<spring:message code="general.name" />">
                </div>
                <div class="form-group">
                    <input type="text" class="form-control"
                        name="numTopics" placeholder="<spring:message code="general.taxonomies.number.topics" />">
                </div>
                <div class="form-group">
                    <input type="text" class="form-control"
                        name="numWords" placeholder="<spring:message code="general.taxonomies.number.words" />">
                </div>
                <div class="form-group">
                    <input type="text" class="form-control"
                        name="thresholdCategories" placeholder="<spring:message code="general.taxonomies.threshold.categories" />">
                </div>
                <div class="form-group">
                    <input type="text" class="form-control"
                        name="numIterations"
                        placeholder="<spring:message code="general.taxonomies.number.iterations" />">
                </div>
                <button type="submit" class="btn btn-primary">
                    <span class="glyphicon glyphicon-plus"></span>
                    <spring:message code="general.create" />
                </button>
            </form>
        </div>
        <br />
        <div class="row">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th><spring:message code="general.name" /></th>
                        <th><spring:message code="general.taxonomies.number.topics" /></th>
                        <th><spring:message code="general.delete" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="taxonomy"
                        items='${virtualEdition.getTaxonomiesSet()}'>
                        <tr>
                            <td><a
                                href="${contextPath}/virtualeditions/restricted/taxonomy/${taxonomy.getExternalId()}">${taxonomy.getName()}</a>
                                <c:if test="${!taxonomy.getAdHoc()}">(${taxonomy.getNumTopics()},${taxonomy.getNumWords()},${taxonomy.getThresholdCategories()},${taxonomy.getNumIterations()})</c:if>
                            </td>
                            <td>${taxonomy.getActiveCategorySet().size()}</td>
                            <td>
                                <form class="form-inline" method="POST"
                                    action="${contextPath}/virtualeditions/restricted/taxonomy/delete">
                                    <input type="hidden"
                                        name="virtualEditionExternalId"
                                        value="${taxonomy.getEdition().getExternalId()}" />
                                    <input type="hidden"
                                        name="taxonomyExternalId"
                                        value="${taxonomy.externalId}" />
                                    <button type="submit"
                                        class="btn btn-primary btn-sm">
                                        <span
                                            class="glyphicon glyphicon-remove"></span>
                                        <spring:message
                                            code="general.delete" />
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <br />
        <div class="row pull-right">
            <form class="form-inline" method="GET"
                action="${contextPath}/virtualeditions">
                <button type="submit" class="btn btn-primary">
                    <span class="glyphicon glyphicon-th-list"></span>
                    <spring:message code="virtual.editions" />
                </button>
            </form>
        </div>
    </div>
</body>
</html>
