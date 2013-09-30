<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<div id=fragmentInter class="row-fluid">
    <h3>Comparison of virtual editions fragments</h3>

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
                                    <span class="badge">${tag.tag}
                                    </span>
                                </c:forEach></td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <c:forEach var="inter" items="${inters}">
                            <td><c:forEach var='user' items='${inter.getContributorSet()}'>${user.username} </c:forEach></td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <c:forEach var="inter" items="${inters}">
                            <td>
                                <table
                                    class="table table-striped table-condensed">
                                    <thead>
                                        <tr>
                                            <th>Quote</th>
                                            <th>Text</th>
                                            <th>User</th>
                                            <th>Tags</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="annotation"
                                            items='${inter.getAnnotationSet()}'>
                                            <tr>
                                                <td>${annotation.quote}</td>
                                                <td>${annotation.text}</td>
                                                <td>${annotation.user.username}</td>
                                                <td><c:forEach
                                                        var="tag"
                                                        items='${annotation.getTagSet()}'>
                                                        <span
                                                            class="badge">${tag.tag}</span>
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
