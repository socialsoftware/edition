<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>

<div id=fragmentInter class="row"
	style="margin-left: 0px; margin-right: 0px">
	<h4>${inters.get(0).edition.title}
		-
		<spring:message code="general.uses" />
		${inters.get(0).uses.edition.getReference()}(${inters.get(0).uses.reference})
	</h4>

	<div class="row" id="content">
		<%@ include file="/WEB-INF/jsp/fragment/transcription.jsp"%>
	</div>

	<%@ include file="/WEB-INF/jsp/fragment/taxonomy.jsp"%>
</div>


<!-- Annotator 2.0 -->
<link rel="gettext" type="application/x-po" href="/resources/locale/pt/annotator.po">
<!-- <script src="/resources/js/annotator.min.js"></script> -->
<script src="/resources/js/annotator-ldod.js"></script>
<link
	href="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1/css/select2.min.css"
	rel="stylesheet" />
<script
	src="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1/js/select2.min.js"></script>


<script>
	function stringifyTags(array) {
		return array.join(" ");
	};

	function parseTags(string) {
		string = $.trim(string);
		var tags = [];
		if (string) {
			tags = string.split(/\,/);
		}
		return tags;
	};

	function editorExtension(e) {
		var tagsField = null;
		var data = null;
		function setAnnotationTags(field, annotation) {
			annotation.tags = parseTags($(".tagSelector").val());
		}
		tagsField = e.addField({
			load : loadField,
			submit : setAnnotationTags
		});
		$("#annotator-field-1").remove("input");
		function loadField(field, annotation) {
			if (typeof annotation.id !== 'undefined') {
				$.get("${contextPath}/fragments/fragment/annotation/" + annotation.id + "/categories", function(data,
						status) {
					$(".tagSelector").val(data).trigger("change");
				});
			} else {
				$(".tagSelector").val([]).trigger("change");
			}
		}
		var select = $("<select>");
		select.attr("class", "tagSelector");
		select.attr("style", "width:263px;");
		$(tagsField).append(select);
		if ('${inters.get(0).getVirtualEdition().getTaxonomy().getOpenVocabulary()}' == 'true') {
			$(".tagSelector").select2({
				multiple : true,
				data : $.parseJSON('${inters.get(0).getAllDepthCategoriesJSON()}'),
				tags : true,
				tokenSeparators : [ ',', '.' ]
			});
		} else {
			$(".tagSelector").select2({
				multiple : true,
				data : $.parseJSON('${inters.get(0).getAllDepthCategoriesJSON()}'),
			});
		}
		$(".tagSelector").on('select2:open', function(e, data) {
			$(".select2-dropdown").css({
				"z-index" : "999999"
			});
		});
	};
</script>

<script>
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
		editorExtensions : [ editorExtension ],
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
		location.reload(true);
		/* $.get("${contextPath}/fragments/fragment/inter/${inters.get(0).externalId}/taxonomy", function(html) {
			$("#taxonomy").replaceWith(html);
		}); */
	}
</script>