<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
<!--  <link rel="stylesheet" href="http://code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">-->
<link rel="stylesheet" type="text/css"
	href="/resources/css/slick.grid.css">
<link rel="stylesheet" type="text/css"
	href="/resources/css/normalize.css">
<link rel="stylesheet" type="text/css"
	href="/resources/css/bootstrap-table.min.css">
<link rel="stylesheet" type="text/css"
	href="/resources/css/bootstrap-select.min.css">
<link rel="stylesheet" type="text/css" href="/resources/css/spinner.css">
<link rel="stylesheet" type="text/css"
	href="/resources/css/toastr.min.css" />

<style>
.cell-effort-driven {
	text-align: center;
}

.cell-reorder {
	cursor: move;
	background: url("images/drag-handle.png") no-repeat center center;
}

.cell-selection {
	border-right-color: silver;
	border-right-style: solid;
	background: #b4dcf0;
	color: gray;
	text-align: right;
	font-size: 10px;
}

.cell-h1 {
	font-weight: bold;
	background: #BBBBBB;
	text-transform: uppercase;
}

.cell-h2 {
	font-weight: bold;
	background: #CCCCCC;
}

.cell-h3 {
	font-style: italic;
	background: #DDDDDD;
}

.slick-row.selected .cell-selection {
	background-color: transparent;
	/* show default selected row background */
}

.recycle-bin {
	width: 120px;
	border: 1px solid gray;
	background: beige;
	padding: 4px;
	font-size: 12pt;
	font-weight: bold;
	color: black;
	text-align: center;
	-moz-border-radius: 10px;
}

.red {
	background: red;
}

.bold {
	font-weight: bold;
}

#outline {
	margin: 12px;
}

aside.right {
	position: fixed;
	right: 0px;
	width: 120px;
	text-align: right;
}

aside {
	display: block;
	position: absolute;
	height: 100%;
	overflow: hidden;
	z-index: 10;
	top: 223px;
	background-color: #ffffff;
}

#panel.affix {
	position: fixed;
	top: 133px;
	z-index: 10;
}

.subnav {
	margin: 0;
	top: 123px;
	z-index: 10;
	background-color: rgba(255, 255, 255, 0.8);
	border-bottom: 1px solid #E1E1E1;
	padding: 0px 0px 0px 0px;
	position: fixed;
	width: 100%;
}

.subnav.affix {
	position: fixed;
	top: 123;
	width: 100%;
	z-index: 10;
}

.navbar {
	margin-bottom: 0;
}

.pager2 {
	padding-left: 0;
	margin: 10px 0;
	text-align: center;
	list-style: none;
}

#wrap {
	position: absolute;
	right: 280px;
	top: 100px;
	visibility: hidden;
}

#fixed {
	position: fixed;
	width: 200px;
	height: 40px;
	margin-left: 0px;
	margin-top: 0px;
	z-index: 1000;
	text-align: left;
	font-size: 10pt;
}

.d1 {
	font-size: 8pt;
}

.d2 {
	font-size: 6pt;
}
/* unset bs3 setting */
.modal-open {
	overflow: auto;
}

h1 {
	font-size: 2em;
}

h2 {
	font-size: 1.5em;
}

h3 {
	font-size: 1em;
}

div {
	border-style: solid;
	border-width: 0px;
}

.linkCol {
	text-align: center;
}

.btn-group>.tooltip+.btn, .btn-group>.popover+.btn {
	margin-left: -1px;
}
</style>

<!-- slickgrid -->
<!--  <script type="text/javascript" src="/resources/js/slickgrid/jquery-1.11.1.min.js"></script>-->
<!--  <script type="text/javascript" src="/resources/js/slickgrid/jquery-ui.js"></script>-->
<script type="text/javascript"
	src="/resources/js/slickgrid/jquery.event.drag-2.2.js"></script>
<script type="text/javascript"
	src="/resources/js/slickgrid/jquery.event.drop-2.2.js"></script>

<script src="/resources/js/slickgrid/slick.core.js"></script>
<script src="/resources/js/slickgrid/plugins/slick.cellrangeselector.js"></script>
<script
	src="/resources/js/slickgrid/plugins/slick.cellselectionmodel.js"></script>
<script src="/resources/js/slickgrid/plugins/slick.rowselectionmodel.js"></script>
<script src="/resources/js/slickgrid/plugins/slick.rowmovemanager.js"></script>
<script src="/resources/js/slickgrid/slick.formatters.js"></script>
<script src="/resources/js/slickgrid/slick.editors.js"></script>
<script src="/resources/js/slickgrid/slick.grid.js"></script>
<script src="/resources/js/slickgrid/coelho.js"></script>

<!-- fraqs -->
<script src="/resources/js/fraqs/modernizr-2.8.3.min.js"></script>
<script src="/resources/js/fraqs/jquery.colors-1.6.0.min.js"></script>
<script src="/resources/js/fraqs/modplug-1.5.0.min.js"></script>
<script src="/resources/js/fraqs/jquery.fracs.js"></script>
<script src="/resources/js/fraqs/jquery.outline.js"></script>

<script src="/resources/js/bootstrap-table.min.js"></script>
<script src="/resources/js/bootstrap-select.min.js"></script>
<script src="/resources/js/jquery.popconfirm.js"></script>
<script src="/resources/js/toastr.min.js"></script>
<script src="/resources/js/jquery.form.js"></script>

