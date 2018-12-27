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
            <h3><spring:message code="user.edit" /></h3>
        </div>

        <div class="row">
            <c:if test="${!strings.isEmpty(message)}">${message}</c:if>
            <br> <br>

            <form:form class="form-horizontal" id="edit"
                action="/admin/user/edit" method="post"
                modelAttribute="editUserForm">

                <input type="hidden" name="_csrf" value="${_csrf.token}" />

                <input type="hidden" name="oldUsername" value="${editUserForm.oldUsername}" />

                <div class="form-group">
                    <label class="col-sm-4 control-label"><spring:message code="login.username" /></label>
                    <div class="col-sm-4">
                        <form:input class="form-control"
                            path="newUsername" value="${editUserForm.newUsername}" />
                    </div>
                    <div class="col-sm-2">
                        <form:errors class="has-error"
                            path="newUsername" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label"><spring:message code="user.firstName" /></label>
                    <div class="col-sm-4">
                        <form:input class="form-control"
                            path="firstName" value="${editUserForm.firstName}" />
                    </div>
                    <div class="col-sm-2">
                        <form:errors class="has-error"
                            path="firstName" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label"><spring:message code="user.lastName" /></label>
                    <div class="col-sm-4">
                        <form:input class="form-control"
                            path="lastName" value="${editUserForm.lastName}" />
                    </div>
                    <div class="col-sm-2">
                        <form:errors class="has-error"
                            path="lastName" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label"><spring:message code="user.email" /></label>
                    <div class="col-sm-4">
                        <form:input class="form-control"
                            path="email" value="${editUserForm.email}" />
                    </div>
                    <div class="col-sm-2">
                        <form:errors class="has-error"
                            path="email" />
                    </div>
                </div>
               <div class="form-group">
                    <label class="col-sm-4 control-label"><spring:message code="user.role.user" /></label>
                    <div class="col-sm-4">
                        <form:checkbox class="form-control"
                            path="user" value="${editUserForm.isUser()}" />
                    </div>
                    <div class="col-sm-2">
                        <form:errors class="has-error"
                            path="user" />
                    </div>
                </div>
               <div class="form-group">
                    <label class="col-sm-4 control-label"><spring:message code="user.role.admin" /></label>
                    <div class="col-sm-4">
                        <form:checkbox class="form-control"
                            path="admin" value="${editUserForm.isAdmin()}" />
                    </div>
                    <div class="col-sm-2">
                        <form:errors class="has-error"
                            path="admin" />
                    </div>
                </div>
               <div class="form-group">
                    <label class="col-sm-4 control-label"><spring:message code="user.enabled" /></label>
                    <div class="col-sm-4">
                        <form:checkbox class="form-control"
                            path="enabled" value="${editUserForm.isEnabled()}" />
                    </div>
                    <div class="col-sm-2">
                        <form:errors class="has-error"
                            path="enabled" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label"><spring:message code="user.password.new" /></label>
                    <div class="col-sm-4">
                        <form:input class="form-control"
                            path="newPassword" />
                    </div>
                    <div class="col-sm-2">
                        <form:errors class="has-error"
                            path="newPassword" />
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