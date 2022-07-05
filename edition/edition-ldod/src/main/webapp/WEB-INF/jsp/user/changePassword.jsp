<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
    <%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>


    <div class="container text-center">

        <div class="row">
            <h3><spring:message code="user.password" /></h3>
        </div>

        <div class="row">
            <c:if test="${!strings.isEmpty(message)}">${message}</c:if>
            <br> <br>

            <form:form class="form-horizontal" id="password"
                action="/user/changePassword" method="post"
                modelAttribute="changePasswordForm">

                <input type="hidden" name="_csrf" value="${_csrf.token}" />
                <input type="hidden" name="username" value="${pageContext.request.userPrincipal.principal.getUsername()}" />

                <div class="form-group">
                    <label class="col-sm-4 control-label"><spring:message code="user.password.current" /></label>
                    <div class="col-sm-4">
                        <form:password class="form-control"
                            path="currentPassword" />
                    </div>
                    <div class="col-sm-2">
                        <form:errors class="has-error"
                            path="currentPassword" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label"><spring:message code="user.password.new" /></label>
                    <div class="col-sm-4">
                        <form:password class="form-control"
                            path="newPassword" />
                    </div>
                    <div class="col-sm-2">
                        <form:errors class="has-error"
                            path="newPassword" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label"><spring:message code="user.password.retype" /></label>
                    <div class="col-sm-4">
                        <form:password class="form-control"
                            path="retypedPassword" />
                    </div>
                    <div class="col-sm-2">
                        <form:errors class="has-error"
                            path="retypedPassword" />
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-12">
                        <button type="submit" class="btn btn-primary"><spring:message code="general.update" /></button>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</body>
</html>