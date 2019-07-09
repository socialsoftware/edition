<%@ include file="/WEB-INF/jsp/common/tags-head.jsp" %>

<div id="fragmentTranscription">
    <h4 class="text-center">
        <c:choose>
            <c:when test="${uiInterface.getSourceTypeOfInter(inters.get(0).getXmlId())=='EDITORIAL'}">
                ${inters.get(0).title} <a
                    href="${contextPath}/reading/fragment/${inters.get(0).getFragment().getXmlId()}/inter/${inters.get(0).getUrlId()}"><span
                    class="glyphicon glyphicon-eye-open"></span></a>
            </c:when>
            <c:otherwise>
                ${inters.get(0).title}
            </c:otherwise>
        </c:choose>
    </h4>
    <br>
    <c:choose>
        <c:when test="${uiInterface.getSourceTypeOfInter(inters.get(0).lastUsed.getXmlId())=='EDITORIAL'}">
            <div class="well" style="font-family: georgia; font-size: medium;">
                <p>${writer.getTranscription()}</p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="well" style="font-family: courier;">
                <p>${writer.getTranscription()}</p>
            </div>
        </c:otherwise>
    </c:choose>
</div>
