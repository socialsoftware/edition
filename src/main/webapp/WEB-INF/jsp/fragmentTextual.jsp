<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false"%>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$(
								'[id="visualisation-properties-authorial"][data-toggle="buttons-checkbox"]')
								.on(
										'click',
										function() {
											var fragInter2 = $(
													'input:radio[name=inter2]:checked')
													.val();
											var selDel = $(
													'input:checkbox[name=del]')
													.is(':checked');
											var selIns = $(
													'input:checkbox[name=ins]')
													.is(':checked');
											var selSubst = $(
													'input:checkbox[name=subst]')
													.is(':checked');
											var selNotes = $(
													'input:checkbox[name=notes]')
													.is(':checked');
											$
													.get(
															"${contextPath}/fragments/fragment/textualauthorial",
															{
																interp : fragInter2,
																del : selDel,
																ins : selIns,
																subst : selSubst,
																notes : selNotes
															},
															function(html) {
																$(
																		"#fragmentTranscription")
																		.replaceWith(
																				html);
															});
										});
					});
</script>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$(
								'[id="visualisation-properties-editorial"][data-toggle="buttons-checkbox"]')
								.on(
										'click',
										function() {
											var fragInter2 = $(
													'input:radio[name=inter2]:checked')
													.val();
											var selDiff = $(
													'input:checkbox[name=diff]')
													.is(':checked');
											$
											.get(
													"${contextPath}/fragments/fragment/textualeditorial",
													{
														interp : fragInter2,
														diff : selDiff
													},
													function(html) {
														$(
																"#fragmentTranscription")
																.replaceWith(
																		html);
													});
									
										});
					});
</script>

<div id=fragmentTextual class="row-fluid">
	<div class="row-fluid">

		<c:choose>
			<c:when test="${inter.sourceType=='EDITORIAL'}">
				<legend>Atributos de Visualização do Testemunho Editorial</legend>
				<div class="btn-group well" id="visualisation-properties-editorial"
					data-toggle="buttons-checkbox">
					<label class="checkbox inline"> <input type="checkbox"
						class="btn" name=diff value="Yes"> Realçar Diferenças
					</label>
				</div>
			</c:when>
			<c:otherwise>
				<legend>Atributos de Visualização do Testemunho Autoral</legend>
				<div class="btn-group well" id="visualisation-properties-authorial"
					data-toggle="buttons-checkbox">
					<label class="checkbox inline"> <input type="checkbox"
						class="btn" name=del value="Yes"> Mostrar Apagados
					</label> <label class="checkbox inline"> <input type="checkbox"
						class="btn" name=ins value="Yes" checked> Realçar
						Inseridos
					</label> <label class="checkbox inline"> <input type="checkbox"
						class="btn" name=subst value="Yes"> Realçar Substituições
					</label> <label class="checkbox inline"> <input type="checkbox"
						class="btn" name=notes value="Yes" checked> Mostrar Notas
					</label>
				</div>
			</c:otherwise>
		</c:choose>
	</div>



	<%@ include file="/WEB-INF/jsp/fragmentTranscription.jsp"%>

	<br>
	<div id="metatextual" class="row-fluid">
		<div class="addBorder row-fluid">
			<c:choose>
				<c:when test="${inter.sourceType=='EDITORIAL'}">
					<c:if test="${inter.title!=''}">
						<em>Título</em>: ${inter.title}</c:if>
					<br>
					<c:if test="${inter.heteronym.name!=''}">
						<em>Heterónimo</em>: ${inter.heteronym.name}</c:if>
					<br>
					<c:if test="${inter.number!=''}">
						<em>Número</em>: ${inter.number}</c:if>
					<br>
					<c:if test="${inter.page!=''}">
						<em>Página</em>: ${inter.page}</c:if>
					<br>
					<c:if test="${inter.date!=''}">
						<em>Data</em>: ${inter.date}</c:if>
					<br>
					<c:if test="${inter.notes!=''}">
						<em>Notas</em>: ${inter.notes}</c:if>
				</c:when>
				<c:otherwise>
				TODO: Incluir meta informação da fonte manuscrita ou impressa
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</div>