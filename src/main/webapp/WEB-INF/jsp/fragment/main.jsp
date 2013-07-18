<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
    <%@ include file="/WEB-INF/jsp/common/ldod-header.jsp"%>

    <div class="container-fluid">
        <div class="row-fluid">
            <div class="span10">
                <c:choose>
                    <c:when test="${inter.sourceType=='EDITORIAL'}"><%@ include
                            file="/WEB-INF/jsp/fragment/interEditorial.jsp"%></c:when>
                    <c:when test="${inter.sourceType=='AUTHORIAL'}"><%@ include
                            file="/WEB-INF/jsp/fragment/interAuthorial.jsp"%></c:when>
                    <c:otherwise>
                        <%@ include
                            file="/WEB-INF/jsp/fragment/interEmpty.jsp"%></c:otherwise>
                </c:choose>
            </div>
            <div class="span2">
                <%@ include file="/WEB-INF/jsp/fragment/navegation.jsp"%>
            </div>
        </div>
    </div>
</body>
</html>
