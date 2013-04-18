<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/ldoD.tld" prefix="ldod"%>
<%@ page session="false"%>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$('[id="interps2"][data-toggle="buttons-radio"]')
								.on(
										'click',
										function() {
											var fragInter1 = $(
													'input:radio[name=inter]:checked')
													.val();
											var fragInter2 = $(
													'input:radio[name=inter2]:checked')
													.val();
											$
													.get(
															"${contextPath}/fragments/fragment/interpretation",
															{
																interp : fragInter1,
																interp2Compare : fragInter2
															},
															function(html) {
																$(
																		"#fragmentTextual")
																		.replaceWith(
																				html);
															});
										});
					});
</script>

<div id="fragmentInterpretation" class="row-fluid">

	<c:if test="${inter!=null}">
		<legend>Testemunho a Comparar</legend>
			
				<div class="btn-group well" id="interps2" data-toggle="buttons-radio">
					<c:forEach var="fragInter" items='${inter.fragment.sortedInterps}'>
						<c:if test="${fragInter.sourceType=='EDITORIAL'}">
							<label class="radio inline"> <c:choose>
									<c:when test="${fragInter.externalId==inter.externalId}">
										<input type="radio" class="btn" name="inter2"
											value="${fragInter.externalId}" checked />
							${fragInter.name} (${ldod:getPercentage(writer,fragInter)}%)
									</c:when>
									<c:otherwise>
										<input type="radio" class="btn" name="inter2"
											value="${fragInter.externalId}" />
							${fragInter.name} (${ldod:getPercentage(writer,fragInter)}%)
									</c:otherwise>
								</c:choose>
							</label>
						</c:if>
					</c:forEach>
					<br>
					<c:forEach var="fragInter" items='${inter.fragment.sortedInterps}'>
						<c:if test="${fragInter.sourceType=='AUTHORIAL'}">
							<label class="radio inline"> <c:choose>
									<c:when test="${fragInter.externalId==inter.externalId}">
										<input type="radio" class="btn" name="inter2"
											value="${fragInter.externalId}" checked />
							${fragInter.name} (${ldod:getPercentage(writer,fragInter)}%)
									</c:when>
									<c:otherwise>
										<input type="radio" class="btn" name="inter2"
											value="${fragInter.externalId}" />
							${fragInter.name} (${ldod:getPercentage(writer,fragInter)}%)
									</c:otherwise>
								</c:choose>
							</label>
						</c:if>
					</c:forEach>

				
			</div>

		<%@ include file="/WEB-INF/jsp/fragmentTextual.jsp"%>

	</c:if>
</div>
