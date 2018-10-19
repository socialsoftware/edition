<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>
<h3 class="text-center">
    <spring:message code="general.classificationGame" />:
    ${game.getVirtualEditionInter().getTitle()}
</h3>
<br />
<h4 class="text-center">
    <spring:message code="general.winner" />:
    ${game.getTag().getContributor().getUsername()}
    <br />
    <spring:message code="general.category" />:
    ${game.getTag().getCategory().getName()}
</h4>
<div class="container">
    <div class="row col-md-12">
        <table class="table table-hover">
            <thead>
            <tr>
                <th>#</th>
                <th><spring:message code="login.username" /></th>
                <th><spring:message code="general.points" /></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="participant" items='${participants}' varStatus="loop">
            <tr>
                <td>${loop.count}</td>
                <td><a href="${contextPath}/edition/user/${participant.getPlayer().getUser().getUsername()}">${participant.getPlayer().getUser().getUsername()}</a></td>
                <td>${participant.getScore()}</td>
            </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</body>
