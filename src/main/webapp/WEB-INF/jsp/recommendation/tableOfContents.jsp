<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
<script src="/static/js/jquery-ui-1.11.4/jquery-ui.js"></script>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>
	<div class="container">
		<input id="acronym" type="hidden" name="externalId"
			value="${edition.acronym}" />
		<h3 class="text-center">
			<spring:message code="virtualedition" />
			${edition.title} (${edition.getSortedInterps().size()})
		</h3>
		<c:if test="${heteronym != null}">
			<h4 class="text-left">
				<spring:message code="tableofcontents.fragmentsof" />
				${heteronym.name}
			</h4>
		</c:if>
			<h4 class="text-center">
				<spring:message code="general.taxonomies" />
			</h4>
			<p class="text-center">
				<input style="display: inline;" class="text-center" type="checkbox"
					id="iterative-sort">
				<spring:message code="recommendation.iterativeSort" />
			</p>

			<table id="taxonomy-table" class="table table-condensed table-hover">
				<thead>
					<tr>
						<th class="iterative-sort" style="display: none;"><spring:message
								code="recommendation.iteration" /></th>
						<th class="text-center"><spring:message
								code="recommendation.criteria" /></th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<th class="iterative-sort" style="display: none;">1</th>
						<th>
							<div class="row row-centered taxonomy-row" id="0"
								style="min-height: 40px;">
								<div class="col-md-3 col-sm-6 taxonomy-range"  property-type="edition">
									<p><spring:message javaScriptEscape="true" code="general.edition"/></p>
									<input type="range" class="range" value='${editionWeight}' max="1" min="0" step="0.1">
								</div>
								<div class="col-md-3 col-sm-6 taxonomy-range"  property-type="source">
									<p><spring:message javaScriptEscape="true" code="general.sources"/></p>
									<input type="range" class="range" value='${sourceWeight}' max="1" min="0" step="0.1">
								</div>
								<div class="col-md-3 col-sm-6 taxonomy-range"  property-type="heteronym">
									<p><spring:message javaScriptEscape="true" code="general.heteronym"/></p>
									<input type="range" class="range" value='${heteronymWeight}' max="1" min="0" step="0.1">
								</div>
								<div class="col-md-3 col-sm-6 taxonomy-range"  property-type="date">
									<p><spring:message javaScriptEscape="true" code="general.date"/></p>
									<input type="range" class="range" value='${dateWeight}' max="1" min="0" step="0.1">
								</div>
								<div class="col-md-3 col-sm-6 taxonomy-range"  property-type="text">
									<p><spring:message javaScriptEscape="true" code="general.text"/></p>
									<input type="range" class="range" value='${textWeight}' max="1" min="0" step="0.1">
								</div>
								<c:forEach var="taxonomy" items='${taxonomiesMap.keySet()}'>
									<div class="col-md-3 col-sm-6 taxonomy-range"
										id="${taxonomy.getName()}" property-type="specific-taxonomy">
										<p>${taxonomy.getName()}</p>
										<input type="range" class="range" value="${taxonomiesMap.get(taxonomy)}" max="1.0"
											min="0.0" step="0.1">
									</div>
								</c:forEach>
							</div>
						</th>
					</tr>
					<c:forEach var="i" begin="2"
						end="${(6 + edition.getTaxonomiesSet().size()) - 1}">
						<tr class="iterative-sort" style="display: none;">
							<th>${i}</th>
							<th>
								<div class="row row-centered taxonomy-row" id="${i}"
									style="min-height: 40px;"></div>
							</th>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="extra" style="display: none;" align="right">
				<div class="form-inline extra">
					<button type="submit" id="save" class="btn btn-primary btn-sm">
						<span class="glyphicon glyphicon-edit"></span>
						<spring:message code="general.save" />
					</button>
				</div>
				<form class="form-inline extra" method="POST"
					action="/recommendation/create" style="display: none;" id="create" >
					<div class="form-group">
						<input type="text" class="form-control" id="new-acronym"
							name="acronym"
							placeholder="<spring:message code="virtualeditionlist.acronym"/>" />
					</div>

					<div class="form-group">
						<input type="text" class="form-control" name="title"
							id="new-title"
							placeholder="<spring:message code="virtualeditionlist.name" />" />
					</div>
					<div class="form-group">
						<select name="pub" class="form-control" id="new-pub">
							<option value="true" selected>
								<spring:message code="general.public" />
							</option>
							<option value="false" selected>
								<spring:message code="general.private" />
							</option>
						</select>
					</div>

					<button type="submit" class="btn btn-primary btn-sm">
						<span class="glyphicon glyphicon-edit"></span>
						<spring:message code="general.create" />
					</button>

				</form>
			</div>
		
		 <c:choose>
		 	<c:when test="${!edition.hasMultipleSections() || not empty inters}">
		 		<%@ include file="/WEB-INF/jsp/recommendation/virtualTable.jsp"%>
		 	</c:when>
        	<c:when test="${edition.hasMultipleSections()}">
        		<%@ include file="/WEB-INF/jsp/recommendation/virtualTableWithSections.jsp"%>
			</c:when>
        </c:choose>
	</div>
	<script type="text/javascript">
		$(document).ready(function(){
			
			function fadeWhenZero(elem){
				var inp = elem.children('input.range');
				var val = inp.val();
				if(val > 0){
					elem.fadeTo(0,1.);
				}else{
					elem.fadeTo(0,0.5);
				}
			}
			
			function startLoad(){
				
				$('body').addClass('progress');
				//$('body').children().addClass('inherit');
				//$('*').css('cursor','inherit');
				//$('body').css('cursor','progress');
			}
			
			function endLoad(){
				$('body').removeClass('progress');
				//$('*').css('cursor','auto');
				//$('body').css('cursor','auto');
			}
			
			function sortInters(id){
				startLoad();
				
				var url;
				var data;
				var id = id;
				var acr = $('#acronym').attr('value');
				var json = [];
				
				if($('#iterative-sort').is(':checked')){
					url = '/recommendation/iterativeSort';
					var stop = false;
					$('.taxonomy-range').each(function(){
						var level = parseInt($(this).parent().attr('id'),10);
						if(isNaN(level)){
							alert("Not a number at " + $(this).attr('id'));
							stop = true;
							return false;
						}
						
						var type = $(this).attr('property-type');
						if(type ==='specific-taxonomy'){
							json.push({
								type : 'property-with-level',
								level : level,
								property : {
									type : 'binary-taxonomy',
									acronym : acr,
									taxonomy : $(this).attr('id'),
									weight : $(this).children('input.range').val()
								}
							});
						}else{
							json.push({
								type : 'property-with-level',
								level : level,
								property : {
									type : type,
									taxonomy : $(this).attr('id'),
									weight : $(this).children('input.range').val()
									}
								});
						} 
					});
					if(stop){ 
						return;
					}
				} else {
					url='/recommendation/sortedEdition';
					$('.taxonomy-range').each(function(){
						var type = $(this).attr('property-type');
						if(type ==='specific-taxonomy'){
							json.push({
								type : type,
								acronym : acr,
								taxonomy : $(this).attr('id'),
								weight : $(this).children('input.range').val()
								});
						}else{
							json.push({
								type : type,
								taxonomy : $(this).attr('id'),
								weight : $(this).children('input.range').val()
								});
						}
					});
				}
				data = {acronym : acr, id : id, properties : json};
				$.ajax({
					url : url,
					type : 'post',
					data : JSON.stringify(data),
					contentType : 'application/json;charset=UTF-8',
					success: function(htm){
						//body.css("cursor", "auto");
						endLoad();
						$('#result-type-iterative').remove();
						$('#virtual-table').empty().append(htm)
						$('.extra').show();
					}
				});
			}
			
			function reSort(){
				var id = $('a.inter.selected').attr('id') || $('a.inter').first().attr('id');
				sortInters(id);
			}
			
			$('.taxonomy-range').each(function(){
				fadeWhenZero($(this));
			});
			
			$('.taxonomy-range').change(function(){
				fadeWhenZero($(this));
				reSort();
			});
			
			$('#iterative-sort').change(function () {
			    if ($(this).is(":checked")) {
			    	$('.iterative-sort').show(); 
			    	$('.taxonomy-range > p').each(function(){
			    		$(this).addClass("draggable");	
			    	});
			    	$('.taxonomy-row').each(function(){
			    		$(this).addClass("drop-zone");	
			    	});
			    	
					$('.draggable').draggable({
				      cancel: "a.ui-icon", 
				      revert: "invalid",
				      containment: "document",
				      helper: "clone",
				      cursor: "move",
				      cursorAt: { top: 5, left: 5 }
				    });
					
				    $('.drop-zone').droppable({
				      accept: ".draggable",
				      activeClass: "ui-state-highlight",
				      hoverClass: "ui-state-hover",
				      drop: function( event, ui ) {
						var self=this, item = $(ui.draggable).parent();
						startLoad();
						item.fadeOut(function(){
							item.appendTo(self).fadeIn();
							reSort();	
				       	});
				       }
				    });
			    }else{
			    	$('.taxonomy-range').each(function(){
						$('#0').append($(this));
						$(this).find('p').removeClass('draggable ui-draggable ui-draggable-handle');
			    	});
			    	$('.iterative-sort').hide();	
			    }
			    
			    reSort();
			});
			
			$('#save').click(function(){
				
				var acronym = $('#acronym').attr('value');
				if($('#result-type-iterative').length){
					var json = [];
					var stack = [];
					
					
					var data = $('#virtual-table tbody tr').map(function(){
						var row = $(this);
						var arr = [];
							$(this).find('td.level').each( function(){
								var text = '';
								$(this).find('p').each(function(){
									text+=$(this).text();
								});
								arr.push(text);
							});
						return {
							depth : arr,
							id : row.find('a.inter').attr('id')
						}
					}).get();

					var old = data[0].depth || [];
					for (i in data){ 
						if(data[i].depth.length == 0){
							data[i].depth = old.slice();
						}else if (data[i].depth.length < old.length){
							var temp = old.slice(0,old.length - data[i].depth.length);
							var fin = temp.concat(data[i].depth);
							old = fin;
							
							data[i].depth = fin.slice();
						}else if (data[i].depth.length === old.length){
							old = data[i].depth;
						}else{
							
						}
					}
					
					var json = {
						acronym : acronym,	
						sections : data
					};
					
					$.ajax({
						url : '/recommendation/iterativesort/save',
						type : 'post',
						data : JSON.stringify(json),
						contentType : 'application/json;charset=UTF-8',
						success: function(htm){
							$('#virtual-table').empty().append(htm);
						}
					});
				} else {
					var inters = [];
					$('a.inter').each(function(){
						inters.push($(this).attr('id'));
					});
					var data = {acronym : acronym, inter : inters};
					$.ajax({
						url : '/recommendation/save',
						type : 'post',
						data : data,
						success: function(htm){
							$('#virtual-table').empty().append(htm);
						}
					});
				}
			});
			
			$('#create').submit(function(event){
				var form = $(this);
				if($('#result-type-iterative').length){
					var json = [];
					var stack = [];
					
					var data = $('#virtual-table tbody tr').map(function(){
						var row = $(this);
						var arr = [];
						
						$(this).find('td.level').each( function(){
							var text = '';
							$(this).find('p').each(function(){
								text+=$(this).text();
							});
							arr.push(text);
						});
							
						return {
							depth : arr,
							id : row.find('a.inter').attr('id')
						}
					}).get();

					var old = data[0].depth || [];
					for (i in data){ 
						if(data[i].depth.length == 0){
							data[i].depth = old.slice();
						}else if (data[i].depth.length < old.length){
							var temp = old.slice(0,old.length - data[i].depth.length);
							var fin = temp.concat(data[i].depth);
							old = fin;
							data[i].depth = fin.slice();
						}else if (data[i].depth.length === old.length){
							old = data[i].depth;
						}else{
							
						}
					}
					
					var acronym = $('#new-acronym').val();
					var pub = $('#new-pub').val();
					var title = $('#new-title').val();
					
					var json = {
						acronym : acronym,
						title : title,
						pub : pub,
						sections : data
					};
				$(this).attr('action', '/recommendation/iterativesort/create');
				var depthstring = '';
				for (i in data){ 
					$('<input />').attr('type', 'hidden').attr('name', 'inter[]').attr('value', data[i].id).appendTo(form);
					depthstring = data[i].depth[0];
					
					for(var j = 1 ; j<data[i].depth.length ; j++){
						depthstring += "|" + data[i].depth[j];
					}
					
					$('<input />').attr('type', 'hidden').attr('name', 'depth[]').attr('value', depthstring).appendTo(form);
				}

				}else{
					$('a.inter').each(function(){
						$('<input />').attr('type', 'hidden').attr('name', 'inter[]').attr('value', $(this).attr('id')).appendTo(form);
					});
				}
				return true;
			});
			
			$('#virtual-table').on('click','button.sort',function(){
				var id = $(this).parent().siblings().find('a.inter').attr('id');
				sortInters(id);
			});
		});
	</script>
</body>
</html>