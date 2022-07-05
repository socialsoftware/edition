<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="container-fluid">

    <div class="container">

        <div class="navbar-header">

            <button type="button" class="navbar-toggle" data-toggle="collapse"
                    data-target=".navbar-collapse">

                <span class="sr-only">Toggle navigation</span> <span
                    class="icon-bar"></span> <span class="icon-bar"></span> <span
                    class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="${contextPath}/"><spring:message
                    code="header.title"/></a>

            <ul class="nav navbar-nav navbar-right hidden-xs">

                <!-- Login -->
                <c:choose>
                    <c:when test="${pageContext.request.userPrincipal.authenticated}">
                        <li class="dropdown login logged-in"><a href="#"
                        class="dropdown-toggle" data-toggle="dropdown">

                        ${pageContext.request.userPrincipal.principal.getUser().getFirstName()}
                        ${pageContext.request.userPrincipal.principal.getUser().getLastName()}
                        <span class="caret"></span>
                        </a>

                        <ul class="dropdown-menu">
                            <li><a
                                    href="<c:url value="${contextPath}/user/changePassword"/>"><spring:message
                                    code="user.password"/></a></li>
                            <li><a href="<c:url value="${contextPath}/signout"/>"><spring:message
                                    code="header.logout"/></a></li>
                        </ul>
                    </c:when>
                    <c:otherwise>
                        <li><a href="${contextPath}/signin"><spring:message
                                code="header.login"/></a></li>
                    </c:otherwise>
                </c:choose>

            </ul>

        </div>

    </div>

</div>

