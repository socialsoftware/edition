<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
<!--  <script type="text/javascript" src="/resources/js/jquery.dataTables.min.js"></script>-->

<link rel="stylesheet" type="text/css" href="/resources/css/bootstrap-select.min.css">
<link rel="stylesheet" type="text/css" href="/resources/css/bootstrap-table.min.css">

<link rel="stylesheet" type="text/css" href="/resources/css/spinner.css" >
<script src="/resources/js/bootstrap-table.min.js"></script>
<script src="/resources/js/bootstrap-select.min.js"></script>


</head>
<body>

<spring:message code="search.complete" var="searchComplete" />
<spring:message code="search.title" var="searchTitle" />
<spring:message code="search.source" var="searchSource" />
<spring:message code="search.authorial" var="searchAuthorial" />

	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>
	<div class="container search-simple">
	<div>
		<h3 class="text-center">
			<spring:message code="header.search.simple" />
		</h3>
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
	 <div class="form-group search-simple">
    
	    <div class="col-sm-4">
	      <input type="text" class="form-control tip" id="query" title="" placeholder='<spring:message code="general.searching.for" />'>
      	</div>
     
       <div class="col-sm-3">
       <div class="tip" title="text div">
      <select class="form-control" data-width="100%" id="searchType" title="">
      	<option value="">${searchComplete}</option>
	    <option value="title">${searchTitle}</option>
	  </select>
	  </div>
	  </div>
	  
	   <div class="col-sm-3">
      <select class="form-control" data-width="100%" id="sourceType">
	    <option value="">${searchSource}</option>
	    <option value="Coelho">Jacinto Prado Coelho</option>
	    <option value="Cunha"> Teresa Sobral Cunha</option>
	    <option value="Zenith">Richard Zenith</option>
	    <option value="Pizarro">Jerónimo Pizarro</option>
	    <option value="BNP">${searchAuthorial}</option>
	   
	  </select>
	  </div>
	  
	    <div class="col-sm-2">
        <button class="btn btn-default" type="button" id="searchbutton"><span class="glyphicon glyphicon-search" ></span> <spring:message code="search" /></button>
      </div>
    </div><!-- /input-group -->
    <br><br>
     <div id="searchresult" style="display:none;width:100%;">
      <hr><div class="spinner-loader"><spring:message code="general.searching" /></div>
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
			
			
			var searchType = $('#searchType').val();
			var sourceType = $('#sourceType').val();
			var data = $('#query').val() + "&" + searchType + "&" + sourceType;
			//var data = $('#query').val();
			
			if($('#query').val()!="") {
				
				
		    $('#searchresult').empty().append("<hr><div class=\"spinner-loader\">Loadind...</div>");
		    $('#searchresult').css("display","block");
				
				
			$.ajax({
				type : "POST",
				url : "/search/simple/result",
				data : data,
				contentType : 'text/plain;charset=UTF-8',
				success : function(html) {
					
					$('#searchresult').empty().append("<hr>"+html);
					$('#tablesearchresults').attr("data-search","true");
					$('#tablesearchresults').bootstrapTable();
					$('#tablesearchresults').show();
					$(".modal-footer").show();
					//$('.result-table').dataTable({
					//	'paging' : false
					//)});
				}
			});
			}
	 			
		});
	</script>
</body>
</html>