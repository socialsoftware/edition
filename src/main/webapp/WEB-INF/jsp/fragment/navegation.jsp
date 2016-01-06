<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<script type="text/javascript">
	$(document).ready(function() {
		$('[id="baseinter"]').on('change', function() {
			var frag = $('#fragment div:first-child').attr("id");
			var data = new Array();
			$('#baseinter :checked').each(function() {
				data.push(this.value);
			});
			$.get("${contextPath}/fragments/fragment/inter", {
				fragment : frag,
				inters : data
			}, function(html) {
				var newDoc = document.open("text/html", "replace");
				newDoc.write(html);
				newDoc.close();
			});
		});
	});
</script>
<script type="text/javascript">
	$(document).ready(function() {
		$('[id="virtualinter"]').on('change', function() {
			var frag = $('#fragment div:first-child').attr("id");
			var data = new Array();
			$('#virtualinter :checked').each(function() {
				data.push(this.value);
			});
			$.get("${contextPath}/fragments/fragment/inter", {
				fragment : frag,
				inters : data
			}, function(html) {
				var newDoc = document.open("text/html", "replace");
				newDoc.write(html);
				newDoc.close();
			});
		});
	});
</script>
<script type="text/javascript">
	$(document).ready(function() {
		$('.recommender-nav').click(function() {
			$(this).parent().submit()
		});
	});
