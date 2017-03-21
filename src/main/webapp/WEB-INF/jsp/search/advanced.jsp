<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
<script type="text/javascript" src="/resources/js/riot.min.js"></script>
<!--<script type="text/javascript" src="/resources/js/jquery.dataTables.min.js"></script>-->
<!-- <script type="text/javascript" src="/resources/js/form-domain-model.js"></script> -->

<link rel="stylesheet" type="text/css"
	href="/resources/css/bootstrap-table.min.css">
<link rel="stylesheet" type="text/css" href="/resources/css/spinner.css">

<script src="/resources/js/bootstrap-table.min.js"></script>

<script type="text/javascript">
	/* for default options or options that expands the ones present in the
	 domain model
	 */
	option = {
		all : {
			id : "all",
			text : '<spring:message javaScriptEscape="true" code="search.option.all" />'
		},
		none : {
			id : "none",
			text : "None"
		}
	};

	//Edition definition
	function Edition(id) {

		// for default options or options that expands the ones present in the
		// domain model
		options = {
			edition : {
				all : "all",
				none : "none"
			}
		};

		this.id = id;
		this.inclusionOption = "in";
		this.selectedEdition = option.all.id;
		this.heteronym = null;
		this.date = null;
	};

	Edition.prototype.json = function() {
		return {
			type : "edition",
			//	id 			: this.id,
			inclusion : this.inclusionOption,
			edition : this.selectedEdition,
			heteronym : this.heteronym == null ? null : this.heteronym.json(),
			date : this.date === null ? null : this.date.json(),
		};
	};

	Edition.prototype.extendedEdition = function(callback) {
		var html = "";
		var self = this;
		if (!(this.selectedEdition === option.all.id || this.selectedEdition === options.edition.none)) {
			$.ajax({
				url : "/search/getEdition?edition=" + this.selectedEdition
			}).done(function(edition) {
				var html = "";
				if (edition.heteronyms !== null) {
					// set heteronym
					self.heteronym = new Heteronym(self.id);
					html += self.heteronym.html(edition.heteronyms);
				}

				if (edition.beginDate !== null && edition.endDate !== null) {
					self.date = new MyDate(self.id);
					self.date.beginDate = edition.beginDate;
					self.date.endDate = edition.endDate;
					html += self.date.html(edition.beginDate, edition.endDate);
				} else {
					date = null;
				}

				callback(html);
			});
		} else {
			callback(html);
		}
	};

	Edition.prototype.render = function(callback) {
		var self = this;
		this.toHTML(function(html) {
			// rendering function
			callback(FormGroup(self.id, html));
		});
	};

	Edition.prototype.toHTML = function(callback) {
		var self = this;
		$.ajax({
			url : "/search/getEditions"
		}).done(
				function(editions) {

					if (this.selectedEdition === null) {
						self.selectedEdition = "all";
					}
					var html = "";
					html += "<div id=\"select-inclusion\" class=\"col-xs-4 col-md-3\">"
							+ "<select class=\"form-control\">" + "<option id = \"in\">"
							+ '<spring:message javaScriptEscape="true" code="search.inclusion.includedIn" />'
							+ "</option>" + "<option id = \"out\">"
							+ '<spring:message javaScriptEscape="true" code="search.inclusion.excludedFrom" />'
							+ "</option>" + "</select>" + "</div>";

					html += "<div class=\"col-xs-5 col-md-3\">" + "<select id=select-edition class=\"form-control\">";

					html += "<option id="+option.all.id+">" + option.all.text + "</option>";
					for ( var i in editions) {
						html += "<option id=" + i + ">" + editions[i] + "</option>";
					}

					html += "</select></div>";

					html += "<div id=extra-options>";
					html += "</div>";

					callback(html);
				});
	};

	Edition.prototype.changeEdition = function(acronym, callback) {
		this.selectedEdition = acronym;
		this.heteronym = this.beginDate = this.endDate = null;
		callback(this);
	};

	Edition.prototype.changeInclusion = function(option) {
		this.inclusionOption = option;
	};

	Edition.prototype.changeHeteronym = function(heteronym) {
		this.heteronym.changeHeteronym(heteronym);
	};

	Edition.prototype.changeDateOption = function(dateOption) {
		this.date.option = dateOption;
	};

	Edition.prototype.setBeginDate = function(beginDate) {
		this.date.beginDate = beginDate;
	};

	Edition.prototype.setEndDate = function(endDate) {
		this.date.endDate = endDate;
	};

	Edition.prototype.getDateOption = function() {
		return this.date.getDateOption();
	};

	//Manuscript definition
	function Manuscript(id) {
		this.url = "/search/getManuscriptsDates";
		this.type = "manuscript";
		this.id = id;
		this.hasLdoDMark = "all";
		this.date = null;
	};

	Manuscript.prototype.render = Edition.prototype.render;

	Manuscript.prototype.toHTML = function(callback) {

		var self = this;

		$.ajax({
			url : self.url
		}).done(
				function(result) {
					var html = "";

					html += "<div class=\"col-xs-4 col-md-3\">";
					html += "<label>" + '<spring:message javaScriptEscape="true" code="general.LdoDLabel" />'
							+ "</label>";
					html += "<select id=\"select-ldod-mark\" class=\"form-control\">" + "<option id="+option.all.id+">"
							+ option.all.text + "</option>" + "<option id=\"true\">"
							+ '<spring:message javaScriptEscape="true" code="search.ldod.with" />' + "</option>"
							+ "<option id=\"false\">"
							+ '<spring:message javaScriptEscape="true" code="search.ldod.without" />' + "</option>"
							+ "</select>";
					html += "</div>";

					//html += "<div>";
					//html+="<select id=\"select-medium\">";

					//var len = result.mediums.length;			
					//for(var i = 0 ;  i < len ; i++){
					//	html+="<option id=\""+result.mediums[i]+"\">"+result.mediums[i]+"</option>";
					//}
					//html+="</select>";
					//html += "</div>";
					html += "<div col-xs-4 col-md-3>";
					self.date = new MyDate(this.id);
					self.date.beginDate = result.dates.beginDate;
					self.date.endDate = result.dates.endDate;
					html += self.date.html(result.dates.beginDate, result.dates.endDate);
					html += "</div>";

					callback(html);
				});
	};

	Manuscript.prototype.json = function() {
		return {
			type : this.type,
			hasLdoDMark : this.hasLdoDMark,
			date : this.date.json()
		};
	};

	Manuscript.prototype.setHasLdoDMark = function(LdoD) {
		this.hasLdoDMark = LdoD;
	}

	Manuscript.prototype.changeDateOption = function(dateOption) {
		this.date.changeDateOption(dateOption);
	};

	Manuscript.prototype.setBeginDate = function(beginDate) {
		this.date.setBeginDate(beginDate);
	};

	Manuscript.prototype.setEndDate = function(endDate) {
		this.date.setEndDate(endDate)
	};

	Manuscript.prototype.getDateOption = function(endDate) {
		return this.date.getDateOption();
	}

	Manuscript.prototype.changeLdoDMark = function(hasLdoDMark) {
		this.hasLdoDMark = hasLdoDMark;
	}

	/* Dactiloscript */
	function Dactiloscript(id) {
		this.url = "/search/getDactiloscriptsDates";
		this.type = "dactiloscript";
		this.id = id;
		this.hasLdoDMark = "all";
		this.date = null;
	};

	//Inherit Manuscript's proto
	Dactiloscript.prototype = Object.create(Manuscript.prototype);

	/* Publication */
	function Publication(id) {
		this.url = "/search/getPublicationsDates";
		this.type = "publication";
		this.id = id;
		this.date = null;
	};

	Publication.prototype.render = Edition.prototype.render;

	Publication.prototype.json = function(callback) {
		return {
			type : this.type,
			date : this.date.json()
		};
	};

	Publication.prototype.toHTML = function(callback) {
		var self = this;
		$.ajax({
			url : self.url
		}).done(function(published) {
			var html = "<div col-xs-4 col-md-3>";
			self.date = new MyDate(this.id);
			self.date.beginDate = published.beginDate;
			self.date.endDate = published.endDate;
			html += self.date.html(published.beginDate, published.endDate);
			html += "</div>";

			callback(html);
		});
	};

	Publication.prototype.changeDateOption = function(dateOption) {
		this.date.changeDateOption(dateOption);
	};

	Publication.prototype.setBeginDate = function(beginDate) {
		this.date.setBeginDate(beginDate);
	};

	Publication.prototype.setEndDate = function(endDate) {
		this.date.setEndDate(endDate)
	};

	Publication.prototype.getDateOption = function(endDate) {
		return this.date.getDateOption();
	}

	/*
	 * Heteronym Definition
	 * 
	 */
	function Heteronym(id) {
		this.id = id;
		this.heteronym = option.all.id;
	};

	Heteronym.prototype.render = Edition.prototype.render;

	Heteronym.prototype.html = function(heteronyms) {

		var html = "<div id=\"heteronym\" class=\"col-xs-5 col-md-3\">" + "<label>"
				+ '<spring:message javaScriptEscape="true" code="general.heteronym" />' + "</label></br>"
				+ "<select id=\"select-heteronym\" class=\"form-control\">";

		html += "<option id="+option.all.id+">" + option.all.text + "</option>";

		for ( var i in heteronyms) {
			if (!heteronyms[i]) {
				html += "<option id="+heteronyms[i]+">"
						+ '<spring:message javaScriptEscape="true" code="search.option.noAttribution"/>' + "</option>";
			} else {
				html += "<option id="+heteronyms[i]+">" + i + "</option>";
			}
		}

		html += "</select></div>";

		return html;

	};

	Heteronym.prototype.toHTML = function(callback) {
		var self = this;
		$.ajax({
			url : "/search/getHeteronyms"
		}).done(function(heteronyms) {
			var html = self.html(heteronyms);
			callback(html);

		});
	};

	Heteronym.prototype.changeHeteronym = function(heteronym) {
		this.heteronym = heteronym;
	};

	Heteronym.prototype.json = function() {
		return {
			type : "heteronym",
			heteronym : this.heteronym,
		};
	};

	/*
	 * Date Definition
	 * Date is a reserved keyword
	 */
	function MyDate(id) {
		this.id = id;
		this.option = option.all.id;
		this.beginDate = null;
		this.endDate = null;
	}

	MyDate.prototype.json = function() {
		return {
			type : "date",
			option : this.option,
			begin : this.beginDate,
			end : this.endDate
		};
	};

	MyDate.prototype.render = Edition.prototype.render;

	MyDate.prototype.html = function(beginDate, endDate) {
		this.beginDate = beginDate;
		this.endDate = endDate;

		var html = "<div id=period class=\"col-xs-5 col-md-3\">" + "<label>"
				+ '<spring:message javaScriptEscape="true" code="general.date" />' + "</label></br>";

		html += "<select id=\"date-option\" class=\"form-control\">" + "<option id=\""+option.all.id+"\">"
				+ option.all.text + "</option>" + "<option id=\"dated\">"
				+ '<spring:message javaScriptEscape="true" code="search.date.dated" />' + "</option>"
				+ "<option id=\"undated\">" + '<spring:message javaScriptEscape="true" code="search.date.unDated" />'
				+ "</option>" + "</select>" + "</br>";
		html += "<div id = \"date-values\" style=\"display: none;\">";
		html += '<label><spring:message javaScriptEscape="true" code="search.date.begin" /></label>' + '</br>'
				+ "<input id=\"date-value-begin\" class=\"form-control\" type=text value=" 
		+ beginDate + " /></br>";
		html += '<label><spring:message javaScriptEscape="true" code="search.date.end" /></label>' + '</br>'
				+ "<input id=\"date-value-end\" class=\"form-control\" type=text value="
		+ endDate + " />";
		html += "</div></div>";

		return html;
	};

	MyDate.prototype.toHTML = function(callback) {
		var self = this;
		$.ajax({
			url : "/search/getDates"
		}).done(function(dates) {

			this.beginDate = dates.beginDate;
			this.endDate = dates.endDate;
			var html = self.html(this.beginDate, this.endDate);

			callback(html);
		});
	};

	MyDate.prototype.changeDateOption = function(dateOption) {
		this.option = dateOption;
	};

	MyDate.prototype.setBeginDate = function(beginDate) {
		this.beginDate = beginDate;
	};

	MyDate.prototype.setEndDate = function(endDate) {
		this.endDate = endDate;
	};

	MyDate.prototype.getDateOption = function() {
		return this.option;
	};

	/* Textual */
	function MyText(id) {
		this.id = id;
		this.text = "null";
	};

	MyText.prototype.json = function(callback) {
		return {
			type : "text",
			text : this.text
		};
	};

	MyText.prototype.render = Edition.prototype.render;

	MyText.prototype.toHTML = function(callback) {

		var html = "<div class=\"col-xs-4 col-md-8\">" + "<label>"
				+ '<spring:message javaScriptEscape="true" code="search.keyword"/>' + "</label>"
				+ "<input id=\"text\" class=\"form-control\"></input>" + "</div>";

		callback(html);
	};

	MyText.prototype.changeText = function(text) {
		this.text = text;
	};

	/* Publication */
	function MyTaxonomy(id) {
		this.id = id;
		this.text = "null";
	};

	MyTaxonomy.prototype.json = function(callback) {
		return {
			type : "taxonomy",
			tags : this.text
		};
	};

	MyTaxonomy.prototype.render = Edition.prototype.render;

	MyTaxonomy.prototype.toHTML = function(callback) {
		var html = "<div class=\"col-xs-4 col-md-8\">" + "<label></br>"
				+ '<spring:message javaScriptEscape="true" code="general.taxonomies" />' + "</lable>"
				+ "<input id=\"text\" class=\"form-control\"></input>" + "</div>";
		callback(html);
	};

	MyTaxonomy.prototype.changeText = function(text) {
		this.text = text;
	};

	//VirtualEdition definition
	function VirtualEdition(id) {

		this.id = id;
		this.inclusionOption = "in";
		this.selectedEdition = option.all.id;
		//this.heteronym = null;
		//this.date = null;
	};

	VirtualEdition.prototype.json = function() {
		return {
			type : "virtualedition",
			inclusion : this.inclusionOption,
			edition : this.selectedEdition,
		};
	};

	VirtualEdition.prototype.render = Edition.prototype.render;

	VirtualEdition.prototype.toHTML = function(callback) {
		var self = this;
		$.ajax({
			url : "/search/getVirtualEditions"
		}).done(
				function(virtualEditions) {
					if (this.selectedEdition === null) {
						self.selectedEdition = "all";
					}

					var html = "";

					//Inclusion
					html += "<div id=\"select-inclusion\" class=\"col-xs-4 col-md-3\">"
							+ "<select class=\"form-control\">" + "<option id = \"in\">"
							+ '<spring:message javaScriptEscape="true" code="search.inclusion.includedIn" />'
							+ "</option>" + "<option id = \"out\">"
							+ '<spring:message javaScriptEscape="true" code="search.inclusion.excludedFrom" />'
							+ "</option>" + "</select>" + "</div>";

					//User virtual editions
					html += "<div class=\"col-xs-5 col-md-4\">" + "<select id=select-edition class=\"form-control\">";

					html += "<option id="+option.all.id+">" + option.all.text + "</option>";
					for ( var i in virtualEditions) {
						html += "<option id=" + i + ">" + virtualEditions[i] + "</option>";
					}

					html += "</select></div>";

					callback(html);
				});
	};

	VirtualEdition.prototype.changeEdition = function(acronym, callback) {
		this.selectedEdition = acronym;
	};

	VirtualEdition.prototype.changeInclusion = function(option) {
		this.inclusionOption = option;
	};

	function FormGroup(id, element) {
		var html = "";
		html += "<div id=" + id + " class=\"row form-group\">"
				+ "<hr><div id=selection-box class=\"col-xs-2 col-md-2\">"
				+ "<select id=selection-box class=\"form-control\">";
		for ( var i in Domain) {
			html += "<option id=" + i + ">" + Domain[i] + "</option>";
		}
		html += "</select>" + "</div>";
		html += "<div class=\"extended col-xs-8 col-md-8\">" + element + "</div>";
		html += "<div class=\"col-xs-2 col-md-2\" align =right>"
				+ "<button id=\"minusBtn\" type=\"button\" class=\"btn btn-default tip\""+ 
				" title=\"<spring:message code='search.removecriteria' />\">"
				+ "<span class=\"glyphicon glyphicon-minus\"></span>" + "</button></div></div>";
		return html;

	}

	//Specification of available options.
	Domain = {
		Edition : '<spring:message javaScriptEscape="true" code="navigation.edition" />',
		Manuscript : '<spring:message javaScriptEscape="true" code="general.manuscript" />',
		Dactiloscript : '<spring:message javaScriptEscape="true" code="general.typescript" />',
		Publication : '<spring:message javaScriptEscape="true" code="general.published" />',
		Heteronym : '<spring:message javaScriptEscape="true" code="general.heteronym" />',
		MyTaxonomy : '<spring:message javaScriptEscape="true" code="general.taxonomies" />',
		VirtualEdition : '<spring:message javaScriptEscape="true" code="virtual.editions" />',
		MyDate : '<spring:message javaScriptEscape="true" code="general.date" />',
		MyText : '<spring:message javaScriptEscape="true" code="search.text" />'
	}

	//FormModel
	function Form() {
		this.items = [];
		this.counter = 1;
		this.mode = "and";

		// Riot.js initialization
		riot.observable(this);
	};

	Form.prototype.json = function() {
		var options = [];

		for (var i = 0; i < this.items.length; i++) {
			options.push(this.items[i].json());
		}

		json = {
			//	type 	: "search",
			mode : this.mode,
			options : options
		};

		return json;
	}

	Form.prototype.add = function() {
		var edition = new Edition(this.counter++);
		this.items.push(edition);
		this.trigger("add", edition);
	};

	Form.prototype.remove = function(key) {
		for (var i = 0; i < this.items.length; i++) {
			if (this.items[i].id == key) {
				this.items.splice(i, 1);
				break;
			}
		}
		this.trigger("remove", key);
	};

	Form.prototype.swap = function(key, newOption) {
		var item = null;
		if (Domain[newOption]) {
			item = new window[newOption](key);
			for ( var i in this.items) {
				if (this.items[i].id == key) {
					this.items[i] = item;
				}
			}
			this.trigger("swap", item);
		} else {
			console.log(newOption + " Isn't known");
		}
	};

	Form.prototype.changeMode = function(option) {
		this.mode = option;
	}

	Form.prototype.changeEdition = function(key, newOption) {
		var self = this;
		for (var i = 0; i < this.items.length; i++) {
			if (this.items[i].id == key) {
				this.items[i].changeEdition(newOption, function(it) {
					self.trigger("change-editor", it);
				});
				break;
			}
		}
	};

	Form.prototype.changeEditionDateOption = function(key, newOption) {
		for (var i = 0; i < this.items.length; i++) {
			if (this.items[i].id == id) {
				this.items[i].changeDateOption(newOption);
				this.trigger("edition-");
			}
		}
	};

	Form.prototype.extendEditorOption = function(key, newOption) {
		var self = this;
		for (var i = 0; i < this.items.length; i++) {
			if (this.items[i].key == key) {
				this.items[i].extendEditorOption(newOption, function(it) {
					self.trigger("render", it);
				});
			}
		}
	};

	Form.prototype.changeInclusion = function(id, option) {
		for (var i = 0; i < this.items.length; i++) {
			if (this.items[i].id == id) {
				this.items[i].changeInclusion(option);
			}
		}
	};

	Form.prototype.changeHeteronym = function(id, optionValue) {
		for (var i = 0; i < this.items.length; i++) {
			if (this.items[i].id == id) {
				this.items[i].changeHeteronym(optionValue);
			}
		}
	};

	Form.prototype.changeDateOption = function(id, optionValue) {
		for (var i = 0; i < this.items.length; i++) {
			if (this.items[i].id == id) {
				this.items[i].changeDateOption(optionValue);
				this.trigger("change-date-option", this.items[i]);
			}
		}
	};

	Form.prototype.changeDateBeginDateOption = function(id, value) {
		for (var i = 0; i < this.items.length; i++) {
			if (this.items[i].id == id) {
				this.items[i].setBeginDate(value);
			}
		}
	}

	Form.prototype.changeDateEndDateOption = function(id, value) {
		for (var i = 0; i < this.items.length; i++) {
			if (this.items[i].id == id) {
				this.items[i].setEndDate(value);
			}
		}
	}

	Form.prototype.changeLdoDMark = function(id, value) {
		for (var i = 0; i < this.items.length; i++) {
			if (this.items[i].id == id) {
				this.items[i].changeLdoDMark(value);
			}
		}
	}

	Form.prototype.changePublication = function(id, value) {
		for (var i = 0; i < this.items.length; i++) {
			if (this.items[i].id == id) {
				this.items[i].changePublication(value);
			}
		}
	}

	Form.prototype.changeText = function(id, value) {
		for (var i = 0; i < this.items.length; i++) {
			if (this.items[i].id == id) {
				this.items[i].changeText(value);
			}
		}
	}
