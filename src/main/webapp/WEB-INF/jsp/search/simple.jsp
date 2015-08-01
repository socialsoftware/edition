<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
<script type="text/javascript" src="/static/js/jquery.dataTables.min.js"></script>

<link rel="stylesheet" type="text/css" href="/static/css/bootstrap-select.min.css">
<link rel="stylesheet" type="text/css" href="/static/css/spinner.css" >
<script src="/static/js/bootstrap-table.min.js"></script>
<script src="/static/js/bootstrap-select.min.js"></script>


</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>
	<div class='container'>
	<div>
		<h1 class="text-center">
			<spring:message code="header.search.simple" />
		</h1>
	</div>
	<br><br>
	<!--  
	<div>
		<input id="query"></input>
		<br/>
		<button id="submit">
			<spring:message code="search" />
		</button>
	</div>
	-->
	 <div class="form-group">
    
	    <div class="col-sm-4">
	      <input type="text" class="form-control tip" id="query" title="pesquisa x" placeholder="Search for...">
      	</div>
     
       <div class="col-sm-3">
       <div class="tip" title="text div">
      <select class="selectpicker" data-width="100%" id="searchType" title="teste x">
      	<option value="">Pesquisa completa</option>
	    <option value="title">Pesquisa por tÌtulo</option>
	  </select>
	  </div>
	  </div>
	  
	   <div class="col-sm-3">
      <select class="selectpicker" data-width="100%" id="sourceType">
	    <option value="">Tipos de fonte</option>
	    <option value="Coelho">Jacinto Prado Coelho</option>
	    <option value="Cunha"> Teresa Sobral Cunha</option>
	    <option value="Zenith">Richard Zenith</option>
	    <option value="Pizarro">JerÛnimo Pizarro</option>
	    <option value="BNP">Fontes Autorais</option>
	   
	  </select>
	  </div>
	  
	    <div class="col-sm-2">
        <button class="btn btn-default" type="button" id="searchbutton"><span class="glyphicon glyphicon-search" ></span> Search</button>
      </div>
    </div><!-- /input-group -->
    <br><br>
     <div id="searchresult" style="display:none;width:100%;">
      <hr><div class="spinner-loader">Loadind...</div>
      </div>
      
	<div id="results"></div>
	</div>
	
	<script type="text/javascript">
	
	/*
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
	 */	
		
		$('#searchbutton').click(function() {
			//$(this).blur();//lose selection
			//var data = model.json();
			//var json = JSON.stringify(model.json());
			
			$('#searchresult').empty().append("<hr><div class=\"spinner-loader\">Loadind...</div>");
			$('#searchresult').css("display","block");
			
			var searchType = $('#searchType').val();
			var sourceType = $('#sourceType').val();
			var data = $('#query').val() + "&" + searchType + "&" + sourceType;
			
			$.ajax({
				type : "POST",
				url : "/search/simple/virtual/result",
				data : data,
				contentType : 'text/plain;charset=UTF-8',
				success : function(html) {
					
					$('#searchresult').empty().append("<hr>"+html);
					$('#tablesearchresults').attr("data-pagination","true");
					$('#tablesearchresults').attr("data-search","true");
					$('#tablesearchresults').bootstrapTable();
					$('#tablesearchresults').show();
					$(".modal-footer").show();
					//$('.result-table').dataTable({
					//	'paging' : false
					//)});
				}
			});
		});
	</script>
</body>
</html>