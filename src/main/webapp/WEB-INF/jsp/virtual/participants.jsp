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
            <spring:message code="general.participants" />
            : <a
                href="${contextPath}/edition/acronym/${virtualedition.acronym}">
                ${virtualedition.title}</a> <br>
        </h1>
        <br>

        <c:forEach var="error" items='${errors}'>
            <div class="row text-error">
                <spring:message code="${error}" />
            </div>
        </c:forEach>
        <div class="row col-md-10">
            <form class="form-inline" method="POST"
                action="${contextPath}/virtualeditions/restricted/addparticipant">
                <div class="form-group">
                    <input type="hidden" class="form-control"
                        name="externalId"
                        value="${virtualedition.externalId}" />
                </div>
                <div class="form-group">
                    <input type="text" class="form-control"
                        name="username"
                        placeholder="<spring:message code="login.username" />"
                        value="${username}" />
                </div>
                <button type="submit" class="btn btn-primary">
                    <span class="glyphicon glyphicon-plus"></span>
                    <spring:message code="general.add" />
                </button>
            </form>
        </div>
        <div class="row col-md-2 pull-right">
            <form class="form-inline" method="GET"
                action="${contextPath}/virtualeditions">
                <button type="submit" class="btn btn-primary">
                    <span class="glyphicon glyphicon-th-list"></span>
                    <spring:message code="virtual.editions" />
                </button>
            </form>
        </div>

        <br />
        <div class="row">
            <table class="table table-hover">
                <caption>
                    <h2>
                        <spring:message code="participantsForm.message1" />
                    </h2>
                </caption>
                <thead>
                    <tr>
                        <th><spring:message code="login.username" /></th>
                        <th><spring:message code="user.firstName" /></th>
                        <th><spring:message code="user.lastName" /></th>
                        <th>email</th>
                        <th><spring:message code="general.remove" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="participant"
                        items='${virtualedition.getParticipantSet()}'>
                        <tr>
                            <td><span
                                class="glyphicon glyphicon-user"></span>
                                <a
                                href="${contextPath}/edition/user/${participant.username}">${participant.username}</a></td>
                            <td>${participant.getFirstName()}</td>
                            <td>${participant.getLastName()}</td>
                            <td><a
                                href="mailto:${participant.getEmail()}">${participant.getEmail()}</a></td>
                            <td>
                                <form class="form-inline" method="POST"
                                    action="${contextPath}/virtualeditions/restricted/removeparticipant">
                                    <input type="hidden" name="veId"
                                        value="${virtualedition.externalId}" />
                                    <input type="hidden" name="userId"
                                        value="${participant.externalId}" />
                                    <button type="submit"
                                        class="btn btn-primary btn-sm">
                                        <span
                                            class="glyphicon glyphicon-remove"></span>
                                        <spring:message
                                            code="general.remove" />
                                    </button>
                                </form>
                            </td>
                        </tr>

                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="row">
            <table class="table table-hover">
                <caption>
                    <h2>
                        <spring:message code="participantsForm.message2" />
                    </h2>
                </caption>
                <thead>
                    <tr>
                        <th><spring:message code="login.username" /></th>
                        <th><spring:message code="user.firstName" /></th>
                        <th><spring:message code="user.lastName" /></th>
                        <th>email</th>
                        <th><spring:message code="general.add" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="user" items='${users}'>
                        <c:if
                            test="${!virtualedition.getParticipantSet().contains(user)}">
                            <tr>
                                <td><span
                                    class="glyphicon glyphicon-user"></span>
                                    <a
                                    href="${contextPath}/edition/user/${user.username}">${user.username}</a></td>
                                <td>${user.getFirstName()}</td>
                                <td>${user.getLastName()}</td>
                                <td><a
                                    href="mailto:${user.getEmail()}">${user.getEmail()}</a></td>
                            <td>
                                <form class="form-inline" method="POST"
                                    action="${contextPath}/virtualeditions/restricted/addparticipant">
                                    <input type="hidden" name="externalId"
                                        value="${virtualedition.externalId}" />
                                    <input type="hidden" name="username"
                                        value="${user.username}" />
                                    <button type="submit"
                                        class="btn btn-primary btn-sm">
                                        <span
                                            class="glyphicon glyphicon-remove"></span>
                                        <spring:message
                                            code="general.add" />
                                    </button>
                                </form>
                            </td>
                            </tr>
                        </c:if>
                    </c:forEach>
                </tbody>
            </table>
        </div>

    </div>
</body>
</html>
