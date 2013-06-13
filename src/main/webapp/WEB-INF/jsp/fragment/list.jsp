<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$(
								'[id="fragments-details"][data-toggle="buttons-checkbox"]')
								.on(
										'click',
										function() {
											var selDetail = $(
													'input:checkbox[name=detail]')
													.is(':checked');
											$
													.get(
															"${contextPath}/fragments/list",
															{
																detail : selDetail
															},
															function(html) {
																$(
																		"#fragmentList")
																		.replaceWith(
																				html);
															});
										});
					});
</script>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/ldod-header.jsp"%>

	<div class="container-fluid">
		<h1 class="text-center"><spring:message code="fragmentlist.title" /></h1>
		<div class="well" id="fragments-details"
			data-toggle="buttons-checkbox">
			<label class="checkbox inline"> <input type="checkbox"
				class="btn" name=detail value="Yes"> <spring:message code="fragmentlist.showdetails" />
			</label>
		</div>
		
		<%@ include file="/WEB-INF/jsp/fragment/listSimple.jsp"%>

	</div>
</body>
</html>