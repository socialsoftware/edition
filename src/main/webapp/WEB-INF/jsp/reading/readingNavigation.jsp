<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<c:if test="${inter != null}">
	<c:set var="fragment" value="${inter.getFragment()}" />
</c:if>

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
			<div class="text-center">
				<c:choose>
					<c:when test="${fragment == null}">
						Start Reading <a
							href="${contextPath}/reading/inter/first/edition/${expertEdition.getExternalId()}"><span
							class="glyphicon glyphicon-forward"></span></a>
					</c:when>
					<c:otherwise>
						<c:forEach var="expertEditionInter"
							items="${expertEdition.getSortedInter4Frag(fragment)}">
							<div>
								<a
									href="${contextPath}/reading/inter/prev/number/${expertEditionInter.externalId}"><span
									class="glyphicon glyphicon-backward"></span></a> <a
									href="${contextPath}/reading/inter/${expertEditionInter.externalId}">${expertEditionInter.getEdition().getAcronym()} ${expertEditionInter.number}
								</a> <a
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
			<h4>
				<a
					href="${contextPath}/edition/internalid/${archiveEdition.externalId}">
					Recommendation</a>
			</h4>
		</div>
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
