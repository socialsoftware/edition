<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
    <%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

    <div class="container">
        <h1 class="text-center">
            <spring:message code="general.edition" />
            : ${fragInter.getEdition().getReference()}
            <spring:message code="fragment" />
            : ${fragInter.getTitle()}
        </h1>
        <h4 class="pull-right">
            <spring:message code="general.public.pages" />
            - <a
                href="${contextPath}/edition/internalid/${fragInter.getEdition().getExternalId()}">
                ${fragInter.getEdition().getReference()}</a> : <a
                href="${contextPath}/fragments/fragment/inter/${fragInter.getExternalId()}">
                ${fragInter.getTitle()}</a>
        </h4>
        <br />
        <div class="row">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th><spring:message code="general.taxonomy" /></th>
                        <th><spring:message code="general.category" /></th>
                        <th><spring:message code="general.words" /></th>
                        <th><spring:message
                                code="general.category.merged" /></th>
                        <th><spring:message
                                code="general.category.extracted" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="taxonomy"
                        items='${fragInter.getEdition().getTaxonomiesSet()}'>
                        <c:forEach var="tag"
                            items='${taxonomy.getSortedActiveTags(fragInter)}'>
                            <tr>
                                <td><a
                                    href="${contextPath}/virtualeditions/restricted/taxonomy/${taxonomy.getExternalId()}">${taxonomy.getName()}</a>
                                </td>
                                <td><a
                                    href="${contextPath}/virtualeditions/restricted/category/${tag.getActiveCategory().getExternalId()}">${tag.getActiveCategory().getName()}</a>
                                    (${tag.getWeight()})</td>
                                <td><c:if
                                        test="${tag.getActiveCategory().getType() == 'GENERATED'}">
                                        <c:forEach
                                            var="fragWordInCategory"
                                            items='${tag.getActiveCategory().getSortedFragWordInCategory()}'> ${fragWordInCategory.getFragWord().getWord()} 
                                        </c:forEach>
                                    </c:if></td>
                                <td><c:if
                                        test="${tag.getActiveCategory().getType()=='MERGED'}">
                                        <c:forEach var="mergedCategory"
                                            items='${tag.getActiveCategory().getMergedCategoriesSet()}'><a href="${contextPath}/virtualeditions/restricted/category/${mergedCategory.getExternalId()}">${mergedCategory.getName()}</a>  </c:forEach>
                                    </c:if></td>
                                <td><c:if
                                        test="${tag.getActiveCategory().getType()=='EXTRACTED'}">
                                        <a href="${contextPath}/virtualeditions/restricted/category/${tag.getOriginSplitTag().getCategory().getExternalId()}">${tag.getOriginSplitTag().getCategory().getName()}</a> 
                                    </c:if></td>
                            </tr>
                        </c:forEach>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
