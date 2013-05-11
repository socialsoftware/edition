<div class="navbar navbar-fixed-top">
	<div class="navbar-inner">
		<div class="container">
			<a class="brand" href="${contextPath}/"><spring:message code="header.title" /></a>
			<div class="offset3">
				<ul class="nav">
					<li class="dropdown"><a class="dropdown-toggle"
						data-toggle="dropdown" href="#"><spring:message code="header.about" /><b class="caret"></b></a>
						<ul class="dropdown-menu" role="menu" aria-labelledby="dLabel">
							<li><a href="#"><spring:message code="header.objectives" /></a></li>
							<li><a href="#"><spring:message code="header.editorialnotes" /></a></li>
							<li><a href="#"><spring:message code="header.funded" /></a></li>
							<li><a href="#"><spring:message code="header.editorialteam" /></a></li>
						</ul></li>
				</ul>
				<ul class="nav">
					<li class="dropdown"><a class="dropdown-toggle"
						data-toggle="dropdown" href="#"><spring:message code="header.editions" /><b class="caret"></b></a>
						<ul class="dropdown-menu" role="menu" aria-labelledby="dLabel">
							<li><a href="${contextPath}/edition/acronym/JPC">Jacinto
									Prado Coelho</a></li>
							<li><a href="${contextPath}/edition/acronym/TSC">Teresa
									Sobral Cunha</a></li>
							<li><a href="${contextPath}/edition/acronym/RZ">Richard
									Zenith</a></li>
							<li><a href="${contextPath}/edition/acronym/JP">Jerónimo
									Pizarro</a></li>
							<li><a href="#"><spring:message code="header.virtualeditions" /></a></li>
						</ul></li>
				</ul>
				<ul class="nav">
					<li class="dropdown"><a class="dropdown-toggle"
						data-toggle="dropdown" href="#"><spring:message code="header.search" /><b class="caret"></b></a>
						<ul class="dropdown-menu" role="menu" aria-labelledby="dLabel">
							<li><a href="${contextPath}/search/fragments"><spring:message code="header.listfragments" /></a></li>
							<li><a href="#"><spring:message code="header.authorialsources" /></a></li>
						</ul></li>
				</ul>
				<ul class="nav">
					<li class="dropdown"><a class="dropdown-toggle"
						data-toggle="dropdown" href="#"><spring:message code="header.virtual" /><b class="caret"></b></a>
						<ul class="dropdown-menu" role="menu" aria-labelledby="dLabel">
							<li><a href="${contextPath}/login"><spring:message code="header.login" /></a></li>
							<li><a href="<c:url value="j_spring_security_logout"/>"><spring:message code="header.logout" /></a></li>
							<li><a href="#"><spring:message code="header.communities" /></a></li>
						</ul></li>
				</ul>
				<ul class="nav">
					<li class="dropdown"><a class="dropdown-toggle"
						data-toggle="dropdown" href="#"><spring:message code="header.admin" /><b class="caret"></b></a>
						<ul class="dropdown-menu" role="menu" aria-labelledby="dLabel">
							<li><a href="${contextPath}/load/corpusForm"><spring:message code="header.loadcorpus" /></a></li>
							<li><a href="${contextPath}/load/fragmentForm"><spring:message code="header.loadfragment" /></a></li>
							<li class="divider"></li>
							<li><a href="${contextPath}/fragments/delete"><spring:message code="header.deletefragment" /></a></li>
						</ul></li>
				</ul>

			</div>
			<div class="text-right">
				<a href="?lang=pt_PT">pt</a> | <a href="?lang=en">en | <a href="?lang=es">es</a>
			</div>
		</div>
	</div>
</div>
