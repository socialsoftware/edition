<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
<link rel="stylesheet" type="text/css" href="/resources/css/spinner.css">
<link rel="stylesheet" type="text/css"
	href="/resources/css/bootstrap-table.min.css">
<script src="/resources/js/bootstrap-table.min.js"></script>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

	<div class="container">
		<h3 class="text-center">
			<spring:message code="fragment.codified" />
			(${fragments.size()})
		</h3>
		<div class="text-center" id="wait" style="width: 100%;">
			<br /> <br />
			<div class="spinner-loader"></div>
		</div>
		<div id="page" style="display: none;">
			<div class="row">
				<table id="tablefragments" data-pagination="false">
					<!-- <table class="table table-striped table-bordered table-condensed"> -->
					<thead>
						<tr>
							<th><spring:message code="tableofcontents.title" /></th>
							<th>Jacinto do Prado Coelho</th>
							<th>Teresa Sobral Cunha</th>
							<th>Richard Zenith</th>
							<th>Jerónimo Pizarro</th>
							<th><spring:message code="authorial" /></th>
							<th><spring:message code="authorial" /></th>
							<th><spring:message code="authorial" /></th>
						</tr>
					<tbody>
						<c:forEach var="fragment" items='${fragments}'>
							<tr>
								<td><a
									href="${contextPath}/fragments/fragment/${fragment.xmlId}">${fragment.title}</a>
								</td>
								<td><c:forEach var="inter"
										items='${fragment.getExpertEditionInters(jpcEdition)}'><%@ include
											file="/WEB-INF/jsp/fragment/interMetaInfo.jsp"%><br>
										<br>
									</c:forEach></td>
								<td><c:forEach var="inter"
										items='${fragment.getExpertEditionInters(tscEdition)}'><%@ include
											file="/WEB-INF/jsp/fragment/interMetaInfo.jsp"%><br>
										<br>
									</c:forEach></td>
								<td><c:forEach var="inter"
										items='${fragment.getExpertEditionInters(rzEdition)}'><%@ include
											file="/WEB-INF/jsp/fragment/interMetaInfo.jsp"%><br>
										<br>
									</c:forEach></td>
								<td><c:forEach var="inter"
										items='${fragment.getExpertEditionInters(jpEdition)}'><%@ include
											file="/WEB-INF/jsp/fragment/interMetaInfo.jsp"%><br>
										<br>
									</c:forEach></td>
								<c:forEach var="inter"
									items='${fragment.getSortedSourceInter()}'>
									<td><%@ include
											file="/WEB-INF/jsp/fragment/interMetaInfo.jsp"%><br>
										<br></td>
								</c:forEach>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
<script>
	$('#tablefragments').attr("data-search", "true");
	$('#tablefragments').bootstrapTable();
	$(".tip").tooltip({
		placement : 'bottom'
	});
</script>
<script>
	$(document).ready(function() {
		$('#page').show();
		$('#wait').hide();
	});
</script>
</html>
