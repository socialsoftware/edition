<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="container">
    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse navbar-ex1-collapse">

        <ul class="nav navbar-nav navbar-left col-md-3">
            <li><a class="navbar-brand" href="${contextPath}/"><spring:message
                        code="header.title" /></a></li>
        </ul>

        <ul class="nav navbar-nav">
            <li class="dropdown"><a href="#"
                class="dropdown-toggle" data-toggle="dropdown"><spring:message
                        code="header.about" /> <b class="caret"></b></a>
                <ul class="dropdown-menu">
                    <li><a href="#"><spring:message
                                code="header.objectives" /></a></li>
                    <li><a href="#"><spring:message
                                code="header.editorialnotes" /></a></li>
                    <li><a href="#"><spring:message
                                code="header.funded" /></a></li>
                    <li><a href="#"><spring:message
                                code="header.editorialteam" /></a></li>
                </ul></li>
            <li class="dropdown"><a href="#"
                class="dropdown-toggle" data-toggle="dropdown"><spring:message
                        code="header.editions" /> <b class="caret"></b></a>
                <ul class="dropdown-menu">
                    <li><a
                        href="${contextPath}/edition/acronym/JPC">Jacinto do
                            Prado Coelho</a></li>
                    <li><a
                        href="${contextPath}/edition/acronym/TSC">Teresa
                            Sobral Cunha</a></li>
                    <li><a href="${contextPath}/edition/acronym/RZ">Richard
                            Zenith</a></li>
                    <li><a href="${contextPath}/edition/acronym/JP">Jerónimo
                            Pizarro</a></li>
                    <c:forEach var="acronym"
                        items='${ldoDSession.selectedVEAcr}'>
                        <li><a
                            href="${contextPath}/edition/acronym/${acronym}">${acronym}</a></li>
                    </c:forEach>
                </ul></li>
            <li class="dropdown"><a href="#"
                class="dropdown-toggle" data-toggle="dropdown"><spring:message
                        code="header.search" /> <b class="caret"></b></a>
                <ul class="dropdown-menu">
                    <li><a href="${contextPath}/search/fragments"><spring:message
                                code="header.listfragments" /></a></li>
                    <li><a href="#"><spring:message
                                code="header.authorialsources" /></a></li>
                </ul></li>
            <!-- Manage Virtual Editions -->
            <li class="dropdown"><a href="#"
                class="dropdown-toggle" data-toggle="dropdown"><spring:message
                        code="header.virtual" /> <b class="caret"></b></a>
                <ul class="dropdown-menu">
                    <li><a href="${contextPath}/virtualeditions"><spring:message
                                code="header.manageeditions" /></a></li>
                </ul></li>
            <!-- Administration -->
            <li class="dropdown"><a href="#"
                class="dropdown-toggle" data-toggle="dropdown"><spring:message
                        code="header.admin" /> <b class="caret"></b></a>
                <ul class="dropdown-menu">
                    <li><a
                        href="${contextPath}/admin/load/corpusForm"><spring:message
                                code="header.loadcorpus" /></a></li>
                    <li><a
                        href="${contextPath}/admin/load/fragmentForm"><spring:message
                                code="header.loadfragment" /></a></li>
                    <li class="divider"></li>
                    <li><a
                        href="${contextPath}/admin/fragment/list"><spring:message
                                code="header.deletefragment" /></a></li>
                </ul></li>
        </ul>

        <ul class="nav navbar-nav navbar-right">
            <!-- Login -->
            <li><c:choose>
                    <c:when
                        test="${pageContext.request.userPrincipal.authenticated}">
                        <a
                            href="<c:url value="j_spring_security_logout"/>"><spring:message
                                code="header.logout" /></a>
                    </c:when>
                    <c:otherwise>
                        <a href="${contextPath}/login"><spring:message
                                code="header.login" /></a>
                    </c:otherwise>
                </c:choose></li>
            <!--  Language -->
            <li class="dropdown"><a href="#"
                class="dropdown-toggle" data-toggle="dropdown"><span
                    class="glyphicon glyphicon-flag"></span><b
                    class="caret"></b></a>
                <ul class="dropdown-menu">
                    <li><a href="?lang=pt_PT">Português</a></li>
                    <li><a href="?lang=en">English</a></li>
                    <li><a href="?lang=es">Español</a></li>
                </ul></li>
        </ul>
    </div>
</div>