<div class="container">

    <div class="collapse navbar-collapse">
        <ul class="nav navbar-nav navbar-nav-flex">

            <!-- About -->
            <li class="dropdown"><a href="#" class="dropdown-toggle"
                                    data-toggle="dropdown"> <spring:message code="header.about"/>
                <span class="caret"></span>
            </a>
                <ul class="dropdown-menu">
                    <div class="dropdown-menu-bg"></div>
                    <li><a href="${contextPath}/about/archive"><spring:message
                            code="header.archive"/></a></li>
                    <li><a href="${contextPath}/about/videos"><spring:message
                            code="header.videos"/></a></li>
                    <li><a href="${contextPath}/about/tutorials"><spring:message
                            code="header.tutorials"/></a></li>
                    <li><a href="${contextPath}/about/faq"><spring:message
                            code="header.faq"/></a></li>
                    <li><a href="${contextPath}/about/encoding"><spring:message
                            code="header.encoding"/></a></li>
                    <li><a href="${contextPath}/about/articles"><spring:message
                            code="header.bibliography"/></a></li>
                    <li><a href="${contextPath}/about/book"><spring:message
                            code="header.book"/></a></li>
                    <li><a href="${contextPath}/about/conduct"><spring:message
                            code="header.conduct"/></a></li>
                    <li><a href="${contextPath}/about/privacy"><spring:message
                            code="header.privacy"/></a></li>
                    <li><a href="${contextPath}/about/team"><spring:message
                            code="header.team"/></a></li>
                    <li><a href="${contextPath}/about/acknowledgements"><spring:message
                            code="header.acknowledgements"/></a></li>
                    <li><a href="${contextPath}/about/contact"><spring:message
                            code="header.contact"/></a></li>
                    <li><a href="${contextPath}/about/copyright"><spring:message
                            code="header.copyright"/></a></li>
                </ul>
            </li>

            <!-- Reading -->
            <li class="dropdown"><a href="#" class="dropdown-toggle"
                                    data-toggle="dropdown"> <spring:message code="general.reading"/>
                <span class="caret"></span>
            </a>
                <ul class="dropdown-menu">
                    <div class="dropdown-menu-bg"></div>
                    <li><a href="${contextPath}/reading"><spring:message
                            code="general.reading.sequences"/></a></li>
                    <li><a href="${contextPath}/ldod-visual"><spring:message
                            code="general.reading.visual"/></a></li>
                    <li><a href="${contentPath}/citations"><spring:message
                            code="general.citations.twitter"/></a></li>

                </ul>
            </li>


            <!-- Documents -->
            <li class="dropdown"><a href="#" class="dropdown-toggle"
                                    data-toggle="dropdown"> <spring:message code="header.documents"/>
                <span class="caret"></span>
            </a>
                <ul class="dropdown-menu">
                    <div class="dropdown-menu-bg"></div>
                    <li><a href="${contextPath}/source/list"><spring:message
                            code="authorial.source"/></a></li>
                    <li><a href="${contentPath}/fragments"><spring:message
                            code="fragment.codified"/></a></li>
                </ul>
            </li>

            <!-- Editions -->
            <li class="dropdown"><a href="#" class="dropdown-toggle"
                                    data-toggle="dropdown"> <spring:message code="header.editions"/>
                <span class="caret"></span>
            </a>
                <ul class="dropdown-menu">
                    <div class="dropdown-menu-bg"></div>
                    <li><a href="${contextPath}/edition/acronym/JPC"><spring:message
                            code="general.editor.prado"/></a></li>
                    <li><a href="${contextPath}/edition/acronym/TSC"><spring:message
                            code="general.editor.cunha"/></a></li>
                    <li><a href="${contextPath}/edition/acronym/RZ"><spring:message
                            code="general.editor.zenith"/></a></li>
                    <li><a href="${contextPath}/edition/acronym/JP"><spring:message
                            code="general.editor.pizarro"/></a></li>
                    <li class="divider"></li>
                    <li><a href="${contextPath}/edition/acronym/LdoD-Arquivo">Arquivo
                        LdoD</a></li>
                    <li class="divider"></li>
                    <c:forEach var="acronym" items='${ldoDSession.selectedVEAcr}'>
                        <li><a href="${contextPath}/edition/acronym/${acronym}">${acronym}</a></li>
                    </c:forEach>
                </ul>
            </li>

            <!-- Search -->
            <li class='dropdown'><a href="#" class="dropdown-toggle"
                                    data-toggle="dropdown"> <spring:message code="header.search"/>
                <span class='caret'></span>
            </a>

                <ul class="dropdown-menu">
                    <div class="dropdown-menu-bg"></div>
                    <li><a href="${contextPath}/search/simple"> <spring:message
                            code="header.search.simple"/></a></li>
                    <li><a href="${contentPath}/search/advanced"> <spring:message
                            code="header.search.advanced"/></a></li>
                </ul>
            </li>

            <!-- Manage Virtual Editions -->
            <li class='dropdown'><a href="#" class="dropdown-toggle"
                                    data-toggle="dropdown"> <spring:message code="virtual"/> <span
                    class='caret'></span>
            </a>

                <ul class="dropdown-menu">
                    <div class="dropdown-menu-bg"></div>
                    <li><a href="${contextPath}/virtualeditions"> <spring:message
                            code="header.virtualeditions"/></a></li>
                    <li><a href="${contentPath}/classificationGames"> <spring:message
                            code="general.classificationGame"/></a></li>
                </ul>
            </li>

            <!-- Administration -->

            <c:if
                    test='${pageContext.request.userPrincipal.principal.hasRole("ROLE_ADMIN")}'>
                <li class="dropdown"><a href="#" class="dropdown-toggle"
                                        data-toggle="dropdown"> <spring:message code="header.admin"/>

                    <span class="caret"></span>
                </a>

                    <ul class="dropdown-menu">
                        <div class="dropdown-menu-bg"></div>
                        <li><a href="${contextPath}/admin/loadForm"><spring:message
                                code="load"/></a></li>
                        <li class="divider"></li>
                        <li><a href="${contextPath}/admin/exportForm"><spring:message
                                code="general.export"/></a></li>
                        <li class="divider"></li>
                        <li><a href="${contextPath}/admin/fragment/list"><spring:message
                                code="fragment.delete"/></a></li>
                        <li class="divider"></li>
                        <li><a href="${contextPath}/admin/user/list"><spring:message
                                code="user.manage"/></a></li>
                        <li class="divider"></li>
                        <li><a href="${contextPath}/admin/virtual/list">Manage
                            Virtual Editions</a></li>
                        <li class="divider"></li>
                        <li><a href="${contextPath}/admin/tweets">Manage Tweets</a></li>
                    </ul>
                </li>
            </c:if>

            <!-- Login -->
            <c:choose>
                <c:when test="${pageContext.request.userPrincipal.authenticated}">
                    <li class="dropdown login logged-in visible-xs"><a href="#"
                    class="dropdown-toggle" data-toggle="dropdown">

                    ${pageContext.request.userPrincipal.principal.getUser().getFirstName()}
                    ${pageContext.request.userPrincipal.principal.getUser().getLastName()}
                    <span class="caret"></span>
                    </a>

                    <ul class="dropdown-menu">
                        <li><a
                                href="<c:url value="${contextPath}/user/changePassword"/>"><spring:message
                                code="user.password"/></a></li>
                        <li><a href="<c:url value="${contextPath}/signout"/>"><spring:message
                                code="header.logout"/></a></li>
                    </ul>
                </c:when>
                <c:otherwise>
                    <li class="login visible-xs"><a href="${contextPath}/signin"><spring:message
                            code="header.login"/></a></li>
                </c:otherwise>
            </c:choose>

            <!--  Language -->
            <li class="nav-lang"><a href="/?lang=pt_PT"
                                    class="${pageContext.response.locale.language.equalsIgnoreCase('pt') ? 'active' : ''}">PT</a>
                <a href="/?lang=en"
                   class="${pageContext.response.locale.language.equalsIgnoreCase('en') ? 'active' : ''}">EN</a>
                <a href="/?lang=es"
                   class="${pageContext.response.locale.language.equalsIgnoreCase('es') ? 'active' : ''}">ES</a>
            </li>
        </ul>

    </div>
</div>