<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
<link rel="stylesheet" type="text/css" href="/resources/css/bootstrap-table.min.css">
<script src="/resources/js/bootstrap-table.min.js"></script>
</head>
<body>
    <%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

    <div class="container">
        <c:choose> <c:when
            test="${edition.sourceType == 'EDITORIAL'}"><%@ include
                file="/WEB-INF/jsp/edition/expertTableOfContents.jsp"%></c:when>
        <c:when test="${edition.getSourceType() == 'VIRTUAL'}"><%@ include
                file="/WEB-INF/jsp/edition/virtualTableOfContents.jsp"%></c:when>
        </c:choose>
    </div>
</body>
</html>