</script>
<div id="fragment" class="row">

	<!-- Fragment ID for javascript -->
	<div id="${fragment.externalId}"></div>

	<div class="btn-group" id="baseinter" data-toggle="checkbox" style="width:100%">
		<!-- AUTHORIAL -->
		<h5 class="text-center">
			<spring:message code="authorial.source" />
		</h5>
		<div class="text-center" style="padding-top:8px">
			<table width=100%>
				<thead>
					<tr>
						<th style="width:10%"></th>
						<th style="width:10%"></th>
						<th style="width:60%"></th>
						<th style="width:20%"></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="sourceInter" items='${fragment.sortedSourceInter}'>
						<tr>
							<td></td>
							<td><c:choose>
									<c:when test="${inters.contains(sourceInter)}">
										<input type="checkbox" name="${sourceInter.externalId}"
											value="${sourceInter.externalId}" checked />
									</c:when>
									<c:otherwise>
										<input type="checkbox" name="${sourceInter.externalId}"
											value="${sourceInter.externalId}" />
									</c:otherwise>
								</c:choose></td>
							<td><a 
								href="${contextPath}/fragments/fragment/inter/${sourceInter.externalId}">${sourceInter.shortName}</a></td>
							<td></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<br>
		<!-- EDITORIAL -->
		<h5 class="text-center">
			<spring:message code="edition.experts" /> <a id="infoexperts" data-placement="bottom" class="infobutton" role="button" data-toggle="popover" data-content="<spring:message code="info.experts" />" > <span class="glyphicon glyphicon-info-sign"></span></a>
		</h5>
		<c:forEach var="expertEdition" items='${ldoD.sortedExpertEdition}'>
			<c:if
				test="${expertEdition.getSortedInter4Frag(fragment).size() != 0}">
				<div class="text-center">
					<table width="100%">
						<caption class="text-center">
							<a
								href="${contextPath}/edition/internalid/${expertEdition.externalId}">
								${expertEdition.editor}</a>
						</caption>
						<thead>
							<tr>
								<th style="width: 10%"></th>
								<th style="width: 10%"></th>
								
								<th style="width: 25%"></th>
								<th style="width: 10%"></th>
								<th style="width: 25%"></th>
								
								<th style="width: 20%"></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="expertEditionInter"
								items="${expertEdition.getSortedInter4Frag(fragment)}">
								<tr>
									<td></td>
									<td><c:choose>
											<c:when test="${inters.contains(expertEditionInter)}">
												<input type="checkbox"
													name="${expertEditionInter.externalId}"
													value="${expertEditionInter.externalId}" checked />
											</c:when>
											<c:otherwise>
												<input type="checkbox"
													name="${expertEditionInter.externalId}"
													value="${expertEditionInter.externalId}" />
											</c:otherwise>
										</c:choose></td>
									
									<td><a
										href="${contextPath}/fragments/fragment/inter/prev/number/${expertEditionInter.externalId}"><span
											class="glyphicon glyphicon-chevron-left"></span></a></td>
									<td><a
										href="${contextPath}/fragments/fragment/inter/${expertEditionInter.externalId}">${expertEditionInter.number}</a></td>
									<td><a
										href="${contextPath}/fragments/fragment/inter/next/number/${expertEditionInter.externalId}"><span
											class="glyphicon glyphicon-chevron-right"></span></a></td>
									<td></td>	
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</c:if>
		</c:forEach>
	</div>
	<br><br>
	<!-- VIRTUAL -->
	<div id="virtualinter" data-toggle="checkbox">
		<c:if
			test="${(ldoDSession != null) && (ldoDSession.materializeVirtualEditions().size() != 0)}">
			<h5 class="text-center">
				<spring:message code="virtual.editions" /> <a id="infovirtualeditions" data-placement="bottom" class="infobutton" role="button" data-toggle="popover" data-content="<spring:message code="info.virtualeditions" />" > <span class="glyphicon glyphicon-info-sign"></span></a>
			</h5>
			<div class="text-center">
				<c:forEach var="virtualEdition"
					items='${ldoDSession.materializeVirtualEditions()}'>
					<div class="text-center" style="padding:8px">
						<a
							href="${contextPath}/edition/internalid/${virtualEdition.externalId}">
							${virtualEdition.acronym}</a>
					</div>
					<table width="100%">
						<thead>
							<tr>
								
								<th style="width: 10%"></th>
								<th style="width: 10%"></th>
								
								<th style="width: 25%"></th>
								<th style="width: 10%"></th>
								<th style="width: 25%"></th>
								
								<th style="width: 20%"></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="virtualEditionInter"
								items="${virtualEdition.getSortedInter4Frag(fragment)}">
								<tr>
									<td></td>
									<td><c:choose>
											<c:when test="${inters.contains(virtualEditionInter)}">
												<input type="checkbox"
													name="${virtualEditionInter.externalId}"
													value="${virtualEditionInter.externalId}" checked />
											</c:when>
											<c:otherwise>
												<input type="checkbox"
													name="${virtualEditionInter.externalId}"
													value="${virtualEditionInter.externalId}" />
											</c:otherwise>
										</c:choose></td>
									<td><a 
										href="${contextPath}/fragments/fragment/inter/prev/number/${virtualEditionInter.externalId}"><span
											class="glyphicon glyphicon-chevron-left"></span></a></td>
									<td><a
										href="${contextPath}/fragments/fragment/inter/${virtualEditionInter.externalId}">${virtualEditionInter.number}</a></td>
									<td><a 
										href="${contextPath}/fragments/fragment/inter/next/number/${virtualEditionInter.externalId}"><span
											class="glyphicon glyphicon-chevron-right"></span></a></td>
									<td></td>
								</tr>




								<tr>
									<c:choose>
										<c:when
											test="${empty recommender || !acronym.equals(virtualEdition.getAcronym())}">
											<td></td>
											<td></td>
											<td><input type="checkbox" style="visibility: hidden;" /></td>
											<!--<td>
												<c:choose>
	    	                                	<c:when test="${previousList.size()>0}">
	        	                            		<form id="prev-form" method="POST" action="${contextPath}/recommendation/fragment/prev/${prev.externalId}">
														<c:forEach var="previous" items='${previousList}'>
															<input type="hidden" name="id[]" value="${previous}">
														</c:forEach>
														<input type="hidden" name="current" value="${virtualEditionInter.externalId}">
														<input type="hidden" name="acronym" value="${virtualEditionInter.getVirtualEdition().getAcronym()}">
														<a class="btn recommender-nav" >
															<span class="glyphicon glyphicon-backward "></span></a>
													</form>	
	                                    		</c:when>
	                                    		<c:otherwise>
	                                    			<a class="btn" style="visibility: hidden;"><span class="glyphicon glyphicon-backward" ></span></a>
	                                    		</c:otherwise>
	                                    		</c:choose>
	                                    		</td>-->
											<td><a
												href="${contextPath}/recommendation/restricted/${virtualEditionInter.getVirtualEdition().getExternalId()}/${virtualEditionInter.externalId}">1</a>
											</td>
											<td>
												<form id="next-form" method="POST"
													action="${contextPath}/recommendation/inter/next/${virtualEditionInter.getNextInter().getExternalId()}">
													<input type="hidden" name="acronym"
														value="${virtualEditionInter.getVirtualEdition().getAcronym()}">
													<input type="hidden" name="current"
														value="${virtualEditionInter.externalId}"> <a
														class="btn recommender-nav"> <span
														class="glyphicon glyphicon-chevron-right"></span>
													</a>
												</form>
											</td>
											<td></td>
										</c:when>
										<c:otherwise>
											<td><input type="checkbox" style="visibility: hidden;" /></td>
											<td><c:choose>
													<c:when test="${previousList.size()>0}">
														<form id="prev-form" method="POST"
															action="${contextPath}/recommendation/inter/prev/${prev.externalId}">
															<c:forEach var="previous" items='${previousList}'>
																<input type="hidden" name="id[]" value="${previous}">
															</c:forEach>
															<input type="hidden" name="current"
																value="${virtualEditionInter.externalId}"> <input
																type="hidden" name="acronym"
																value="${virtualEditionInter.getVirtualEdition().getAcronym()}">
															<a class="btn recommender-nav"> <span
																class="glyphicon glyphicon-chevron-left"></span></a>
														</form>
													</c:when>
													<c:otherwise>
														<a class="btn" style="visibility: hidden;"> <span
															class="glyphicon glyphicon-chevron-left"> </span>
														</a>
													</c:otherwise>
												</c:choose></td>
											<td><a
												href="${contextPath}/recommendation/restricted/${virtualEditionInter.getVirtualEdition().getExternalId()}/${virtualEditionInter.externalId}">${previousList.size() + 1}</a></td>
											<td><c:if test="${empty last}">
													<form id="next-form" method="POST"
														action="${contextPath}/recommendation/inter/next/${next.getExternalId()}">
														<c:forEach var="previous" items='${previousList}'>
															<input type="hidden" name="id[]" value="${previous}">
														</c:forEach>
														<input type="hidden" name="acronym"
															value="${virtualEditionInter.getVirtualEdition().getAcronym()}">
														<input type="hidden" name="current"
															value="${virtualEditionInter.externalId}"> <a
															class="btn recommender-nav"> <span
															class="glyphicon glyphicon-chevron-right"></span>
														</a>
													</form>
												</c:if></td>
										</c:otherwise>
									</c:choose>
								</tr>
								<!--<a class="btn" href="${contextPath}/recommendation/inter/next//${next.externalId}">
                                    	<span class="glyphicon glyphicon-forward"></span></a>-->





							</c:forEach>
							<c:if
								test="${virtualEdition.participantSet.contains(user) && (inters.size() == 1) && virtualEdition.canAddFragInter(inters.get(0))}">
								<tr>
									<td></td>
									<td></td>
									<td></td>
									<td><form class="form-horizontal" method="POST"
											action="/virtualeditions/restricted/addinter/${virtualEdition.externalId}/${inters.get(0).externalId}">
											<fieldset>
												<button type="submit" class="btn btn-primary btn-xs">
													<span class="glyphicon glyphicon-plus"></span>
													<spring:message code="general.add" />
												</button>
											</fieldset>
										</form></td>
									<td></td>
									<td></td>
								</tr>
							</c:if>
						</tbody>
					</table>
				</c:forEach>
			</div>
		</c:if>
	</div>
</div>

<script type="text/javascript">
$('#infoexperts').popover();
$('#infovirtualeditions').popover();
</script>
