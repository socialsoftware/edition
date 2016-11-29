<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<script type="text/javascript">
	function openRecomModal() {
		$('#recommendationModal').modal()
	}

	function resetPrevRecom() {
		$.get("${contextPath}/reading/inter/prev/recom/reset");
		$('#recommendationModal').modal('hide');
		location.reload();
	}

	function changeWeight(type, value) {
		$.post('${contextPath}/reading/weight', {
			'type' : type,
			'value' : value
		}, function(result) {
			alert(result);
		});
	}

	function reload() {
		location.reload();
	}
</script>

<script type="text/javascript">
	$('#recommendationModal').on('hidden.bs.modal', function() {
		location.reload();
	})
</script>

<c:if test="${inter != null}">
	<c:set var="fragment" value="${inter.getFragment()}" />
</c:if>

<!-- HEADERS -->
<div class="row">
	<!-- EDITORIAL -->
	<c:forEach var="expertEdition" items='${ldoD.sortedExpertEdition}'>
		<div class="col-md-2">
			<div class="text-center">
				<h4>
					<a
						href="${contextPath}/edition/internalid/${expertEdition.externalId}">
						${expertEdition.editor}</a>
				</h4>
			</div>
		</div>
	</c:forEach>

	<!-- RECOMMENDATION -->
	<div class="col-md-2">
		<div class="text-center">
			<h4>
				<a onClick="openRecomModal()"><spring:message
						code="general.recommendation" /> </a>
			</h4>
		</div>
	</div>
</div>

<!-- INTERPRETATIONS MENU -->
<div class="row">
	<!-- EDITORIAL -->
	<c:forEach var="expertEdition" items='${ldoD.sortedExpertEdition}'>
		<div class="col-md-2">
			<div class="text-center">
				<c:choose>
					<c:when test="${fragment == null}">
						<a
							href="${contextPath}/reading/inter/first/edition/${expertEdition.getExternalId()}"><%-- <spring:message
								code="general.reading.start" /> --%> <span
							class="glyphicon glyphicon-forward"></span> </a>
					</c:when>
					<c:otherwise>
						<c:forEach var="expertEditionInter"
							items="${expertEdition.getSortedInter4Frag(fragment)}">
							<div>
								<a
									href="${contextPath}/reading/inter/prev/number/${expertEditionInter.externalId}"><span
									class="glyphicon glyphicon-backward"></span></a> <a
									href="${contextPath}/reading/inter/${expertEditionInter.externalId}">${expertEditionInter.getEdition().getAcronym()}
									${expertEditionInter.number} </a> <a
									href="${contextPath}/reading/inter/next/number/${expertEditionInter.externalId}"><span
									class="glyphicon glyphicon-forward"></span></a>
							</div>
						</c:forEach>
					</c:otherwise>
				</c:choose>

			</div>
		</div>
	</c:forEach>

	<!-- RECOMMENDATION -->
	<c:if test="${fragment != null}">
		<div class="col-md-2">
			<div class="text-center">
				<c:if test="${prevRecom != null}">
					<div>
						<a href="${contextPath}/reading/inter/prev/recom">${prevRecom.getEdition().getAcronym()}
							${prevRecom.number} <span class="glyphicon glyphicon-backward"></span>
						</a>
					</div>
				</c:if>
				<div>
					<a href="${contextPath}/reading/inter/${inter.externalId}"><span
						class="glyphicon glyphicon-play"></span>
						${inter.getEdition().getAcronym()} ${inter.number} </a>
				</div>
				<c:forEach var="recomInter" items="${recommendations}">
					<div>
						<a href="${contextPath}/reading/inter/${recomInter.externalId}"><span
							class="glyphicon glyphicon-forward"></span>
							${recomInter.getEdition().getAcronym()} ${recomInter.number} </a>
					</div>
				</c:forEach>
			</div>
		</div>
	</c:if>
</div>

<!-- Recommendations configuration Modal HTML -->
<div class="modal fade" id="recommendationModal" tabindex="-1"
	role="dialog">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" onclick="reload()"
					data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h3 class="modal-title text-center">
					<spring:message code="general.recommendation.config" />
				</h3>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="form-group" id="clearPrevRecomForm">
						<div class="col-md-7">
							<h4 class="text">
								<spring:message code="general.reset.list" />
								:
							</h4>
						</div>
						<div class="col-md-1">
							<button type="submit" class="btn btn-primary"
								onclick="resetPrevRecom()">
								<span class="glyphicon glyphicon-saved"></span>
								<spring:message code="general.reset" />
							</button>
						</div>
					</div>
				</div>
				<hr>
				<div class="row text-center">
					<div class="col-md-3 text-center">
						<h4>
							<spring:message code="recommendation.criteria" />
							:
						</h4>
					</div>
					<div class="col-md-2 col-sm-4">
						<spring:message code="general.heteronym" />
						<input type="range" class="range"
							onChange="changeWeight('heteronym', value)"
							value='${ldoDSession.getRecommendation().getHeteronymWeight()}'
							max="1" min="0" step="0.2">
					</div>
					<div class="col-md-2 col-sm-4">
						<spring:message code="general.date" />
						<input type="range" class="range"
							onChange="changeWeight('date', value)"
							value='${ldoDSession.getRecommendation().getDateWeight()}'
							max="1" min="0" step="0.2">
					</div>
					<div class="col-md-2 col-sm-4">
						<spring:message code="general.text" />
						<input type="range" class="range"
							onChange="changeWeight('text', value)"
							value='${ldoDSession.getRecommendation().getTextWeight()}'
							max="1" min="0" step="0.2">
					</div>
					<div class="col-md-2 col-sm-4">
						<spring:message code="general.taxonomy" />
						<input type="range" class="range"
							onChange="changeWeight('taxonomy', value)"
							value="${ldoDSession.getRecommendation().getTaxonomyWeight()}"
							max="1.0" min="0.0" step="0.2">
					</div>
				</div>
				<br>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" onclick="reload()"
						data-dismiss="modal">
						<spring:message code="general.close" />
					</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
	</body>
</div>