<script>
var editionData = new Array();
<c:forEach var="inter" items='${virtualEdition.sortedInterps}'>
	editionDataItem = new Object();
	editionDataItem.fragment = ${inter.getFragment().externalId};
	editionDataItem.number = ${inter.number};
	editionDataItem.externalId = ${inter.externalId};
	
	<c:set var="newLine" value='
	'/>
	<c:set var="titleTemp" value="${fn:replace(inter.title,newLine,'')}" />
	<c:set var="newLine" value="
        "/>
	<c:set var="titleTemp" value="${fn:replace(titleTemp,newLine,'')}" />

	<c:choose>
		<c:when test="${fn:contains(titleTemp, '\"')}">
			editionDataItem.title ='${titleTemp}';
		</c:when>
		<c:otherwise>
			editionDataItem.title ="${titleTemp}";
    		</c:otherwise>
	</c:choose>
 	 	 
	 var listused = new Array();
	 <c:forEach var="used" items="${inter.getListUsed()}">
	 	used = new Object();
	 	used.externalId = ${used.externalId};
	 	used.shortName = "${used.shortName}";
	 	listused.push(used);
	 </c:forEach>
	 editionDataItem.listused = listused;
	 
	editionData.push(editionDataItem);
</c:forEach> 

console.log(editionData);
</script>
</head>
<body>

	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

	<spring:message code="virtualedition.tt.addFragment" var="addFragText" />
	<spring:message code="virtualedition.tt.deleteFragment"
		var="deleteFragText" />
	<spring:message code="virtualedition.tt.cut" var="cutText" />
	<spring:message code="virtualedition.tt.paste" var="pasteText" />
	<spring:message code="virtualedition.tt.moveup" var="moveUpText" />
	<spring:message code="virtualedition.tt.movedown" var="moveDownText" />
	<spring:message code="virtualedition.tt.movetop" var="moveTopText" />
	<spring:message code="virtualedition.tt.movebottom"
		var="moveBottomText" />
	<spring:message code="virtualedition.tt.moveposition" var="movePosText" />
	<spring:message code="virtualedition.tt.select" var="selectText" />
	<spring:message code="virtualedition.tt.save" var="saveText" />

	<c:set var="userLdoD"
		value='${pageContext.request.userPrincipal.principal.getUser()}' />

	<!-- subnav here -->
	<div class="navbar subnav" role="navigation">
		<div class="navbar-inner">
			<div class="container">
				<ul class="pager2 subnav-pager">
					<div class="btn-group-wrap">
						<div class="btn-group" role="group" aria-label="...">
							<button class="btn btn-default tip" type="button"
								title="${addFragText}" data-toggle="modal"
								data-target="#searchmodal" aria-expanded="false"
								aria-controls="collapsemenu" rel="tooltip"
								data-original-title='Hello'>
								<span class="glyphicon glyphicon-plus"
									aria-hidden="Adicionar fragmentos"></span>
							</button>
						</div>
						<div class="btn-group" role="group" aria-label="...">
							<button type="button" id="del" class="btn btn-default tip"
								title="${deleteFragText}">
								<span class="glyphicon glyphicon-trash"
									aria-hidden="Remover fragmentos"></span>
							</button>
						</div>
						<div class="btn-group" role="group">
							<button type="button" id="cut" class="btn btn-default tip"
								title="${cutText}">
								<span class="glyphicon glyphicon glyphicon-scissors"></span>
							</button>
							<button type="button" id="paste" class="btn btn-default tip"
								title="${pasteText}">
								<span class="glyphicon glyphicon glyphicon-paste"></span>
							</button>
						</div>
						<div class="btn-group" role="group" aria-label="...">
							<button type="button" id="up" class="btn btn-default tip"
								title="${moveUpText}">
								<span class="glyphicon glyphicon-chevron-up" aria-hidden="up"></span>
							</button>
							<button type="button" id="down" class="btn btn-default  tip"
								title="${moveDownText}">
								<span class="glyphicon glyphicon-chevron-down"
									aria-hidden="down"></span>
							</button>
							<button type="button" id="top" class="btn btn-default tip"
								title="${moveTopText}">
								<span class="glyphicon glyphicon-arrow-up" aria-hidden="top"></span>
							</button>
							<button type="button" id="bottom" class="btn btn-default tip"
								title="${moveBottomText}">
								<span class="glyphicon glyphicon-arrow-down"
									aria-hidden="bottom"></span>
							</button>
						</div>

						<div class="btn-group popover-markup">
							<a href="#" class="trigger btn btn-default tip"
								title="${movePosText}" style="border-radius: 5px"> <span
								class="glyphicon glyphicon-arrow-right" aria-hidden="up"></span></a>
							<div class="head hide">
								Posicionar
								<div class="closepopover"
									style="position: absolute; right: 8px; top: 5px">
									<button type="button" id="closepopover" class="close tip"
										aria-label="Close" onclick="$('.popover').popover('hide');">
										<span aria-hidden="true">&times;</span>
									</button>
								</div>
							</div>
							<div class="content hide">
								<div class=".form-horizontal " style="width: 140px">
									<div class="col-sm-6"
										style="padding-left: 0px; padding-right: 0px; padding-bottom: 12px; padding-top: 4px;">
										<input id="movetopos" type="number" value="1" min="1"
											class="form-control" onkeyup="moveEnter(event)">
									</div>
									<div class="col-sm-6"
										style="padding-left: 12px; padding-right: 0px; padding-bottom: 12px; padding-top: 4px;">
										<button onclick="moveFragments()"
											class="btn btn-default btn-block" id="moveok">OK</button>
									</div>
								</div>
							</div>
						</div>

						<!--
                <button type="button" id="up" class="btn btn-default">Cima</button>
                <button type="button" id="down" class="btn btn-default">Baixo</button>
                <button type="buttom" id="top" class="btn btn-default">Inicio</button>
                <button type="button" id="bottom" class="btn btn-default">Fim</button>
                -->


						<!-- Single button -->
						<!--  
                <div class="btn-group">
                  <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                    <span class="glyphicon glyphicon-arrow-right" aria-hidden="up"></span><span class="caret"></span>
                  </button>
                  <div class="dropdown-menu" role="menu" style="padding:15px">
                  	Posição:
                    <input id="movetopos" type="number" value="0" min="0" class="form-control">
                	<br>
                    <button type="button" id="moveok" class="btn btn-default pull-right">OK</button>
                    <!--  <li class="divider"></li>
                    <li><div id="slider2"></div></li>
                    <li class="divider"></li>-->
						<!--  </div>-->


						<!--  
                   <div class="dropdown-menu" style="padding:17px;">
	             
	                <input name="username" id="username" type="text" placeholder="Username"> 
	                <input name="password" id="password" type="password" placeholder="Password"><br>
	                <button type="button" id="btnLogin" class="btn">Login</button>  
	            	</div>
	              -->
						<!--  </div>-->




						<!--
                
                 <div class="btn-group" role="group" aria-label="...">
                  <button type="button" class="btn btn-default" data-toggle="modal" data-target=".bs-example-modal-addsection2">
                <span class="glyphicon glyphicon-text-height" aria-hidden="Adicionar secção"></span>
                </button>
              </div>
                
                <button type="button" class="btn btn-default" data-toggle="modal" data-target=".bs-example-modal-addsection2">
                Secção
                </button>
               
                 <div class="btn-group" role="group" aria-label="...">
                  <button type="button"  id="preview" class="btn btn-default" data-toggle="modal" data-target="#myModal">
                <span class="glyphicon glyphicon-th-list" aria-hidden="Previsualizar Índice"></span>
                </button>
              </div>
 					-->
						<!--
               <div class="btn-group" role="group" aria-label="...">
                   <button type="button" id="save" class="btn btn-default">
                <span class="glyphicon glyphicon-cloud-upload" aria-hidden="Guardar"></span>
                </button>
              </div>
              <!--
                <button type="button"  id="preview" class="btn btn-default" data-toggle="modal" data-target="#myModal">
                Preview
                </button>
                <button type="button" id="save" class="btn btn-default">Save</button>
                -->



						<div class="btn-group" role="group" aria-label="...">
							<input id="tname" type="text" class="btn btn-default"
								placeholder="${selectText}" style="font-size: 0.8em;">
						</div>
						<button type="button" id="savebutton" class="btn btn-primary tip"
							data-loading-text="<div class='spinner-loader' style='font-size:3.5px'></div>"
							title="${saveText}"
							onclick="$('#savebutton').button('loading');$('#formedition').submit();">
							<span class="glyphicon glyphicon-floppy-disk"
								aria-hidden="bottom"></span>
						</button>

						<!--<br><br>
                <div id="slider2"></div>-->

					</div>
				</ul>
			</div>
		</div>
	</div>
	<br>


	<!-- Modal -->
	<div class="modal fade" id="searchmodal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Add New Fragment</h4>
				</div>
				<div class="modal-body">
					<div class="form-group">
						<div class="col-sm-4">
							<input type="text" class="form-control tip" id="query"
								title="pesquisa x" placeholder="Search for...">
						</div>
						<div class="col-sm-3">
							<div class="tip" title="text div">
								<select class="selectpicker" data-width="100%" id="searchType"
									title="teste x">
									<option value="">Pesquisa completa</option>
									<option value="title">Pesquisa por título</option>
								</select>
							</div>
						</div>
						<div class="col-sm-3">
							<select class="selectpicker" data-width="100%" id="sourceType">
								<option value="">Tipos de fonte</option>
								<option value="Coelho">Jacinto do Prado Coelho</option>
								<option value="Cunha">Teresa Sobral Cunha</option>
								<option value="Zenith">Richard Zenith</option>
								<option value="Pizarro">Jerónimo Pizarro</option>
								<option value="BNP">Fontes Autorais</option>

							</select>
						</div>
						<div class="col-sm-2">
							<button class="btn btn-default" type="button" id="searchbutton">
								<span class="glyphicon glyphicon-search"></span> Search
							</button>
						</div>
					</div>
					<br>

					<!--
      <table data-toggle="table">
        <thead>
        <tr>
        	<th data-field="state" data-checkbox="true"></th>
            <th>Fragments</th>
            <th>Interpretations</th>
        </tr>
        </thead>
        <tbody>
        <tr>
	       	<td data-checkbox="true"></td>
	       	<td>a</td>
	       	<td>b</td>
        </tr>
        <tr>
	       	<td data-checkbox="true" data-val="true"></td>
	       	<td>a</td>
	       	<td>b</td>
        </tr>
          <tr>
	       	<td data-checkbox="true"></td>
	       	<td>a</td>
	       	<td>b</td>
       
        </tbody>
    </table>  -->

					<div id="searchresult"
						style="display: none; width: 100%; text-align: center;">
						<hr>
						<div class="spinner-loader">Loading...</div>
					</div>

				</div>
				<div class="modal-footer" style="display: none">
					<button type="button" class="btn btn-primary" id="addfragments">Add</button>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade bs-example-modal-addsection2" id="sectionmodal2">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Adicionar Secção</h4>
				</div>
				<div class="modal-body">
					<input id="sectionname" type="text" class="btn btn-default"
						style="width: 100%; text-align: left;">
				</div>
				<div class="modal-footer">
					<select id="titleid">
						<option value="1">T1</option>
						<option value="2">T2</option>
						<option value="3">T3</option>
					</select>
					<button type="submit-button" id="okaddsection2"
						class="btn btn-primary">OK</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->

	<!-- Modal -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Edição Virtual</h4>
				</div>
				<div class="modal-body" id="modalpreview">...</div>

			</div>
		</div>
	</div>
	<br>
	<br>
	<br>


	<div class="container">
		<div class="row col-md-1">
			<form class="form-inline" method="GET"
				action="${contextPath}/virtualeditions/restricted/manage/${virtualEdition.externalId}">
				<input type="hidden" name="${_csrf.parameterName}"
					value="${_csrf.token}" />
				<button type="submit" class="btn btn-default">
					<span class="glyphicon glyphicon-arrow-left"></span>
					<spring:message code="general.back" />
				</button>
			</form>
		</div>

		<div class="row col-md-12">
			<h1 class="text-center">
				<span id="editiontitle">${virtualEdition.title}</span>
			</h1>
		</div>
		<!-- 
        <div class="row pull-right">
            <form class="form-inline" method="GET"
                action="${contextPath}/virtualeditions">
                <button type="submit" class="btn btn-primary">
                    <span class="glyphicon glyphicon-th-list"></span>
                    <spring:message code="virtual.editions" />
                </button>
            </form>
        </div>
		 -->
		<div class="row col-md-12">
			<div class="collapse" id="collapsemenu">
				<div class="well" style="height: 70px">
					<form class="form-inline" role="form" method="POST"
						id="formedition"
						action="/virtualeditions/restricted/reorder/${externalId}">
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" />
						<div class="form-group pull-right"
							style="padding-right: 0px; padding-left: 0px">
							<input type="hidden" name="fraginters" value="" id="fraginters">
							<button type="submit" class="btn btn-primary" id="submit">
								<span class="glyphicon glyphicon-ok"></span>
							</button>
						</div>

					</form>
				</div>
			</div>
		</div>
		<br>
		<div class="row col-md-12">
			<div id="myGrid"></div>
			<br> <br>
		</div>
	</div>
	<aside class="right"> <canvas id="outline" width="200"
		height="400"></canvas> </aside>
