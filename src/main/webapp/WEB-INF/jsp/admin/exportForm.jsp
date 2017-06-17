<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

	<div class="container">
		<h2>EXPORTAR</h2>
		<h3 class="text-center">Exportar Utilizadores</h3>
		<hr>
		<div class="row">
			<form class="form-inline" method="GET"
				action="${contextPath}/admin/export/users">
				<button type="submit" class="btn btn-primary">
					<span class="glyphicon glyphicon-export"></span> Exportar
					utilizadores
				</button>
				<br>
				<br>
			</form>
		</div>
		<h3 class="text-center">Exportar Edições Virtuais</h3>
		<hr>
		<div class="row">
			<form class="form-inline" method="GET"
				action="${contextPath}/admin/export/virtualeditions">
				<button type="submit" class="btn btn-primary">
					<span class="glyphicon glyphicon-export"></span> Exportar edições
					virtuais
				</button>
				<br>
				<br>
			</form>
		</div>
		<h3 class="text-center">Exportar Fragmentos</h3>
		<hr>
		<div class="row">
			<form class="form-inline" method="GET"
				action="${contextPath}/admin/exportAll">
				<button type="submit" class="btn btn-primary">
					<span class="glyphicon glyphicon-export"></span> Exportar arquivo
					completo
				</button>
				<br>
				<br> <small><i>(poderá demorar alguns instantes)</i></small>
			</form>
		</div>
		<hr>
		<div class="row">
			<form class="form-inline" method="GET"
				action="${contextPath}/admin/exportRandom">
				<button type="submit" class="btn btn-primary">
					<span class="glyphicon glyphicon-export"></span> Exportar 3
					fragmentos aleatórios
				</button>
			</form>
		</div>
		<hr>
		<div class="row">
			<form class="form-inline" method="POST"
				action="${contextPath}/admin/exportSearch">
				<input type="hidden" name="${_csrf.parameterName}"
					value="${_csrf.token}" /> <input type="text" class="form-control"
					name="query" placeholder="pesquisar titulo" /> <br>
				<br>
				<button type="submit" class="btn btn-primary">
					<span class="glyphicon glyphicon-export"></span> Pesquisar
				</button>
			</form>
			<c:if test="${nResults > 0}">
				<br>
				<form class="form-inline" method="POST"
					action="${contextPath}/admin/exportSearchResult">
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" /> <input type="hidden" name="query"
						value="${query}" />
					<button type="submit" class="btn btn-primary">
						<span class="glyphicon glyphicon-export"></span> Exportar
						${nResults} frags
					</button>
				</form>
			</c:if>
		</div>
		<hr>
		<div class="row">
			<c:choose>
				<c:when test="${nResults > 0}">
      			Nº de fragmentos encontrados: ${nResults}
      			<table class="table table-bordered table-condensed">
						<c:forEach var="frag" items='${frags}'>
							<tr>
								<td>${frag}</td>
							</tr>
						</c:forEach>
					</table>
				</c:when>

				<c:otherwise>
					<c:if test="${nResults == 0}">
     		 	Não foram encontrados fragmentos com o valor de pesquisa: "${query}".
     		 	</c:if>
				</c:otherwise>
			</c:choose>
		</div>
	</div>

</body>
</html>
