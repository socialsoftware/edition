<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="container">
	<!-- Collect the nav links, forms, and other content for toggling -->
	<div class="navbar-header navbar-left col-md-3">
		<button type="button" class="navbar-toggle" data-toggle="collapse"
			data-target=".navbar-collapse">
			<span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span>
			<span class="icon-bar"></span> <span class="icon-bar"></span>
		</button>
		<div class="navbar-inner">
			<a class="navbar-brand" href="${contextPath}/"><spring:message
					code="header.title" /></a>
		</div>
	</div> 
	<div class="collapse navbar-collapse navbar-ex1-collapse">
		<ul class="nav navbar-nav">
			<li class="dropdown"><a href="#" class="dropdown-toggle"
				data-toggle="dropdown"><spring:message code="header.about" /> <b
					class="caret"></b></a>
				<ul class="dropdown-menu">
					<li><a href="#"><spring:message code="header.objectives" /></a></li>
					<li><a href="#"><spring:message
								code="header.editorialnotes" /></a></li>
					<li><a href="#"><spring:message code="header.funded" /></a></li>
					<li><a href="#"><spring:message
								code="header.editorialteam" /></a></li>
				</ul></li>
			<li><a href="${contextPath}/source/list"><spring:message
						code="header.documents" /></a></li>
			<li class="dropdown"><a href="#" class="dropdown-toggle"
				data-toggle="dropdown"><spring:message code="header.editions" />
					<b class="caret"></b></a>
				<ul class="dropdown-menu">
					<li><a href="${contextPath}/edition/acronym/JPC">Jacinto
							do Prado Coelho</a></li>
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
			<!-- Search -->
			<li class='dropdown'>
				<a href="#" class="dropdown-toggle" data-toggle="dropdown">
					<spring:message code="header.search" />
					<b class='caret'></b>
				</a>
				<ul class="dropdown-menu">
					<li>
						<a href="${contextPath}/search/simple">
							<spring:message code="header.search.simple" />
						</a>
					</li>
					<li>
						<a href="${contentPath}/search/advanced">
							<spring:message code="header.search.advanced" />
						</a>
					</li>
					<li>
						<a href="${contentPath}/search/fragments">
							<spring:message code="fragmentlist.title" />
						</a>
					</li>
				</ul>
			</li>
			<!-- Manage Virtual Editions -->
			<li><a href="${contextPath}/virtualeditions"><spring:message
						code="virtual" /> </a></li>
			<!-- Administration -->
			<li class="dropdown"><a href="#" class="dropdown-toggle"
				data-toggle="dropdown"><spring:message code="header.admin" /> <b
					class="caret"></b></a>
				<ul class="dropdown-menu">
					<li><a href="${contextPath}/admin/load/corpusForm"><spring:message
								code="corpus.load" /></a></li>
					<li><a href="${contextPath}/admin/load/fragmentFormAtOnce"><spring:message
								code="fragment.load" /></a></li>
					<li><a href="${contextPath}/admin/load/fragmentFormStepByStep"><spring:message
								code="fragments.load" /></a></li>
					<li class="divider"></li>
					<li><a href="${contextPath}/admin/exportForm"><spring:message
                                code="general.export" /></a></li>
					<li class="divider"></li>
					<li><a href="${contextPath}/admin/fragment/list"><spring:message
								code="fragment.delete" /></a></li>
				</ul></li>
		</ul>
		<ul class="nav navbar-nav navbar-right">
			<!-- Login -->
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
			<!--  Language -->
			<li class="dropdown"><a href="#" class="dropdown-toggle"
				data-toggle="dropdown"><span class="glyphicon glyphicon-flag"></span><b
					class="caret"></b></a>
				<ul class="dropdown-menu">
					<li><a href="?lang=pt_PT">Português</a></li>
					<li><a href="?lang=en">English</a></li>
					<li><a href="?lang=es">Español</a></li>
				</ul></li>
		</ul>
	</div>
</div>