</body>
<script>
 // Only run on browsers that support css transitions
// See also example.css:15
var linkFormatter = function (row, cell, value, columnDef, dataContext) {
        //console.log(dataContext);dataContext.externalId
        
  //console.log(dataContext);
  if(dataContext.externalId)
  if(dataContext.h == 0) {
    return '<a href="/fragments/fragment/inter/'+dataContext.externalId+'" target="_blank"><span style="text-align:center" class=\'glyphicon glyphicon-link\'></a>';
  } else if(dataContext.h > 0) {
    return '<a href="" title="edit title"><span style="text-align:center" class=\'glyphicon glyphicon-pencil\'></a>';
  }
};
var grid;
var data = [];
var columns = [
 /* {
    id: "#",
    name: "",
    width: 40,
    behavior: "selectAndMove",
    selectable: false,
    resizable: false,
    cssClass: "cell-reorder dnd"
  },*/
   {
    id: "id",
    name: "",
    field: "id",
    width: 40,
    behavior: "selectAndMove",
    selectable: true,
    resizable: false,
    sortable: true,
  },
  {
    id: "name",
    name: "Título",
    field: "name",
    behavior: "selectAndMove",
    cssClass: "cell-title",
    width: 400,
    sortable: true
  },
  {
    id: "edition",
    name: "Edição",
    field: "edition",
    behavior: "selectAndMove",
    sortable: true 
  }  ,
  {
    id: "link",
    name: "Link",
    field: "link",
    behavior: "selectAndMove",
    width: 20,
    formatter: linkFormatter,
    cssClass: "linkCol"
  }
];
var options = {
  editable: false,
  enableAddRow: false,
  enableCellNavigation: true,
  forceFitColumns: false,
  autoEdit: false,
  autoHeight: true,
  forceSyncScrolling:true,
  autoExpandColumns: true,
  enableColumnReorder: false,
  multiColumnSort: true
};
function requiredFieldValidator(value) {
  if (value == null || value == undefined || !value.length) {
    return {valid: false, msg: "This is a required field"};
  } else {
    return {valid: true, msg: null};
  }
}
var data = [];
$(function () {
 
  
    for (var i = 0; i < editionData.length; i++) {
    	
      editionStr = "";
      if (editionData[i].listused[0]!=null)
     	for(j=0;j<editionData[i].listused.length;j++)
     		editionStr = editionStr + "> " + editionData[i].listused[j].shortName + " ";	
     	
      taxStr = "";
     	     		
      data[i] = {
        id: i+1,
        //name: "Name " + i,
        fragment:editionData[i].fragment,
        name: editionData[i].title,
        date: editionData[i].date,
        tax: taxStr,
        //edition: editionData[i].listused[0].shortName,
        edition:editionStr,
        usedExternalId: editionData[i].listused[0].externalId,
        externalId:editionData[i].externalId,
        link: "link",
        //complete: Math.random() < 0.5 ? true : false,
         //complete: Math.random() < 0.5 ? true : false,
        h:0
      };
      	
  
    }
  grid = new Slick.Grid("#myGrid", data, columns, options);
 
  grid.onSort.subscribe(function (e, args) {
      var cols = args.sortCols;
      data.sort(function (dataRow1, dataRow2) {
        for (var i = 0, l = cols.length; i < l; i++) {
          var field = cols[i].sortCol.field;
          var sign = cols[i].sortAsc ? 1 : -1;
          var value1 = dataRow1[field], value2 = dataRow2[field];
          var result = (value1 == value2 ? 0 : (value1 > value2 ? 1 : -1)) * sign;
          if (result != 0) {
            return result;
          }
        }
        return 0;
      });
      grid.invalidate();
      grid.render();
    });
  grid.setSelectionModel(new Slick.RowSelectionModel());
  var moveRowsPlugin = new Slick.RowMoveManager({
    cancelEditOnDrag: true
  });
  
  
  moveRowsPlugin.onBeforeMoveRows.subscribe(function (e, data) {
    for (var i = 0; i < data.rows.length; i++) {
      // no point in moving before or after itself
      if (data.rows[i] == data.insertBefore || data.rows[i] == data.insertBefore - 1) {
        e.stopPropagation();
        return false;
      }
    }
    return true;
  });
  moveRowsPlugin.onMoveRows.subscribe(function (e, args) {
    var extractedRows = [], left, right;
    var rows = args.rows;
    var insertBefore = args.insertBefore;
    left = data.slice(0, insertBefore);
    right = data.slice(insertBefore, data.length);
    rows.sort(function(a,b) { return a-b; });
    //console.log("rows "+rows);
    //console.log("left "+left);
    //console.log("right "+right);
    // console.log("insertBefore "+insertBefore);
    for (var i = 0; i < rows.length; i++) {
      extractedRows.push(data[rows[i]]);
    }
    rows.reverse();
    for (var i = 0; i < rows.length; i++) {
      var row = rows[i];
      if (row < insertBefore) {
        left.splice(row, 1);
      } else {
        right.splice(row - insertBefore, 1);
      }
    }
    data = left.concat(extractedRows.concat(right));
    var selectedRows = [];
    var cutRows = [];
    for (var i = 0; i < rows.length; i++)
      selectedRows.push(left.length + i);
    grid.resetActiveCell();
    grid.setData(data);
    grid.setSelectedRows(selectedRows);
    
    grid.invalidate();
    //console.log("onMoveRows");
    $("#outline").fracs("outline",'redraw');
  });
  grid.registerPlugin(moveRowsPlugin);
  grid.onDragInit.subscribe(function (e, dd) {
    // prevent the grid from cancelling drag'n'drop by default
    e.stopImmediatePropagation();
  });
  grid.onDragStart.subscribe(function (e, dd) {
    var cell = grid.getCellFromEvent(e);
    if (!cell) {
      return;
    }
    dd.row = cell.row;
    if (!data[dd.row]) {
      return;
    }
    
    if (Slick.GlobalEditorLock.isActive()) {
      return;
    }
    e.stopImmediatePropagation();
    dd.mode = "recycle";
    var selectedRows = grid.getSelectedRows();
    if (!selectedRows.length || $.inArray(dd.row, selectedRows) == -1) {
      selectedRows = [dd.row];
      grid.setSelectedRows(selectedRows);
    }
    dd.rows = selectedRows;
    dd.count = selectedRows.length;
    var proxy = $("<span></span>")
        .css({
          position: "absolute",
          display: "inline-block",
          padding: "4px 10px",
          background: "#e0e0e0",
          border: "1px solid gray",
          "z-index": 99999,
          "-moz-border-radius": "8px",
          "-moz-box-shadow": "2px 2px 6px silver"
        })
        .text("Drag to Recycle Bin to delete " + dd.count + " selected row(s)")
        .appendTo("body");
    dd.helper = proxy;
    $(dd.available).css("background", "pink");
    return proxy;
  });
  grid.onDrag.subscribe(function (e, dd) {
    if (dd.mode != "recycle") {
      return;
    }
   
    dd.helper.css({top: e.pageY + 5, left: e.pageX + 5});
  });
  grid.onDragEnd.subscribe(function (e, dd) {
    if (dd.mode != "recycle") {
      return;
    }
    dd.helper.remove();
    $(dd.available).css("background", "beige");
    
  });
  $.drop({mode: "mouse"});
  $("#dropzone")
      .bind("dropstart", function (e, dd) {
        if (dd.mode != "recycle") {
          return;
        }
        $(this).css("background", "yellow");
      })
      .bind("dropend", function (e, dd) {
        if (dd.mode != "recycle") {
          return;
        }
        $(dd.available).css("background", "pink");
      })
      .bind("drop", function (e, dd) {
        if (dd.mode != "recycle") {
          return;
        }
        var rowsToDelete = dd.rows.sort().reverse();
        for (var i = 0; i < rowsToDelete.length; i++) {
          data.splice(rowsToDelete[i], 1);
        }
        grid.invalidate();
        grid.setSelectedRows([]);
      });
  grid.onAddNewRow.subscribe(function (e, args) {
    var item = {name: "New task", complete: false};
    $.extend(item, args.item);
    data.push(item);
    grid.invalidateRows([data.length - 1]);
    grid.updateRowCount();
    grid.render();
  });
})
function sortNumber(a,b) {
    return a - b;
}
function removeRows(selectedRows) {
  
 //console.log("removeRows "+selectedRows);
 for (var i = 0; i < selectedRows.length; i++) {
    data.splice(selectedRows[i]-i, 1);
  }
  grid.invalidate();
  grid.setSelectedRows([]);
}
// remove function
$("#del").click(function() {
  var selectedRows = grid.getSelectedRows().sort(sortNumber);
  removeRows(selectedRows);
  $("#outline").fracs("outline",'redraw');
});
$("#del").popConfirm({
	title: "Are you sure?",
	content: "",
	placement: "bottom"
});
$( "#down" ).click(function() {
  selectedRows = grid.getSelectedRows().sort(sortNumber);
  moveRowsTo(selectedRows,selectedRows[selectedRows.length-1]+2);
  window.scrollTo(0,$('.selected').offset().top-300);
});
$( "#up" ).click(function() {
  selectedRows = grid.getSelectedRows().sort(sortNumber);
  if(selectedRows.length>0)
    if((selectedRows[0]-1)>=0) {
      moveRowsTo(selectedRows,selectedRows[0]-1);
      window.scrollTo(0,$('.selected').offset().top-300);
    }
});
$( "#top" ).click(function() {
  var selectedRows = grid.getSelectedRows().sort(sortNumber);
  if(selectedRows.length>0) {
    moveRowsTo(selectedRows,0);
    window.scrollTo(0,$('.selected').offset().top-300);
  }  
});
$( "#bottom" ).click(function() {
  var selectedRows = grid.getSelectedRows().sort(sortNumber);
  if(selectedRows.length>0) {
    moveRowsTo(selectedRows,data.length);
    window.scrollTo(0,$('.selected').offset().top-300);
  }
});
$( "#cut" ).click(function() {cut();});
$( "#paste" ).click(function() {paste();});
	
