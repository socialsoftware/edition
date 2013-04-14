<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false"%>
<script type="text/javascript">
	$(document).ready(function() {
		$('[id="interps2"][data-toggle="buttons-radio"]').on('click', function() {
			var fragInter1 = $('input:radio[name=inter]:checked').val();
			var fragInter2 = $('input:radio[name=inter2]:checked').val();
			alert(fragInter2);
			$.get("${contextPath}/fragments/fragment/textual", {
				interp : fragInter1,
				interp2Compare : fragInter2
			}, function(html) {
				$("#fragmentTextual").replaceWith(html);
			});
		});
	});
</script>

<div id="fragmentInterpretation" class="row">

	<c:if test="${inter!=null}">
		<br />
		<div id="menu" class="row span12">
			<div class="row">
				<label> Comparar: </label>
					<div class="btn-group" id="interps2" data-toggle="buttons-radio">
						<c:forEach var="fragInter" items='${fragment.sortedInterps}'>
							<label class="radio inline"> <c:choose>
									<c:when
										test="${fragInter.externalId==inter.externalId}">
										<input type="radio" class="btn" name="inter2"
											value="${fragInter.externalId}" checked>
							${fragInter.name} </input>
									</c:when>
									<c:otherwise>
										<input type="radio" class="btn" name="inter2"
											value="${fragInter.externalId}">
							${fragInter.name} </input>
									</c:otherwise>
								</c:choose>
							</label>
						</c:forEach>
					</div>
			</div>

			<div>
				<c:choose>
					<c:when test="${inter.sourceType=='EDITORIAL'}">
						<label>Testemunho Editorial</label>
					</c:when>
					<c:otherwise>
						<label>Testemunho Autoral</label>
					</c:otherwise>
				</c:choose>
			</div>
		</div>

		<%@ include file="/WEB-INF/jsp/fragmentTextual.jsp"%>

	</c:if>
</div>
