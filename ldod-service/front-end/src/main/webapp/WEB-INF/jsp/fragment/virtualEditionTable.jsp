<%@ include file="/WEB-INF/jsp/common/tags-head.jsp" %>
<link rel="stylesheet" type="text/css"
      href="/resources/css/bootstrap-table.min.css">
<script src="/resources/js/bootstrap-table.min.js"></script>

<table class="table table-bordered table-striped table-condensed">
    <thead>
    <tr>
        <th><spring:message code="virtualcompare.quote"/></th>
        <th><spring:message code="virtualcompare.comment"/></th>
        <th><spring:message code="virtualcompare.user"/></th>
        <th><spring:message code="general.tags"/></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="tag" items='${inter.getAllDepthTagsAccessibleByUser(user)}'>
        <tr>
            <td>---</td>
            <td>---</td>
            <td><span class="glyphicon glyphicon-user"></span> <a
                    href="${contextPath}/edition/user/${tag.getUsername()}">${tag.getUsername()}</a></td>
            <td><span class="glyphicon glyphicon-tag"></span> <a
                    href="${contextPath}/edition/acronym/${tag.getAcronym()}/category/${tag.getUrlId()}">${tag.getName()}</a>
            </td>
        </tr>
    </c:forEach>
    <c:forEach var="annotation" items='${inter.getAllDepthAnnotationsAccessibleByUser(user)}'>
        <tr>
            <td>${annotation.quote}</td>

            <c:choose>
                <c:when test="${annotation.isHumanAnnotation()}">
                    <td>${annotation.text}</td>
                </c:when>
                <c:otherwise>
                    <td><a href="${annotation.getSourceLink()}">tweet</a>
                        <br>
                        <a href="${annotation.getProfileURL()}">profile</a>
                        <br>
                            ${annotation.getDate()}
                        <c:if test="${annotation.getCountry() != 'unknown'}">
                            <br>
                            Country: ${annotation.getCountry()}
                        </c:if>
                    </td>
                </c:otherwise>
            </c:choose>

            <td><span class="glyphicon glyphicon-user"></span> <a
                    href="${contextPath}/edition/user/${annotation.username}">${annotation.username}</a></td>

            <c:if test="${annotation.isHumanAnnotation()}">
                <td><c:forEach var="tag" items='${annotation.getTags()}'>
                    <span class="glyphicon glyphicon-tag"></span>
                    <a
                            href="${contextPath}/edition/acronym/${tag.getAcronym()}/category/${tag.getUrlId()}">${tag.getName()}</a>
                </c:forEach></td>
            </c:if>


        </tr>
    </c:forEach>
    </tbody>
</table>
