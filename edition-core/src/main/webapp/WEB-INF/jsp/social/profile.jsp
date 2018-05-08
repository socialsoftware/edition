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
        <h3 class="text-center">
            ${user.firstName} ${user.lastName}
            <i class="glyphicon glyphicon-user"></i>
        </h3>
    </div>
    <div class="row">
        <ul>
            <li>Member since:</li>
            <li>Points:</li>
        </ul>
    </div>
    <p>
        Member of virtual editions:
        <c:forEach var="edition" items="${user.getPublicEditionList()}"
                   varStatus="loop">
            <a href="${contextPath}/edition/acronym/${edition.getAcronym()}">
                    ${edition.getTitle()}</a><c:if test="${!loop.last}">, </c:if>
        </c:forEach>
    </p>
</div>


<c:set var="isAdmin"
       value="${virtualEdition.getAdminSet().contains(user)}" />
<c:set var="isMember"
       value="${virtualEdition.getParticipantSet().contains(user)}" />
<c:set var="isPending"
       value="${virtualEdition.getPendingSet().contains(user)}" />
<c:set var="isPublic" value="${virtualEdition.pub}" />
<c:set var="isLdoDEdition"
       value="${virtualEdition.isLdoDEdition()}" />

<!--
<form class="form-inline" method="POST"
      action="${contextPath}/">
    <input type="hidden" name="${_csrf.parameterName}"
           value="${_csrf.token}" /> <input type="hidden"
                                            name="externalId" value="${virtualEdition.externalId}" />
    <button type="submit" class="btn btn-primary btn-sm">
        <span class="glyphicon glyphicon-plus"></span>
        Convite
    </button>
</form>
<!-->
</body>
</html>