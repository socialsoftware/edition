
<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
<script type="text/javascript" src="/static/js/riot.min.js"></script>
<script type="text/javascript" src="/static/js/form-domain-model.js"></script>

<script type="text/javascript">

	//Presenter		
	$(document).ready(function() {

		var model = new Form();
		var anchor = $('#options');

		//Listen to view events
		//add a new options
		$('body').on('click', '#plusBtn', function() {
			model.add();
		});

		//remove an option
		$('body').on('click', '#minusBtn', function() {
			var id = $(this).parents(".form-group").attr("id");
			model.remove(id);
		});

		//swap an option
		$('body').on('change', '#selection-box', function() {
			var id = $(this).parents(".form-group").attr("id");
			var newOption = $(this).find(':selected')[0].id;
			model.swap(id, newOption);
		});

		//Listen to when an edition option is selected
		$('body').on('change', '#select-edition', function() {
			var id = $(this).parents(".form-group").attr("id");
			var newOption = $(this).find(':selected')[0].id;
			model.changeEdition(id, newOption);
		});

		//Listen to when a different edition's heteronym is selected
		$('body').on('change', '#select-heteronym', function() {
			var id = $(this).parents(".form-group").attr("id");
			var newOption = $(this).find(':selected')[0].id;
			model.changeHeteronym(id, newOption);
		});
		
		$('body').on('change', '#select-inclusion', function() {
			var id = $(this).parents(".form-group").attr("id");
			var newOption = $(this).find(':selected')[0].id;
			model.changeInclusion(id, newOption);
		});
		
		//Listen to when a different edition's date is selected
// 		$('body').on('change', '#edition-date-option', function() {
// 			alert("working");
// 			var id = $(this).parents(".form-group").attr("id");
// 			var newOption = $(this).find(':selected')[0].id;
// 			model.changeEditionDateOption(id, newOption);
// 		});
		
// 		$('body').on('change', '#edition-date-value', function() {
// 			var id = $(this).parents(".form-group").attr("id");
// 			var newOption = $(this).find(':selected')[0].id;
// 			model.changeHeteronym(id, newOption);
// 		});
		
		//Listen to when a different publication place is selected
		$('body').on('change', '#select-publication', function() {
			var id = $(this).parents(".form-group").attr("id");
			var newOption = $(this).find(':selected')[0].id;
			model.changePublication(id, newOption);
		});
		
// 		//Listen to when a different publication place is selected
// 		$('body').on('change', '#single-option', function() {
// 			var id = $(this).parents(".form-group").attr("id");
// 			var newOption = $(this).find(':selected')[0].id;
// 			model.changeSingleOption(id, newOption);
// 		});
		
		//MyDate
		//Listen to when a date option is selected
		$('body').on('change', '#date-option', function() {
			var id = $(this).parents(".form-group").attr("id");
			var newOption = $(this).find(':selected')[0].id;
			model.changeDateOption(id, newOption);
		});
		
		//Listen to when a date begin value is selected
		$('body').on('change', '#date-value-begin', function() {
			var id = $(this).parents(".form-group").attr("id");
			var value = $(this).val();
				model.changeDateBeginDateOption(id, value);
		});
		
		//Listen to when a date end value is selected
		$('body').on('change', '#date-value-end', function() {
			var id = $(this).parents(".form-group").attr("id");
			var value = $(this).val();
				model.changeDateEndDateOption(id, value);
		});
		
		//Form mode,
		$('body').on('change', '#select-mode', function() {
			model.changeMode($(this).find(':selected')[0].id);
		});
		
		$('body').on('change', '#select-ldod-mark', function() {
			var id = $(this).parents(".form-group").attr("id");
			var option = $(this).find(':selected')[0].id;
			model.changeLdoDMark(id, option);
		});
		

		
		//Events from domain
		//Add event
		model.on("add", function(item) {
			item.render(function(html) {
				anchor.append(html);
			});
		});

		//Remove event
		model.on("remove", function(key) {
			//anchor.remove;
			$("#" + key).remove();
		});

		//Swap event
		model.on("swap", function(item) {
			item.toHTML(function(html){
			    $("#" + item.id).children(".extended").empty();
			    $("#" + item.id).children(".extended").append(html)
			    //$("#" + item.id).children(".extended").replaceWith(html);
			});
			
			//$("#"+item.key).append(item.pattern);
		});

		
		model.on("change-editor", function(item) {
			item.extendedEdition(function(html) {
				$('#' + item.id).find('#extra-options').empty().append(html);
			});
		});

		
		model.on("change-date-option", function(item) {
			if(item.getDateOption() === 'dated'){
				$('#'+item.id).find("#date-values").show();
			}else{
				$('#'+item.id).find("#date-values").hide();	
			}
		});

		
		//Handle 
		$('#submit').click(function() {
			var json = JSON.stringify(model.json());
			$.ajax({
				type : "POST",
				url : "/search/advanced/result",
				data : json,
				contentType : 'application/json',
			}).done(function(fragments) {
				var html = "<table class=\"table table-hover table-condensed\">"+
			    "<thead>"+
			        "<tr>"+
			            "<th>Frags</th>"+
			            "<th>Iterps</th>"+
			        "</tr>"+"<tbody>";
				
				for(var i in fragments){
					html +=  "<tr>"+
						"<td rowspan=\""+fragments[i].inters.length+"\">"+"<a href=\""+fragments[i].url+"\">"+fragments[i].title+"</a>"+"</td>";

					var length = fragments[i].inters.length	;
					html += "<td> <a href=\""+fragments[i].inters[0].url+"\">"+fragments[i].inters[0].title+"</a></td></tr>";
						
					for(var j = 1 ; j<length;j++){
						html += "<tr><td> <a href=\""+fragments[i].inters[j].url+"\">"+fragments[i].inters[j].title+"</a></td></tr>";
					}
				}
				
				$("#results").empty().append(html);
			});
		});

		//Add one element
		model.add();
	});
</script>

</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>
	<div class='container'>
		<h1 class="text-center">
			<spring:message code="header.search.advanced" />
		</h1>
		<div id="main" class="row ">
			<div class="col-xs-6 col-md-6 center-block">
				<select id="select-mode">
					<option id="and">Match all of the following</option>
					<option id="or">Match any of the following</option>
				</select>
			</div>
			<div class="col-xs-6 col-md-6 center-block " align="right">
				<button id="plusBtn" type="button" class="btn btn-default btn-lg">
					<span class="glyphicon glyphicon-plus"></span>
				</button>
			</div>
		</div>
		<div id="options"></div>
		<div>
			<button type="submit" class="btn btn-default btn-lg" id='submit'>
				<spring:message code="search" />
			</button>
		</div>
		<div id="results"></div>

	</div>


</body>
</html>