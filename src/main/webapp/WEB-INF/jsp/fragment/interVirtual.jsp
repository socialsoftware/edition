<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>

<div id=fragmentInter class="row">
	<h3>${inters.get(0).edition.title}</h3>
	<h4>
		<spring:message code="general.uses" />
		${inters.get(0).uses.edition.getReference()}(${inters.get(0).uses.reference})
	</h4>

	<div class="row" id="content">
		<%@ include file="/WEB-INF/jsp/fragment/transcription.jsp"%>
	</div>

	<%@ include file="/WEB-INF/jsp/fragment/taxonomy.jsp"%>
</div>


<!-- Annotator 2.0 -->
<script src="/resources/js/annotator.min.js"></script>
<link
	href="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1/css/select2.min.css"
	rel="stylesheet" />
<script
	src="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1/js/select2.min.js"></script>


<script type="text/javascript">
	function stringifyTags(array) {
		return array.join(" ");
	}

	function parseTags(string) {
		string = $.trim(string);
		var tags = [];

		if (string) {
			tags = string.split(/\s+/);
		}

		return tags;
	}

	function editorExtension(e) {
		// The input element added to the Annotator.Editor wrapped in jQuery.
		// Cached to save having to recreate it everytime the editor is displayed.
		var field = null;
		var tagsField = null;
		var input = null;

		function updateField(field, annotation) {
			var value = '';
			if (annotation.tags) {
				value = stringifyTags(annotation.tags);
			}
			input.val(value);
		}

		function setAnnotationTags(field, annotation) {
			annotation.tags = parseTags(input.val());
		}

		field = e.addField({
		//	label : _t('Add some tags here') + '\u2026',
			load : updateField,
			submit : setAnnotationTags
		});

		input = $(field).find(':input');
		
		var input2;
		
		var params = "?";
		var andParam = "";

		function setAnnotationTags2(field, annotation) {
			$(".select2-selection_choice").each(function(){
			    params += andParam + "selected=" + $(this).val();
			    andParam = "&";
			});
			alert(params);
			annotation.tags = parseTags(input2.val());
		}

		tagsField = e.addField({
			load : updateField,
			//submit : setAnnotationTags2
		})

		var data = $.parseJSON('${inters.get(0).getCategoriesJSON()}');
					

		$(tagsField).select2({
		//	placeholder : _t('Add some tags here') + '\u2026',
			multiple : true,
			data : data,
			style : "width:100%",
			 placeholder: "Select a state",
		});
		
		//input2 =  $(tagsField).find(':input');
	};
</script>


<script type="text/javascript">
	var pageUri = function() {
		return {
			beforeAnnotationCreated : function(ann) {
				ann.uri = '${inters.get(0).externalId}';
			}
		};
	};

	var app = new annotator.App();
	app.include(annotator.ui.main, {
		element : document.querySelector('#content'),
		editorExtensions : [ annotator.ui.tags.editorExtension ],
		viewerExtensions : [ annotator.ui.tags.viewerExtension ]
	});

	app.include(annotator.storage.http, {
		prefix : '${contextPath}/fragments/fragment'
	}).include(pageUri);

	app.include(annotator.identity.simple);

	app.include(annotator.authz.acl);

	app.include(createAnnotation);
	app.include(updateAnnotation);
	app.include(deleteAnnotation);

	app.start().then(function() {
		app.annotations.load({
			uri : '${inters.get(0).externalId}',
			limit : 0,
			all_fields : 1
		});
	}).then(function() {
		app.ident.identity = '${user.username}';
	})

	function createAnnotation() {
		return {
			annotationCreated : reloadPage
		};
	}

	function updateAnnotation() {
		return {
			annotationUpdated : reloadPage
		};
	}

	function deleteAnnotation() {
		return {
			annotationDeleted : reloadPage
		};
	}

	function reloadPage() {
		$
				.get(
						"${contextPath}/fragments/fragment/inter/${inters.get(0).externalId}/taxonomy",
						function(html) {
							$("#taxonomy").replaceWith(html);
						});
	}
</script>

