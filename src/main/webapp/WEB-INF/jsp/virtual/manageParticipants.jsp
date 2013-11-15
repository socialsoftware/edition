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
            <spring:message code="virtualedition" />
            : ${virtualedition.title}<br>
            <spring:message code="participant.manage" />
            <br>
        </h1>
        <br>

        <div class="row">
            <c:forEach var="error" items='${errors}'>
                <div class="row text-error">
                    <spring:message code="${error}" />
                </div>
            </c:forEach>
            <form class="form-inline" method="POST"
                action="/virtualeditions/restricted/addparticipant">
                <fieldset>
                    <input type="hidden" name="externalId"
                        id="externalId"
                        value="${virtualedition.externalId}" /> <input
                        type="text" class="username" name="username"
                        id="username"
                        placeholder="<spring:message code="login.username" />"
                        value="${username}" /></label>
                    <button type="submit" class="btn btn-sm">
                        <span class="glyphicon glyphicon-plus"></span>
                        <spring:message code="general.add" />
                    </button>
                </fieldset>
            </form>
        </div>
        <div class="row">
            <table
                class="table table-striped table-bordered table-condensed">
                <thead>
                    <tr>
                        <th><spring:message code="login.username" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="participant"
                        items='${virtualedition.participant}'>
                        <tr>
                            <td>
                                <form class="form-inline" method="POST"
                                    action="${contextPath}/virtualeditions/restricted/removeparticipant">
                                    ${participant.username} <input
                                        type="hidden" name="veId"
                                        value="${virtualedition.externalId}" />
                                    <input type="hidden" name="userId"
                                        value="${participant.externalId}" />
                                    <button type="submit"
                                        class="btn btn-sm pull-right">
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

    </div>
</body>
</html>
