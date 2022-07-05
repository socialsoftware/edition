<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

	<div class="container">
		<div class="row">
			<div class="col-md-4">
				<h4>
					<spring:message code="corpus.load.title" />
				</h4>
				<form class="form-inline" method="POST"
					action="${contextPath}/admin/load/corpus"
					enctype="multipart/form-data">
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />
					<div class="form-group">
						<input type="file" name="file">
					</div>
					<br> <br>
					<div class="form-group">
						<button type="submit" class="btn btn-primary btn-sm">
							<spring:message code="general.submit" />
						</button>
					</div>
				</form>
			</div>
			<div class="col-md-4">
				<h4>
					<spring:message code="fragment.load.title" />
				</h4>
				<form class="form-inline" method="POST"
					action="${contextPath}/admin/load/fragmentsAtOnce"
					enctype="multipart/form-data">
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />
					<div class="form-group">
						<input type="file" name="file">
					</div>
					<br> <br>
					<div class="form-group">
						<button type="submit" class="btn btn-primary btn-sm">
							<spring:message code="general.submit" />
						</button>
					</div>
				</form>
			</div>
			<div class="col-md-4">
				<h4>
					<spring:message code="fragment.load.titleAll" />
				</h4>
				<form class="form-inline" method="POST"
					action="${contextPath}/admin/load/fragmentsStepByStep"
					enctype="multipart/form-data">
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />
					<div class="form-group">
						<input type="file" multiple name="files">
					</div>
					<br> <br>
					<div class="form-group">
						<button type="submit" class="btn btn-primary btn-sm">
							<spring:message code="general.submit" />
						</button>
					</div>
				</form>
			</div>
		</div>
		<br/>
		<br/>
		<br/>
		<div class="row">
			<div class="col-md-4">
				<h4>Load Users</h4>
				<form class="form-inline" method="POST"
					action="${contextPath}/admin/load/users"
					enctype="multipart/form-data">
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />
					<div class="form-group">
						<input type="file" name="file">
					</div>
					<br> <br>
					<div class="form-group">
						<button type="submit" class="btn btn-primary btn-sm">
							<spring:message code="general.submit" />
						</button>
					</div>
				</form>
			</div>
			<div class="col-md-4">
				<h4>Load Virtual Editions Corpus</h4>
				<form class="form-inline" method="POST"
					action="${contextPath}/admin/load/virtual-corpus"
					enctype="multipart/form-data">
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />
					<div class="form-group">
						<input type="file" name="file">
					</div>
					<br> <br>
					<div class="form-group">
						<button type="submit" class="btn btn-primary btn-sm">
							<spring:message code="general.submit" />
						</button>
					</div>
				</form>
			</div>
			<div class="col-md-4">
				<h4>Load Virtual Editions Fragments</h4>
				<form class="form-inline" method="POST"
					action="${contextPath}/admin/load/virtual-fragments"
					enctype="multipart/form-data">
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />
					<div class="form-group">
						<input type="file" multiple name="files">
					</div>
					<br> <br>
					<div class="form-group">
						<button type="submit" class="btn btn-primary btn-sm">
							<spring:message code="general.submit" />
						</button>
					</div>
				</form>
			</div>
		</div>
		<br />
		<div class="hero-unit">
			<c:if test="${message != null}">
				<c:choose>
					<c:when test="${error}">
						<div class="alert alert-danger">
							<a class="close" data-dismiss="alert"></a> <strong>${message}</strong>
						</div>
					</c:when>
					<c:otherwise>
						<div class="alert alert-success">
							<a class="close" data-dismiss="alert"></a> <strong>${message}
							</strong>
						</div>
					</c:otherwise>
				</c:choose>
			</c:if>
		</div>
	</div>
</body>
</html>