/*
$( "#moveok" ).click(function() {
  console.log("teste "+$('#movetopos').val());
  pos = $('#movetopos').val()
  if($.isNumeric(pos) && pos >= 0) {
	  var selectedRows = grid.getSelectedRows().sort(sortNumber);
	  if(selectedRows.length>0) {
	    moveRowsTo(selectedRows,$('#movetopos').val());
	    window.scrollTo(0,$('.selected').offset().top-300);
	  }
  }
});
*/
function moveFragments () {
	  //console.log("teste "+$('#movetopos').val());
	  pos = $('#movetopos').val()
	  if($.isNumeric(pos) && pos > 0) {
		  var selectedRows = grid.getSelectedRows().sort(sortNumber);
		  if(selectedRows.length>0) {
			  
			
			if (pos <= selectedRows[0]) 
				pos = pos-1;
			//console.log("selected: "+pos)
		    moveRowsTo(selectedRows,pos);
		    window.scrollTo(0,$('.selected').offset().top-300);
		  }
	  }
	  
	  $('.popover').popover('hide');
}
function moveEnter(enter) {
	if(event.keyCode == 13)
		$('#moveok').click();
}
$("#okaddsection2").click(function() {
  sectionname = $('#sectionname').val();
  level = $('#titleid').val();
  
  $('#sectionmodal2').modal('toggle');
  addNewSection(sectionname,level);
});
$('#searchmodal').on('shown.bs.modal', function () {
    $('#searchquerybt').focus()
})
$('#sectionmodal2').on('shown.bs.modal', function () {
    $('#sectionname').focus()
})
$( "#save" ).click(function() {
  //console.log(data);
  //alert(JSON.stringify(data));
});
$("#preview").click(function() {
  datastr = "";
  for (var i = 0; i < data.length; i++) {
        if (data[i].h > 0)
          datastr = datastr+ "<h"+data[i].h+">"+data[i].name+"</h"+data[i].h+">";
        else 
          datastr = datastr+ data[i].name+"<br>";
  //   var row = rows[i];
  }
  $("#modalpreview").html(datastr);
  //id="modalpreview"
});
function moveRowsTo(rows, insertBefore) {
    var extractedRows = [], left, right;
    left = data.slice(0, insertBefore);
    right = data.slice(insertBefore, data.length);
    rows.sort(function(a,b) { return a-b; });
    for (var i = 0; i < rows.length; i++) {
      extractedRows.push(data[rows[i]]);
    }
    rows.reverse();
    for (var i = 0; i < rows.length; i++) {
      var row = rows[i];
      if (row < insertBefore) {
        left.splice(row, 1);
      } else {
        right.splice(row - insertBefore, 1);
      }
    }
    data = left.concat(extractedRows.concat(right));
    var selectedRows = [];
    for (var i = 0; i < rows.length; i++)
      selectedRows.push(left.length + i);
    grid.resetActiveCell();
    grid.setData(data);
    grid.setSelectedRows(selectedRows);
    
    grid.invalidate();
    //console.log("onMoveRows");
    $("#outline").fracs("outline",'redraw');
  };
