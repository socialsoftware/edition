<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
<script type="text/javascript" src="/static/js/jquery.dataTables.min.js"></script>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>
	<div class='container'>
	<div>
		<h1 class="text-center">
			<spring:message code="header.search.simple" />
		</h1>
	</div>
	
	<div>
		<input id="query"></input>
		<br/>
		<button id="submit">
			<spring:message code="search" />
		</button>
	</div>
	
	<div id="results"></div>
	</div>
	
	<script type="text/javascript">
		$('#submit').click(function() {
			//$(this).blur();//lose selection
			//var data = model.json();
			//var json = JSON.stringify(model.json());
			var data = $('#query').val();

			$.ajax({
				type : "POST",
				url : "/search/simple/result",
				data : data,
				contentType : 'text/plain;charset=UTF-8',
				success : function(html) {
					console.log(html)
					$('#results').empty().append(html);
					$('.result-table').dataTable({
						'paging' : false
					});
				}
			});
		});
	</script>
</body>
</html>