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
            <h3>Sign Up</h3>
        </div>

        <div class="row">
            <c:if test="${!strings.isEmpty(message)}">${message}</c:if>
            <br> <br>

            <form:form class="form-horizontal" id="signup"
                action="/signup" method="post"
                modelAttribute="signupForm" commandNme="signupForm">

                <input type="hidden" name="_csrf" value="${_csrf.token}" />

                <div class="form-group">
                    <label class="col-sm-4 control-label">First
                        name</label>
                    <div class="col-sm-4">
                        <form:input class="form-control"
                            path="firstName" />
                    </div>
                    <div class="col-sm-2">
                        <form:errors class="has-error" path="firstName" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label">Last
                        name</label>
                    <div class="col-sm-4">
                        <form:input class="form-control" path="lastName" />
                    </div>
                    <div class="col-sm-2">
                        <form:errors class="has-error" path="lastName" />
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-4 control-label">Username</label>
                    <div class="col-sm-4">
                        <form:input class="form-control" path="username" />
                    </div>
                    <div class="col-sm-2">
                        <form:errors class="has-error"
                            path="username" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label">Password</label>
                    <div class="col-sm-4">
                        <form:password class="form-control"
                            path="password" />
                    </div>
                    <div class="col-sm-2">
                        <form:errors class="has-error"
                            path="password" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label">Email</label>
                    <div class="col-sm-4">
                        <form:input class="form-control" path="email" />
                    </div>
                     <div class="col-sm-2">
                        <form:errors class="has-error"
                            path="email" />
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-12">
                        <button type="submit" class="btn btn-primary">Signup</button>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</body>
</html>