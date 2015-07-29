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
            <spring:message code="virtual.editions" />
        </h1>
        <br>
        <div class="row">
            <c:forEach var="error" items='${errors}'>
                <div class="row text-error">
                    <spring:message code="${error}" />
                </div>
            </c:forEach>
        </div>
        <div class="row">
            <form class="form-inline" method="POST"
                action="/virtualeditions/restricted/create">
                <div class="form-group">
                    <input type="text" class="form-control"
                        name="acronym"
                        placeholder="<spring:message code="virtualeditionlist.acronym" />"
                        value="${acronym}" />
                </div>
                <div class="form-group">
                    <input type="text" class="form-control" name="title"
                        placeholder="<spring:message code="virtualeditionlist.name" />"
                        value="${title}" />
                </div>
                <div class="form-group">
                    <select class="form-control" name="pub">
                        <c:choose>
                            <c:when test="${pub == false}">
                                <option value="true">
                                    <spring:message
                                        code="general.public" />
                                </option>
                                <option value="false" selected>
                                    <spring:message
                                        code="general.private" />
                                </option>
                            </c:when>
                            <c:otherwise>
                                <option value="true" selected>
                                    <spring:message
                                        code="general.public" />
                                </option>
                                <option value="false">
                                    <spring:message
                                        code="general.private" />
                                </option>
                            </c:otherwise>
                        </c:choose>
                    </select>
                </div>
                <div class="form-group">
                    <select class="form-control" name="use"><option
                            value="no"><spring:message
                                code="tableofcontents.usesEdition" /></option>
                        <c:forEach var='expertEdition'
                            items='${expertEditions}'>
                            <option
                                value='${expertEdition.getExternalId()}'>${expertEdition.getEditor()}</option>
                        </c:forEach>
                        <c:forEach var='virtualEdition'
                            items='${virtualEditions}'>
                            <option
                                value='${virtualEdition.getExternalId()}'>${virtualEdition.getAcronym()}</option>
                        </c:forEach></select>
                </div>
                <button type="submit" class="btn btn-primary">
                    <span class="glyphicon glyphicon-edit"></span>
                    <spring:message code="general.create" />
                </button>
            </form>
        </div>
        <br />
        <div class="row">
            <div>
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th><spring:message
                                    code="virtualeditionlist.acronym" /></th>
                            <th><spring:message
                                    code="virtualeditionlist.name" /></th>
                            <th><spring:message code="general.date" /></th>
                            <th><spring:message
                                    code="general.access" /></th>
                            <th><spring:message
                                    code="general.select" /></th>
                            <th><spring:message code="general.edit" /></th>
                            <th><spring:message
                                    code="general.participants" /></th>
                            <th><spring:message
                                    code="general.taxonomies" /></th>
							<th><spring:message
                                    code="general.recommendations" /></th>
                            <th><spring:message
                                    code="general.delete" /></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="virtualEdition"
                            items='${virtualEditions}'>
                            <tr>
                                <td>${virtualEdition.acronym}</td>
                                <td>${virtualEdition.title}</td>
                                <td>${virtualEdition.getDate().toString("dd-MM-yyyy")}</td>
                                <td><c:choose>
                                        <c:when
                                            test="${virtualEdition.pub}">
                                            <spring:message
                                                code="general.public" />
                                        </c:when>
                                        <c:otherwise>
                                            <spring:message
                                                code="general.private" />
                                        </c:otherwise>
                                    </c:choose></td>
                                <td><form class="form-inline"
                                        method="POST"
                                        action="${contextPath}/virtualeditions/toggleselection">
                                        <input type="hidden"
                                            name="externalId"
                                            value="${virtualEdition.externalId}" />
                                        <button type="submit"
                                            class="btn btn-primary btn-sm">
                                            <span
                                                class="glyphicon glyphicon-check"></span>
                                            <c:choose>
                                                <c:when
                                                    test="${ldoDSession.selectedVEs.contains(virtualEdition)}">
                                                    <spring:message
                                                        code="general.deselect" />
                                                </c:when>
                                                <c:otherwise>
                                                    <spring:message
                                                        code="general.select" />
                                                </c:otherwise>
                                            </c:choose>
                                        </button>
                                    </form></td>
                                <c:choose>
                                    <c:when
                                        test="${virtualEdition.participantSet.contains(user)}">
                                        <td><a class="btn btn-sm"
                                            href="${contextPath}/virtualeditions/restricted/editForm/${virtualEdition.externalId}"><span
                                                class="glyphicon glyphicon-edit"></span>
                                                <spring:message
                                                    code="general.edit" /></a></td>
                                        <td><a class="btn btn-sm"
                                            href="${contextPath}/virtualeditions/restricted/participantsForm/${virtualEdition.externalId}"><span
                                                class="glyphicon glyphicon-user"></span>
                                                <spring:message
                                                    code="general.participants" /></a></td>
                                        <td><a class="btn btn-sm"
                                            href="${contextPath}/virtualeditions/restricted/${virtualEdition.externalId}/taxonomy"><span
                                                class="glyphicon glyphicon-tags"></span>
                                                <spring:message
                                                    code="general.taxonomies" /></a></td>
										<td><a class="btn btn-sm"
                                            href="${contextPath}/recommendation/restricted/${virtualEdition.externalId}"><span
                                                class="glyphicon glyphicon-wrench"></span>
                                                <spring:message
                                                    code="general.recommendations" /></a>
											
										</td>
                                        <td>
                                            <form class="form-inline"
                                                method="POST"
                                                action="${contextPath}/virtualeditions/restricted/delete">
                                                <input type="hidden"
                                                    name="externalId"
                                                    value="${virtualEdition.externalId}" />
                                                <button type="submit"
                                                    class="btn btn-primary btn-sm">
                                                    <span
                                                        class="glyphicon glyphicon-remove"></span>
                                                    <spring:message
                                                        code="general.delete" />
                                                </button>
                                            </form>
                                        </td>
                                    </c:when>
                                    <c:otherwise>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                    </c:otherwise>
                                </c:choose>
                            </tr>

                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>