</script>

</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>
	<div class='container'>
		<h3 class="text-center">
			<spring:message code="header.search.advanced" />
		</h3>
		<div id="main" class="row ">
			<br> <br>
			<div class="col-xs-5 col-md-5 center-block">
				<select id="select-mode" class="form-control" data-width="75%">
					<option id="and"><spring:message javaScriptEscape="true"
							code="search.rule.matchAll" /></option>
					<option id="or"><spring:message javaScriptEscape="true"
							code="search.rule.matchOne" /></option>
				</select>
			</div>
		</div>
		<div id="options"></div>
		<hr>
		<div class="row ">
			<div class="col-xs-6 col-md-6">
				<button type="submit" class="btn btn-default" id='submit'>
					<span class="glyphicon glyphicon-search"></span>
					<spring:message code="search" />
				</button>
			</div>

			<div class="col-xs-6 col-md-6" align="right">
				<button id="plusBtn" type="button" class="btn btn-default tip"
					title='<spring:message code="search.addcriteria" />'>
					<span class="glyphicon glyphicon-plus"></span>
				</button>
			</div>

		</div>
		<br> <br>
		<div id="results"></div>

	</div>

	<script type="text/javascript">
		//Presenter		

		function clean() {
			$("#results").empty()
		}

		$(document)
				.ready(
						function() {
							var model = new Form();
							var anchor = $('#options');

							//Listen to view events
							//add a new options
							$('body').on('click', '#plusBtn', function(e) {
								model.add();
								e.stopPropagation()
								clean();

							});

							//remove an option
							$('body').on('click', '#minusBtn', function(e) {
								var id = $(this).parents(".form-group").attr("id");
								model.remove(id);

								e.stopPropagation()
								clean();
							});

							//swap an option
							$('body').on('change', '#selection-box', function(e) {
								var id = $(this).parents(".form-group").attr("id");
								var newOption = $(this).find(':selected')[0].id;
								model.swap(id, newOption);
								e.stopPropagation()
								clean();
							});

							//Listen to when an edition option is selected
							$('body').on('change', '#select-edition', function(e) {
								var id = $(this).parents(".form-group").attr("id");
								var newOption = $(this).find(':selected')[0].id;
								model.changeEdition(id, newOption);
								e.stopPropagation();
								clean();
							});

							//Listen to when a different edition's heteronym is selected
							$('body').on('change', '#select-heteronym', function(e) {
								var id = $(this).parents(".form-group").attr("id");
								var newOption = $(this).find(':selected')[0].id;
								model.changeHeteronym(id, newOption);
								e.stopPropagation();
								clean();
							});

							$('body').on('change', '#select-inclusion', function(e) {
								var id = $(this).parents(".form-group").attr("id");
								var newOption = $(this).find(':selected')[0].id;
								model.changeInclusion(id, newOption);
								e.stopPropagation();
								clean();
							});

							//MyDate
							//Listen to when a date option is selected
							$('body').on('change', '#date-option', function(e) {
								var id = $(this).parents(".form-group").attr("id");
								var newOption = $(this).find(':selected')[0].id;
								model.changeDateOption(id, newOption);
								e.stopPropagation();
								clean();
							});

							//Listen to when a date begin value is selected
							$('body').on('change', '#date-value-begin', function(e) {
								var id = $(this).parents(".form-group").attr("id");
								var value = $(this).val();
								model.changeDateBeginDateOption(id, value);
								e.stopPropagation()
								clean();
							});

							//Listen to when a date end value is selected
							$('body').on('change', '#date-value-end', function(e) {
								var id = $(this).parents(".form-group").attr("id");
								var value = $(this).val();
								model.changeDateEndDateOption(id, value);
								e.stopPropagation()
								clean();

							});

							//Form mode,
							$('body').on('change', '#select-mode', function(e) {
								model.changeMode($(this).find(':selected')[0].id);
								e.stopPropagation()
								clean();
							});

							$('body').on('change', '#select-ldod-mark', function(e) {
								var id = $(this).parents(".form-group").attr("id");
								var option = $(this).find(':selected')[0].id;
								model.changeLdoDMark(id, option);
								e.stopPropagation()
								clean();
							});

							$('body').on('change', '#text', function(e) {
								var id = $(this).parents(".form-group").attr("id");
								var value = $(this).val();
								model.changeText(id, value);
								e.stopPropagation()
								clean();
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
								item.toHTML(function(html) {
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
								if (item.getDateOption() === 'dated') {
									$('#' + item.id).find("#date-values").show();
								} else {
									$('#' + item.id).find("#date-values").hide();
								}
							});

							//Handle 
							$('#submita')
									.click(
											function() {
												$(this).blur();
												var json = JSON.stringify(model.json());

												$
														.ajax({
															type : "POST",
															url : "/search/advanced/result",
															data : json,
															contentType : 'application/json',
														})
														.done(
																function(fragments) {
																	if (fragments.length > 0) {
																		var html = "";
																		var interCount = 0;
																		for ( var i in fragments) {
																			var length = fragments[i].inters.length;
																			interCount += length;
																			html += "<tr>"
																					+ "<td rowspan=\""+length+"\">"
																					+ "<a href=\""+fragments[i].url+"\">"
																					+ fragments[i].title + "</a>"
																					+ "</td>";

																			html += "<td><p><a href=\""+fragments[i].inters[0].url+"\">"
																					+ fragments[i].inters[0].title
																					+ "</a> ("
																					+ fragments[i].inters[0].type
																					+ ")</td></tr>";

																			for (var j = 1; j < length; j++) {
																				html += "<tr><td><a href=\""+fragments[i].inters[j].url+"\">"
																						+ fragments[i].inters[j].title
																						+ "</a> ("
																						+ fragments[i].inters[j].type
																						+ ")</td></tr>";
																			}
																		}

																		html = "<table class=\"table table-hover table-condensed\">"
																				+ "<thead>"
																				+ "<tr>"
																				+ "<th>"
																				+ '<spring:message javaScriptEscape="true" code="fragments"/> ('
																				+ fragments.length
																				+ ")</th>"
																				+ "<th>"
																				+ '<spring:message javaScriptEscape="true" code="interpretations"/> ('
																				+ interCount
																				+ ")</th>"
																				+ "</tr>"
																				+ "<tbody>" + html

																		$("#results").empty().append(html);
																	} else {
																		$("#results")
																				.empty()
																				.append(
																						'<p><spring:message javaScriptEscape="true" code="search.noResults"/><p>');
																	}

																});
											});

							$('#submit').click(function() {
								$(this).blur();//lose selection
								var data = model.json();
								console.log(data);
								var json = JSON.stringify(model.json());

								$('#results').empty().append("<hr><div class=\"spinner-loader\">Loadind...</div>");
								$('#results').css("display", "block");

								$.ajax({
									type : "POST",
									url : "/search/advanced/result",
									data : json,
									contentType : 'application/json',

									success : function(html) {
										console.log(html)

										$('#results').empty().append(html);

										$('#tablesearchresults').attr("data-pagination", "true");
										$('#tablesearchresults').attr("data-search", "true");
										$('#tablesearchresults').bootstrapTable();

										/*
										$('.result-table').dataTable({
											'paging':	false
										});
										 */
									}
								});
							});
							//Add one element
							model.add();
						});
		$(".tip").tooltip({
			placement : 'bottom'
		});
	</script>
</body>
</html>