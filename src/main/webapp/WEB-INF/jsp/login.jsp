<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
    <%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

    <security:authorize access="!isAuthenticated()">

        <div class="container text-center">
            <c:if test="${loginFailed}">
                <div class="row text-error">
                    <spring:message code="login.error" />
                </div>
            </c:if>

            <div class="row">
                <div class="login-form">
                    <h2>
                        <spring:message code="header.title" />
                    </h2>
                    <form class="form-horizontal" role="form"
                        method="POST" action="j_spring_security_check">
                        <div class="form-group">
                            <div class="col-md-offset-4 col-md-4">
                                <input type="text" class="form-control"
                                    id="username_or_email"
                                    name="j_username"
                                    placeholder="<spring:message code="login.username" />">
                            </div>
                            <br><br>
                            <div class="col-md-offset-4 col-md-4">
                                <input type="password"
                                    class="form-control" id="password"
                                    name="j_password"
                                    placeholder="<spring:message code="login.password" />">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-md-offset-4 col-md-4">
                                <button
                                    class="btn btn-primary form-control"
                                    type="submit">
                                    <spring:message
                                        code="general.signin" />
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- /container -->

    </security:authorize>

</body>
</html>