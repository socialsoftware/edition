<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

<h1> Get User Feed </h1>
<div class="row">
    <div class="col-md-4">
        <form class="form-inline" method="POST"
              action="${contextPath}/social/facebook/user"
              enctype="multipart/form-data">
            <input type="hidden" name="${_csrf.parameterName}"
                   value="${_csrf.token}" />
            <div class="form-group">
                <input type="text" name="id">
            </div>
            <br> <br>
            <div class="form-group">
                <button type="submit" class="btn btn-primary btn-sm">
                    <spring:message code="general.submit" />
                </button>
            </div>
        </form>
    </div>
</div>
</body>
</html>

