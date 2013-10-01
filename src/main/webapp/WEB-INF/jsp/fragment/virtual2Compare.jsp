<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<div id=fragmentInter class="row-fluid">
    <h3>
        <spring:message code="virtualcompare.title" />
    </h3>

    <div class="row-fluid">
        <div class="row-fluid span12">
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <c:forEach var="inter" items="${inters}">
                            <th>${inter.edition.getReference()}</th>
                        </c:forEach>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <c:forEach var="inter" items="${inters}">
                            <td><c:forEach var="tag"
                                    items='${inter.getTagSet()}'>
                                    <i class="icon-tag"></i>
                                    <a
                                        href="${contextPath}/edition/tag/${tag.tag}">${tag.tag}</a>
                                </c:forEach></td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <c:forEach var="inter" items="${inters}">
                            <td><c:forEach var='user'
                                    items='${inter.getContributorSet()}'>
                                    <i class="icon-user"></i>
                                    <a
                                        href="${contextPath}/edition/user/${user.username}">${user.username}</a>
                                </c:forEach></td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <c:forEach var="inter" items="${inters}">
                            <td>
                                <table
                                    class="table table-striped table-condensed">
                                    <thead>
                                        <tr>
                                            <th><spring:message
                                                    code="virtualcompare.quote" /></th>
                                            <th><spring:message
                                                    code="virtualcompare.comment" /></th>
                                            <th><spring:message
                                                    code="virtualcompare.user" /></th>
                                            <th><spring:message
                                                    code="general.tags" /></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="annotation"
                                            items='${inter.getAnnotationSet()}'>
                                            <tr>
                                                <td>${annotation.quote}</td>
                                                <td>${annotation.text}</td>
                                                <td><i
                                                    class="icon-user"></i><a
                                                    href="${contextPath}/edition/user/${annotation.user.username}">${annotation.user.username}</a></td>
                                                <td><c:forEach
                                                        var="tag"
                                                        items='${annotation.getTagSet()}'>
                                                        <i
                                                            class="icon-tag"></i>
                                                        <a
                                                            href="${contextPath}/edition/tag/${tag.tag}">${tag.tag}</a>
                                                    </c:forEach></td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </td>
                        </c:forEach>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
