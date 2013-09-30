<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<div class="navbar-inner">
		<div class="container">
			<a class="brand" href="${contextPath}/"><spring:message
					code="header.title" /></a>
			<div class="offset3">
				<ul class="nav">
					<li class="dropdown"><a class="dropdown-toggle"
						data-toggle="dropdown" href="#"><spring:message
								code="header.about" /><b class="caret"></b></a>
						<ul class="dropdown-menu" role="menu" aria-labelledby="dLabel">
							<li><a href="#"><spring:message code="header.objectives" /></a></li>
							<li><a href="#"><spring:message
										code="header.editorialnotes" /></a></li>
							<li><a href="#"><spring:message code="header.funded" /></a></li>
							<li><a href="#"><spring:message
										code="header.editorialteam" /></a></li>
						</ul></li>
				</ul>
				<ul class="nav">
					<li class="dropdown"><a class="dropdown-toggle"
						data-toggle="dropdown" href="#"><spring:message
								code="header.editions" /><b class="caret"></b></a>
						<ul class="dropdown-menu" role="menu" aria-labelledby="dLabel">
							<li><a href="${contextPath}/edition/acronym/JPC">Jacinto
									Prado Coelho</a></li>
							<li><a href="${contextPath}/edition/acronym/TSC">Teresa
									Sobral Cunha</a></li>
							<li><a href="${contextPath}/edition/acronym/RZ">Richard
									Zenith</a></li>
							<li><a href="${contextPath}/edition/acronym/JP">Jerónimo
									Pizarro</a></li>
							<c:forEach var="acronym" items='${ldoDSession.selectedVEAcr}'>
								<li><a href="${contextPath}/edition/acronym/${acronym}">${acronym}</a></li>
							</c:forEach>
						</ul></li>
				</ul>
				<ul class="nav">
					<li class="dropdown"><a class="dropdown-toggle"
						data-toggle="dropdown" href="#"><spring:message
								code="header.search" /><b class="caret"></b></a>
						<ul class="dropdown-menu" role="menu" aria-labelledby="dLabel">
							<li><a href="${contextPath}/search/fragments"><spring:message
										code="header.listfragments" /></a></li>
							<li><a href="#"><spring:message
										code="header.authorialsources" /></a></li>
						</ul></li>
				</ul>
				<ul class="nav">
					<li class="dropdown"><a class="dropdown-toggle"
						data-toggle="dropdown" href="#"><spring:message
								code="header.virtual" /><b class="caret"></b></a>
						<ul class="dropdown-menu" role="menu" aria-labelledby="dLabel">
							<li><a href="${contextPath}/virtualeditions"><spring:message
										code="header.manageeditions" /></a></li>
						</ul></li>
				</ul>
				<ul class="nav">
					<li class="dropdown"><a class="dropdown-toggle"
						data-toggle="dropdown" href="#"><spring:message
								code="header.admin" /><b class="caret"></b></a>
						<ul class="dropdown-menu" role="menu" aria-labelledby="dLabel">
							<li><a href="${contextPath}/admin/load/corpusForm"><spring:message
										code="header.loadcorpus" /></a></li>
							<li><a href="${contextPath}/admin/load/fragmentForm"><spring:message
										code="header.loadfragment" /></a></li>
							<li class="divider"></li>
							<li><a href="${contextPath}/admin/fragment/list"><spring:message
										code="header.deletefragment" /></a></li>
						</ul></li>
				</ul>
				<ul class="nav pull-right">
					<li class="dropdown"><a class="dropdown-toggle"
						data-toggle="dropdown" href="#"><i class="icon-flag"></i><b class="caret"></b></a>
						<ul class="dropdown-menu" role="menu" aria-labelledby="dLabel">
							<li><a href="?lang=pt_PT">Português</a></li>
							<li><a href="?lang=en">English</a></li>
							<li><a href="?lang=es">Español</a></li>
						</ul></li>
				</ul>
				<ul class="nav pull-right">
					<li><c:choose>
							<c:when test="${pageContext.request.userPrincipal.authenticated}">
								<a href="<c:url value="j_spring_security_logout"/>"><spring:message
										code="header.logout" /></a>
							</c:when>
							<c:otherwise>
								<a href="${contextPath}/login"><spring:message
										code="header.login" /></a>
							</c:otherwise>
						</c:choose></li>
				</ul>
			</div>
		</div>
	</div>
