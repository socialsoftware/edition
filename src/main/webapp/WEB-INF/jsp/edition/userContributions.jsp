<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
    <%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

    <div class="container">
        <h3 class="text-center">
            <spring:message code="general.contributionsOf" />
            ${user.firstName} ${user.lastName}  (${user.username}) (${user.getFragInterSet().size()})
        </h3>

        <table class="table table-hover table-condensed">
            <thead>
                <tr>
                    <th><spring:message code="general.edition" /></th>
                    <th><spring:message
                            code="tableofcontents.title" /></th>
                    <th><spring:message
                            code="tableofcontents.usesEditions" /></th>
                </tr>
            <tbody>
                <c:forEach var="inter" items='${user.getFragInterSet()}'>
                    <tr>
                        <td><a
                            href="${contextPath}/edition/acronym/${inter.edition.acronym}">${inter.getEdition().getReference()}</a></td>
                        <td><a
                            href="${contextPath}/fragments/fragment/inter/${inter.externalId}">${inter.title}</a></td>
                        <td><c:forEach var="used"
                                items="${inter.getListUsed()}">-><a
                                    href="${contextPath}/fragments/fragment/inter/${used.externalId}">${used.shortName}</a>
                            </c:forEach></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>

