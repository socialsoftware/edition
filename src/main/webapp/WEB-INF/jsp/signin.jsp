<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
    <%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

    <div class="container text-center">
        <c:if test="${not empty param['param.error']}">
            <div class="row text-error">
                <spring:message code="login.error" />
            </div>
        </c:if>

        <div class="row">
            <div class="login-form">
                <h2>
                    <spring:message code="header.title" />
                </h2>
                <form class="form-horizontal" role="form" method="POST"
                    action="/signin/authenticate">
                    <input type="hidden" name="_csrf" value="${_csrf.token}" />
                    <div class="form-group">
                        <div class="col-md-offset-4 col-md-4">
                            <input type="text" class="form-control"
                                id="username_or_email" name="username"
                                placeholder="<spring:message code="login.username" />">
                        </div>
                        <br>
                        <br>
                        <div class="col-md-offset-4 col-md-4">
                            <input type="password" class="form-control"
                                id="password" name="password"
                                placeholder="<spring:message code="login.password" />">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-offset-4 col-md-4">
                            <button class="btn btn-primary form-control"
                                type="submit">
                                <spring:message code="general.signin" />
                            </button>
                        </div>
                    </div>
                </form>
            </div>

            <!-- TWITTER SIGNIN -->
            <form id="tw_signin" action="/signin/twitter" method="POST">
                <input type="hidden" name="_csrf" value="${_csrf.token}"></input>
                <button type="submit">Sign In with Twitter</button>
            </form>

            <!-- FACEBOOK SIGNIN -->
            <form name="fb_signin" id="fb_signin"
                action="/signin/facebook" method="POST">
                <input type="hidden" name="_csrf" value="${_csrf.token}"></input>
                <input type="hidden" name="scope"
                    value="public_profile,email"></input>
                <button type="submit">Sign In with Facebook</button>
            </form>

            <!-- LINKEDIN SIGNIN -->
            <form name="li_signin" id="li_signin"
                action="/signin/linkedin" method="POST">
                <input type="hidden" name="_csrf" value="${_csrf.token}"></input>
                <button type="submit">Sign In with LinkedIn</button>
            </form>

            <!-- SIGNUP -->
            <form name="li_signup" id="li_signip" action="/signup"
                method="GET">
                <button type="submit">Signup</button>
            </form>

        </div>
    </div>

    <!-- /container -->

</body>

</html>