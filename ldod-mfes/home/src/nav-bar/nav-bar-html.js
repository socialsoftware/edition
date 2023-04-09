/** @format */
import style from './style/style.css?inline';
import dropdown from 'shared/dropdown/navbar-header-ssr';

export default language => {
	return /*html*/ `

        <nav class="navbar navbar-expand-md fixed-top">
            <div class="container-md" style="display: flex;width: 100%;min-height: 62px">
                <a is="nav-to-new" to="/" class="navbar-brand" data-navbar-key="header_title">
                    LdoD Archive
                </a>
                <button
                    class="navbar-toggler"
                    type="button"
                    data-bs-toggle="collapse"
                    data-bs-target="#navbarNav"
                    aria-controls="navbar-nav"
                    aria-expanded="false"
                    aria-label="Toggle navigation"
                >
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <div class="navbar-collapse user-lg">
                    <ul class="navbar-nav ml-auto">
                        <div id="user-lg">
                            <li is="user-component" language="${language}" key="user"></li>
                        </div>
                    </ul>
                </div>
            </div>
            <div class="navbar-border-bottom"></div>
            <div class="container-md" style="display: flex;width: 100%; padding: 0;">
                <div class="collapse navbar-collapse nav" id="navbar-nav">
                    <ul class="navbar-nav">
                        ${dropdown({
							name: 'admin',
							data: { name: 'admin', pages: [] },
							constants: { pt: {}, en: {}, es: {} },
						})}
                        <div id="user-md">
                            <li is="user-component" language="${language}" key="user"></li>
                        </div>
                        <li is="lang-drop" class="nav-item dropdown nav-drop" language="${language}">
                            <a role="button" id="pt" class="nav-link">PT</a>
                            <a role="button" id="en" class="nav-link active">EN</a>
                            <a role="button" id="es" class="nav-link">ES</a>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="navbar-border-bottom-sm"></div>
        </nav>
        <style>
            ${style}
        </style>
    `;
};
