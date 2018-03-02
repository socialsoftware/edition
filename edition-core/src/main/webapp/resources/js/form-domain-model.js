

/* for default options or options that expands the ones present in the
 domain model
 */
option = {
		all : {
			id : "all",
			text : "All"
		},
		none :{
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
		type 		: "edition",
	//	id 			: this.id,
		inclusion	: this.inclusionOption,
		edition 	: this.selectedEdition,
		heteronym 	: this.heteronym == null ? null : this.heteronym.json(),
		date		: this.date === null ? null : this.date.json(),
	};
};

Edition.prototype.extendedEdition = function(callback) {
	var html = "";
	var self = this;
	if (!(this.selectedEdition === option.all.id || this.selectedEdition === options.edition.none)) {
		$.ajax({ url : "/search/getEdition?edition=" + this.selectedEdition})
		.done(function(edition) {
			var html = "";
			if (edition.heteronyms !== null) {
				// set heteronym
				self.heteronym = new Heteronym(self.id);
				html += self.heteronym.html(edition.heteronyms);
			}

			if (edition.beginDate !== null
					&& edition.endDate !== null) {
				self.date = new MyDate(self.id);
				self.date.option = "dated";
				self.date.beginDate = edition.beginDate;
				self.date.endDate = edition.endDate;
				html += self.date.html(edition.beginDate,edition.endDate);
			}else{
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
				html += "<div id=\"select-inclusion\" class=\"col-xs-4 col-md-2\">" + "<select>"
				+ "<option id = \"in\">included in</option>"
				+ "<option id = \"out\">excluded from</option>" + "</select>"
				+ "</div>";

				html += "<div class=\"col-xs-5 col-md-3\">"
					+ "<select id=select-edition>";

				html += "<option id=all>all</option>";
				for ( var i in editions) {
					html += "<option id=" + i + ">"
					+ editions[i] + "</option>";
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

Edition.prototype.changeDateOption = function(dateOption){
	this.date.option = dateOption;
};

Edition.prototype.setBeginDate = function(beginDate){
	this.date.beginDate = beginDate;
};

Edition.prototype.setEndDate = function(endDate){
	this.date.endDate = endDate;
};

Edition.prototype.getDateOption = function(endDate){
	return this.date.getDateOption();
};


//Manuscript definition
function Manuscript(id) {
	this.url = "/search/getManuscripts";
	this.type = "manuscript";
	this.id = id;
	this.hasLdoDMark = "all"; 
	this.date = null;
};

Manuscript.prototype.render = Edition.prototype.render; 

Manuscript.prototype.toHTML = function(callback) {
	
	var self = this;
	
	$.ajax({
			url:self.url
		}).done(function(result){
			var html = "";
			
			
			html += "<div class=\"col-xs-4 col-md-3\">";
			html += "<p>hasLdoDMakr</p>";
			html += "<select id=\"select-ldod-mark\">" +
				"<option id=\"all\">All</option>" +
				"<option id=\"true\">Com</option>" +
				"<option id=\"false\">Sem</option>" +
				"</select>";
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
			html += self.date.html(result.dates.beginDate,result.dates.endDate);
			html += "</div>";
			
			callback(html);
	});
};

Manuscript.prototype.json = function(){
	return {
		type : this.type,
		hasLdoDMark : this.hasLdoDMark,
		date : this.date.json()
	};
};

Manuscript.prototype.setHasLdoDMark = function(LdoD){
	this.hasLdoDMark = LdoD;
}

Manuscript.prototype.changeDateOption = function(dateOption){
	this.date.changeDateOption(dateOption);
};

Manuscript.prototype.setBeginDate = function(beginDate){
	this.date.setBeginDate(beginDate);
};

Manuscript.prototype.setEndDate = function(endDate){
	this.date.setEndDate(endDate)
};

Manuscript.prototype.getDateOption = function(endDate){
	return this.date.getDateOption();
}

Manuscript.prototype.changeLdoDMark = function(hasLdoDMark){
	this.hasLdoDMark = hasLdoDMark;
}

/* Dactiloscript */
function Dactiloscript(id) {
	this.url = "/search/getDactiloscripts";
	this.type = "dactiloscript";
	this.id = id;
	this.hasLdoDMark = "all"; 
	this.date = null;
};

//Inherit Manuscript's proto
Dactiloscript.prototype = Object.create(Manuscript.prototype);


/* Publication */
function Publication(id) {
	this.id = id;
	this.published;
};

Publication.prototype.json = function(callback) {
	return{
		type :	"publication",
		pubPlace : published
	};
};


Publication.prototype.render = Edition.prototype.render;

Publication.prototype.toHTML = function(callback) {
	var self = this;
	$.ajax({
		url : "/search/getPublications"
	}).done(
			function(published) {
				var html = "<div class=\"col-xs-4 col-md-2\">" +
					"<p>Publication Place</p>"
					+ "<select>"
				for ( var i in published) {
					html += "<option id =\"" + published[i] + "\">"
					+ published[i] + "</option>";
				}
				html += "<select>" + "<div>";

				callback(html);
			});
};

/*
 * Heteronym Definition
 * 
 */
function Heteronym(id){
	this.id = id;
	this.heteronym = option.all.id;
};

Heteronym.prototype.render = Edition.prototype.render;

Heteronym.prototype.html = function(heteronyms){

	var html = "<div id=\"heteronym\" class=\"col-xs-5 col-md-3\">" +
	"<p>"
	+ "Heterónimia"	
	+ "</p>"+
	"<select id=\"select-heteronym\">";

	html += "<option id="+option.all.id+">"+option.all.text+"</option>";

	for(var i in heteronyms){
		html += "<option id="+heteronyms[i]+">"+i+"</option>";
	}

	html+="</select></div>";

	return html;

};

Heteronym.prototype.toHTML = function(callback){
	var self = this;
	$.ajax({
		url : "/search/getHeteronyms"
	}).done(function(heteronyms){
		var html = self.html(heteronyms);
		callback(html);

	});
};

Heteronym.prototype.changeHeteronym = function(heteronym){
	this.heteronym = heteronym;
};

Heteronym.prototype.json = function(){
	return {
		type : "heteronym",
		heteronym : this.heteronym,
	};
};


/*
 * Date Definition
 * Date is a reserved keyword
 */
function MyDate(id){
	this.id = id;
	this.option = option.all.id;
	this.beginDate = null;
	this.endDate = null;
}

MyDate.prototype.json = function(){
	return {
		type	:"date",
		option	: this.option,
		begin	: this.beginDate,
		end	 	: this.endDate	
	};
};

MyDate.prototype.render = Edition.prototype.render;

MyDate.prototype.html = function(beginDate,endDate){	
	this.beginDate = beginDate;
	this.endDate = endDate;		

	var html = "<div id=period class=\"col-xs-5 col-md-3\">"
		+ "<p>" + "Periocidade" + "</p>";

	html += "<select id=\"date-option\">" +
	"<option id=\""+option.all.id+"\">"+option.all.text+"</option>" +
	"<option id=\"dated\">Datado</option>" +
	"<option id=\"non-dated\">Não Datado</option>" +
	"</select>" +
	"</br>";
	html += "<div id = \"date-values\" style=\"display: none;\">";
	html += "Start	<input id=\"date-value-begin\" type=text value="
		+ beginDate + " /></br>";
	html += "End	<input id=\"date-value-end\"type=text value="
		+ endDate + " />";
	html += "</div></div>";


	return html;
};

MyDate.prototype.toHTML = function(callback){
	var self = this;	
	$.ajax({
		url:"/search/getDates"
	}).done(function(dates){

		this.beginDate = dates.beginDate;
		this.endDate = dates.endDate;		
		var html = self.html(this.beginDate,this.endDate); 

		callback(html);
	});
};

MyDate.prototype.changeDateOption = function(dateOption){
	this.option = dateOption;
};

MyDate.prototype.setBeginDate = function(beginDate){
	this.beginDate = beginDate;
};

MyDate.prototype.setEndDate = function(endDate){
	this.endDate = endDate;
};

MyDate.prototype.getDateOption = function(){
	return this.option;
};


function FormGroup(id, element) {
	var html = "";
	html += "<div id=" + id + " class=\"row form-group\">"
	+ "<div id=selection-box class=\"col-xs-2 col-md-1\">"
	+ "<select id=selection-box>";
	for ( var i in Domain) {
		html += "<option id=" + i + ">"
		+ Domain[i]
		+ "</option>";
	}
	html += "</select>" + "</div>";
	html += "<div class=\"extended col-xs-8 col-md-9\">" + element + "</div>";
	html += "<div class=\"col-xs-2 col-md-2\" align =right>"
		+ "<button id=minusBtn type=button class=\"btn btn-default btn-lg\">"
		+ "<span class=\"glyphicon glyphicon-minus\"></span>"
		+ "</button> </div> </div>";
	return html;

}

//Expecification of available options.
Domain = {
		Edition :'<spring:message javaScriptEscape="true" code="search" />',
		Manuscript : Manuscript,
		Dactiloscript : Dactiloscript,
		MyDate : Date,
		Publication : Publication,
		Heteronym : Heteronym
}

alert('<spring:message javaScriptEscape="true" code="search" />');

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

	for ( var i = 0; i<this.items.length;i++) {
		options.push(this.items[i].json());
	}

	json =  {
		//	type 	: "search",
			mode 	: this.mode,
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
		item = new (newOption)(key);
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

Form.prototype.changeMode = function(option){
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

Form.prototype.changeEditionDateOption = function(key,newOption){
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

Form.prototype.changeInclusion = function(id,option){
	for (var i = 0; i < this.items.length; i++) {
		if (this.items[i].id == id) {
			this.items[i].changeInclusion(option);
		}
	}
};

Form.prototype.changeHeteronym = function(id,optionValue){
	for (var i = 0; i < this.items.length; i++) {
		if (this.items[i].id == id) {
			this.items[i].changeHeteronym(optionValue);
		}
	}
};

Form.prototype.changeDateOption = function(id,optionValue){
	for (var i = 0; i < this.items.length; i++) {
		if (this.items[i].id == id) {
			this.items[i].changeDateOption(optionValue);
			this.trigger("change-date-option", this.items[i]);
		}
	}
};

Form.prototype.changeDateBeginDateOption = function(id, value){
	for (var i = 0; i < this.items.length; i++) {
		if (this.items[i].id = id) {
			this.items[i].setBeginDate(value);
		}
	}
}

Form.prototype.changeDateEndDateOption = function(id, value){
	for (var i = 0; i < this.items.length; i++) {
		if (this.items[i].id = id) {
			this.items[i].setEndDate(value);
		}
	}
}

Form.prototype.changeLdoDMark = function(id, value){
	for (var i = 0; i < this.items.length; i++) {
		if (this.items[i].id = id) {
			this.items[i].changeLdoDMark(value);
		}
	}
}