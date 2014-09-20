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
                href="${contextPath}/edition/acronym/${virtualEdition.acronym}">
                ${virtualEdition.title}</a> <br>
        </h1>
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

        <br />
        <br />
        <br />
        <div class="row col-md-12 has-error">
            <c:forEach var="error" items='${errors}'>
                <div class="row">
                    <spring:message code="${error}" />
                </div>
            </c:forEach>
        </div>
        <div class="row col-md-12">
            <form class="form-inline" role="form" method="POST"
                action="/virtualeditions/restricted/edit/${externalId}">
                <div class="form-group col-xs-2">
                    <label class="control-label for="acronym"><spring:message
                            code="virtualeditionlist.acronym" /></label> <input
                        type="text" class="form-control" name="acronym"
                        id="acronym"
                        placeholder="<spring:message code="virtualeditionlist.acronym" />"
                        value="${acronym}" />
                </div>
                <div class="form-group  col-xs-4">
                    <label class="control-label" for="title"><spring:message
                            code="virtualeditionlist.name" /></label> <input
                        type="text" class="form-control" name="title"
                        id="title"
                        placeholder="<spring:message code="virtualeditionlist.name" />"
                        value="${title}" />
                </div>
                <div class="form-group col-xs-2">
                    <label class="control-label" for="date"><spring:message
                            code="general.date" /></label> <input
                        class="form-control  col-xs-2"
                        id="disabledInput" type="text" name="date"
                        id="date" value="${date}" disabled />
                </div>
                <div class="form-group  col-xs-2">
                    <label class="control-label" for="pub"><spring:message
                            code="general.access" /></label> <select
                        class="form-control" name="pub" id="pub">
                        <c:choose>
                            <c:when test="${pub == false}">
                                <option value="true">
                                    <spring:message
                                        code="general.public" />
                                </option>
                                <option value="false" selected><spring:message
                                        code="general.private" /></option>
                            </c:when>
                            <c:otherwise>
                                <option value="true" selected><spring:message
                                        code="general.public" /></option>
                                <option value="false"><spring:message
                                        code="general.private" /></option>
                            </c:otherwise>
                        </c:choose>
                    </select>
                </div>
                <br /> <label class="sr-only" for=submit><spring:message
                        code="general.update" /></label>
                <button type="submit" class="btn btn-primary"
                    id="submit">
                    <span class="glyphicon glyphicon-edit"></span>
                    <spring:message code="general.update" />
                </button>
            </form>
        </div>
    </div>
</body>
</html>
