<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
	<c:choose>
		<c:when
			test="${(inters.size() == 1) && (inters.get(0).sourceType=='VIRTUAL')}">
			<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%></c:when>
		<c:otherwise><%@ include
				file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%></c:otherwise>
	</c:choose>
	<div id="fragmentBody">
		<%@ include file="/WEB-INF/jsp/fragment/body.jsp"%>
	</div>
</body>
<script>
$(".tip").tooltip({placement: 'bottom'});
</script>
</html>
