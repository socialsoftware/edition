<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>

<div id=fragmentInter class="row" style="margin-left:0px;margin-right:0px">
	<h4>${inters.get(0).edition.title} - <spring:message code="general.uses" /> ${inters.get(0).uses.edition.getReference()}(${inters.get(0).uses.reference})</h3>
	<!--  <h4>
		<spring:message code="general.uses" />
		${inters.get(0).uses.edition.getReference()}(${inters.get(0).uses.reference})
	</h4>-->
	
	<div class="row" id="content">
		<%@ include file="/WEB-INF/jsp/fragment/transcription.jsp"%>
	</div>

		<%@ include file="/WEB-INF/jsp/fragment/listTaxonomies.jsp"%>
	</div>


<!-- Annotator 2.0 -->
<script src="/resources/js/annotator.min.js"></script>

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
						"${contextPath}/fragments/fragment/inter/${inters.get(0).externalId}/taxonomies",
						function(html) {
							$("#taxonomies").replaceWith(html);
						});
	}
</script>