//update data to submit
  $( "#formedition" ).submit(function( event ) {
    
	//alert(data[0].usedExternalId);
	 
    usedIDS = "";
    for (var i = 0; i < data.length; i++) {
    	usedIDS = usedIDS + data[i].usedExternalId + ";";
    }
    
    $( "#fraginters" ).val(usedIDS);
    //console.log(usedIDS);
    
  });
</script>


<script>
$("#outline").fracs("outline", {
    crop: true,
    styles: [
        {
            selector: 'header,footer,section,article',
            fillStyle: 'rgb(230,230,230)'
        },
        /*{
            selector: 'h1',
            fillStyle: 'rgb(200,200,200)'
        },*/
        {
            selector: '#myGrid',
            fillStyle: 'rgb(238,238,238)'
        },
        {
            selector: '.selected',
            fillStyle: 'rgb(180,220,240)'
        }, 
        {
            selector: '.slick-reorder-proxy',
            fillStyle: 'rgb(80,160,240)'
        }, 
        {
            selector: '.cell-h1',
            fillStyle: 'rgb(136,136,136)'
        }, 
        {
            selector: '.cell-h2',
            fillStyle: 'rgb(170,170,170)'
        }, 
        {
            selector: '.cell-h3',
            fillStyle: 'rgb(204,204,204)'
        }
        /*
        {
            selector: '.slick-reorder-guide',
            fillStyle: 'rgb(255,0,255)'
        }*/
        
        
    ]
});
$( "#outline" ).mousemove(function( event ) {
	  var parentOffset = $(this).parent().offset(); 
	  var pageCoords = "( " + event.pageX + ", " + event.pageY + " )";
	  var clientCoords = "( " + event.clientX + ", " + event.clientY + " )";
	  my = event.pageY-parentOffset.top-12;
	  h = $("#outline").height();
	  //console.log(my/h*data.length);
	  id = Math.floor(my/h*data.length);
	  //console.log(id);
	  $('.wrap').css({top:event.clientY+'px'});
	  
	if(id>2 && id<data.length-2)
	  $('.wrap').attr("data-content",data[id].name+"\n"+data[id].name+"\n"+data[id].name);
	  //$('.wrap').popover('show');
	  //$("#fixed").html("<span class=\"d2\">"+data[id-2].name+"</span><br>"+"<span class=\"d1\">"+data[id-1].name+"</span><br>"+data[id].name+"<br>"+"<span class=\"d1\">"+data[id+1].name+"</span><br>"+"<span class=\"d2\">"+data[id+2].name+"</span>");
	  if(id>2 && id<data.length-2)
	  $("#fixed").html("<span class=\"d2\">"+data[id-2].name.substring(0,42)+"...</span><br>"+"<span class=\"d1\">"+data[id-1].name.substring(0,32)+"...</span><br>"+data[id].name.substring(0,26)+"...<br>"+"<span class=\"d1\">"+data[id+1].name.substring(0,26)+"...</span><br>"+"<span class=\"d2\">"+data[id+2].name.substring(0,26)+"...</span>");
	  //$("#wrap")[0].style.top =  $("#wrap")[0].style.top + 1;
	  //$("#label").text(data[id].name);
	  //console.log(id);
	   //$( "span:first" ).text( "( event.pageX, event.pageY ) 
	  
	})
	.mouseenter(function( event ) {
	  $('.wrap').css("visibility", "visible");
	  //$('.wrap').fadeIn( 100 );
	})
	.mouseleave(function() {
	  $('.wrap').css("visibility", "hidden");
	  //$('.wrap').fadeOut( 100 );
	});
	function refreshSwatch() {
	  //console.log($( "#slider2" ).slider( "value" ));
	  //pos = $( "#slider2" ).slider( "value" );
	  //.sort();
	  //selectedRows = selectedRows.reverse();
	  var selectedRows = grid.getSelectedRows().sort(sortNumber);
	  if(selectedRows.length>0) {
	    moveRowsTo(selectedRows,pos);
	    window.scrollTo(0,$('.selected').offset().top-300);
	  }  
	  /*
	  console.log(selectedRows[0])
	  d = data[selectedRows[0]];
	  data[selectedRows[0]] = data[pos];
	  data[pos] = d;
	  grid.invalidate();
	  sr = [pos];
	  grid.setSelectedRows(sr);
	  */
	  $("#outline").fracs("outline",'redraw');
	  //window.scrollTo(0,$('.selected').offset().top-300);
	}
	/*
	$(function() {
	    $( "#slider2" ).slider({
	      max: data.length,
	      slide: refreshSwatch,
	      change: refreshSwatch
	    });
	});*/
	var selectedrows;
	$( "#tname" ).bind("keyup change",function( event ) {
	  selectedrows = [];
	  grid.setSelectedRows(selectedrows);
	  str = $("#tname").val();
	  //console.log("-"+str+"-");
	  if (str.length > 1) {
	  
	    for (var i = 0; i < data.length; i++) {
	      if(data[i].name.toUpperCase().indexOf(str.toUpperCase()) > -1)
	        selectedrows.push(i);
	    }
	    grid.setSelectedRows(selectedrows);
	  }
	  $("#outline").fracs("outline",'redraw');
	});
	function cut() {
	   selectedRows = grid.getSelectedRows().sort(sortNumber); 
	    
	  if (selectedRows.length > 0) {
	    cutRows = [];
	    for (var i = 0; i < selectedRows.length; i++) {
	      cutRows.push(data[selectedRows[i]]);
	    }
	    removeRows(selectedRows);
	    selectedRows = [];
	    $("#outline").fracs("outline",'redraw');
	  }
	}
	function copy() {
		/*
	    selectedRows = grid.getSelectedRows().sort(sortNumber); 
	    
	    if (selectedRows.length > 0) {
	    cutRows = [];
	    for (var i = 0; i < selectedRows.length; i++) {
	      cutRows.push(data[selectedRows[i]]);
	    }
	    //selectedRows = [];
	    $("#outline").fracs("outline",'redraw');
	  }
	    */
	}
	function paste() {
	  
	    selectedRows = grid.getSelectedRows().sort(sortNumber); 
	    
	    var left, right, paste = [];
	    var insertAt = 0;
	    //console.log(insertAt);
	    //console.log(selectedRows);
	    //var paste =  cutRows.slice();
	    //var paste = [].concat(cutRows);
	    
	    if(cutRows.length > 0) {
	     var paste = JSON.parse(JSON.stringify(cutRows))
	    if(selectedRows.length > 0) 
	      insertAt = selectedRows[selectedRows.length-1]+1;
	    left = data.slice(0, insertAt);
	    right = data.slice(insertAt, data.length);
	    data = left.concat(paste.concat(right));
	    selectedRows = Array(paste.length);
	    for (var i = 0; i < paste.length; i++) {
	      selectedRows[i] = left.length+i;
	    }
	  
	    for (var i = 0; i < data.length; i++) {
	        data[i].id = i+1;
	        //console.log(data[i]);
	    }
	    grid.setData(data);
	    grid.setSelectedRows(selectedRows);
	    
	    grid.invalidate();
	    
	    cutRows = [];
	    $("#outline").fracs("outline",'redraw');
	    }
	   
	}
	function addNewSection (sectionname,level) {
	  var item = {name: sectionname, complete: false, h:level};
	  //var item = {name: ""sectionname"", complete: true};
	  selectedRows = grid.getSelectedRows().sort(sortNumber); 
	  var insertAt = 0;
	  if(selectedRows.length > 0) 
	      insertAt = selectedRows[selectedRows.length-1]+1;
	   
	  data.splice(insertAt, 0, item);
	  
	  
	  grid.invalidate();
	  grid.setActiveCell(insertAt, 1);
	  //$('.cell-title')[insertAt].className = $('.cell-title')[insertAt].className+" cell-h1";
	  $("#outline").fracs("outline",'redraw');
	    //grid.updateRowCount();
	    //grid.render();
	}
	
	function addNewFragment (externalid,name,edition,fragExternalId) {
		  var item = {date:"",fragment:fragExternalId,edition:edition,link:"link",tax:"",usedExternalId:externalid, name:name,complete: false, h:0};
		  //var item = {name: ""sectionname"", complete: true};
		  selectedRows = grid.getSelectedRows().sort(sortNumber); 
		  var insertAt = 0;
		  if(selectedRows.length > 0) 
		      insertAt = selectedRows[selectedRows.length-1]+1;
		   
		  data.splice(insertAt, 0, item);
		  
		  
		  grid.invalidate();
		  grid.setActiveCell(insertAt, 1);
		
		  $("#outline").fracs("outline",'redraw');

		}
	$(window).keydown(function (e){
	    if (e.metaKey && e.keyCode == 88) 
	      cut();
	});
	$(window).keydown(function (e){
	    if (e.metaKey && e.keyCode == 86) 
	      paste();
	});
	$(window).keydown(function (e){
	    if (e.metaKey && e.keyCode == 67) 
	      copy();
	});
	
	$('.subnav').affix({
	      offset: {
	        top: $('.navbar-fixed-top').height()
	      }
	}); 
	//-------------------------------
	    
	    //---------------------------------
	    
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
				url :  "/search/simple/result",
				data : data,
				contentType : 'text/plain;charset=UTF-8',
				success : function(html) {
					
					$('#searchresult').empty().append("<hr>"+html);
					$('#tablesearchresults').attr("data-pagination","true");
					$('#tablesearchresults').attr("data-search","true");
					$('#tablesearchresults').bootstrapTable();
					$('#tablesearchresults').bootstrapTable('showColumn', 'state');
					$('#tablesearchresults').show();
					$(".modal-footer").show();
					//$('.result-table').dataTable({
					//	'paging' : false
					//)});
				}
			});
		});
		
	    $('#addfragments').click(function() {
	    	
	    	selectedInters = $('#tablesearchresults').bootstrapTable('getSelections');
	    	var duplicate = false;
	    	var strDuplicate = "";
	    	for (var i = 0; i < selectedInters.length; i++) {
	    	
	    		duplicate = false;
	    	
	    		for (var j = 0; j < data.length; j++) {
	    			
	    			//console.log("length: "+data.length +" "+data[j].fragment);
	    			if (data[j].fragment == selectedInters[i][4]) {
	    				//console.log("REPETIDO "+selectedInters[i][2] + selectedInters[i][3]);
	    				strDuplicate = strDuplicate + selectedInters[i][2] + " ( " + selectedInters[i][3]+ ")<br>"; 
	    				duplicate = true;
	    			}
	    		}
	    		
	    		if (!duplicate)
	    		addNewFragment(selectedInters[i][1],selectedInters[i][2],selectedInters[i][3],selectedInters[i][4]);
	   	    }
	    	
	    	/*
	    	$('input[name="selectedinters"]:checked').each(function() {
	    		   console.log(this.value); 
	    		   addNewFragment($("#title"+this.value).val(),this.value);
	    	});
	    	*/
	    	
	    	
	    	
	    	
	    	$('#searchmodal').modal('toggle');
	    	
	    	if(strDuplicate.length > 0) {
	    		toastr["warning"](strDuplicate, "Fragmentos repetidos:")	
	    		//alert("Fragmentos duplicados:<br>"+strDuplicate);
	    	}
	    		
	    });
		$('.dropdown-menu').click(function(event) {event.stopPropagation();});
		
		$('.popover-markup>.trigger').popover({
		    html: true,
		    title: function () {
		        return $(this).parent().find('.head').html();
		    },
		    content: function () {
		        return $(this).parent().find('.content').html();
		    }
		});
		
		$('.popover-markup').on('shown.bs.popover', function () {
			
			
			var selectedRows = grid.getSelectedRows().sort(sortNumber);
			//console.log("l "+selectedRows.length);
			
			if(selectedRows.length == 0) {
				$('#movetopos').prop('disabled', true);
				$('#moveok').prop('disabled', true);
			} 
			else
			{
				$('#movetopos').prop('disabled', false);
				$('#moveok').prop('disabled', false);
				$('#movetopos').val(selectedRows[0]+1);
				$('#movetopos').focus();
			}
						
		});
		
		$('#closepopover').click(function(event) {$('.popover').popover('hide');});
		
		$(".tip").tooltip({placement: 'bottom'});
		
		$(document).ready(function() { 
            // bind 'myForm' and provide a simple callback function 
            $('#formedition').ajaxForm(function() { 
                //alert("Thank you for your comment!");
                $("#editiontitle").html($("#title").val());
                $("#savebutton").button("reset");
            }); 
        }); 
		
		
</script>
</html>