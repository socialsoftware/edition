<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <link rel="stylesheet" type="text/css"
          href="/resources/css/bootstrap-table.min.css">
    <script src="/resources/js/bootstrap-table.min.js"></script>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="panel panel-default">
            <div class="panel-heading">  <h4 ></h4></div>
            <div class="panel-body">
                <div class="col-md-4 col-xs-12 col-sm-6 col-lg-4">
                    <img alt="User Pic" src="https://x1.xingassets.com/assets/frontend_minified/img/users/nobody_m.original.jpg" id="profile-image1" class="img-circle img-responsive">
                </div>
                <div class="col-md-8 col-xs-12 col-sm-6 col-lg-8" >
                    <div class="container" >
                        <h2>${user.firstName} ${user.lastName}</h2>
                    </div>
                    <hr>
                    <ul class="container details" >
                        <li><p><span class="glyphicon glyphicon-user one" style="width:50px;"></span>${user.getUsername()}</p></li>
                    </ul>
                    <hr>
                    <div class="col-sm-5 col-xs-6 tital ">Member since: </div>
                    <div class="col-sm-5 col-xs-6 tital"> Virtual Editions:</div>
                    <p>
                        <strong><spring:message code="header.editions" />: </strong>
                        <c:forEach var="edition" items="${user.getPublicEditionList()}"
                                   varStatus="loop">
                            <a href="${contextPath}/edition/acronym/${edition.getAcronym()}">
                                    ${edition.getTitle()}</a><c:if test="${!loop.last}">, </c:if>
                        </c:forEach>
                    </p>
                </div>
            </div>
        </div>
</div>
</body>
</html>


