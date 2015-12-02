<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>

<div id="fragmentTranscription" class="row">

	<div class="row" id="content">
		<h4 class="text-center">${inters.get(0).title}</h4>
		<c:choose>
			<c:when test="${inters.get(0).lastUsed.sourceType=='EDITORIAL'}">
				<div class="well" style="font-family: georgia; font-size: medium;">
					<p>${writer.getTranscription()}</p>
				</div>
			</c:when>
			<c:otherwise>
				<div class="well" style="font-family: courier;">
					<p>${writer.getTranscription()}</p>
				</div>
			</c:otherwise>
		</c:choose>
	</div>
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
						"${contextPath}/fragments/fragment/inter/${inters.get(0).externalId}",
						function(html) {
							var newDoc = document.open("text/html", "replace");
							newDoc.write(html);
							newDoc.close();
						});
	}
</script>

