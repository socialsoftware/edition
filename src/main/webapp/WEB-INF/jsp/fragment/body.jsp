<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<div id="fragmentBody">
    <div class="container">
        <div class="row" style="margin-left:0px;margin-right:0px">
            <div class="col-md-9">
                <c:choose>
                    <c:when test="${inters.size() == 0}"><%@ include
                            file="/WEB-INF/jsp/fragment/interEmpty.jsp"%></c:when>
                    <c:when
                        test="${(inters.size() == 1) && (inters.get(0).sourceType=='EDITORIAL')}"><%@ include
                            file="/WEB-INF/jsp/fragment/interEditorial.jsp"%></c:when>
                    <c:when
                        test="${(inters.size() == 1) && (inters.get(0).sourceType=='AUTHORIAL')}"><%@ include
                            file="/WEB-INF/jsp/fragment/interAuthorial.jsp"%></c:when>
                    <c:when
                        test="${(inters.size() == 1) && (inters.get(0).sourceType=='VIRTUAL')}"><%@ include
                            file="/WEB-INF/jsp/fragment/interVirtual.jsp"%></c:when>
                    <c:when test="${(inters.size() > 1) && (inters.get(0).sourceType!='VIRTUAL')}"><%@ include
                            file="/WEB-INF/jsp/fragment/inter2Compare.jsp"%></c:when>
                    <c:otherwise><%@ include
                            file="/WEB-INF/jsp/fragment/virtual2Compare.jsp"%></c:otherwise>
                </c:choose>
            </div>
            <div class="col-md-3">
                <%@ include file="/WEB-INF/jsp/fragment/navegation.jsp"%>
            </div>
        </div>
    </div>
</div>
<br>
<br>