/** @format */
import constants from './constants';
import headersMenus from './headers-menus';

export default language => {
	return /*html*/ `
<nav class="navbar navbar-expand-md fixed-top">
    <div class="container-md" style="display: flex;width: 100%">
        <a is="nav-to" to="/" class="navbar-brand" data-navbar-key="header_title">${
			constants[language]['header_title']
		}</a>
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
    <div class="container-md" style="display: flex;width: 100%">
        <div class="collapse navbar-collapse nav" id="navbar-nav">
            <ul class="navbar-nav">
                    ${Object.keys(headersMenus)
						.map(
							key =>
								/*html*/ `<li  language="${language}" is="drop-down" key="${key}" class="nav-item dropdown"></li>`
						)
						.join('')}
               <div id="user-md">
                <li is="user-component" language="${language}" key="user"></li>
               </div>
                <li language="${language}" is="lang-menu" class="nav-item dropdown nav-lang">
                    <a id="pt" class="nav-link" href="#">PT</a>
                    <a id="en" class="nav-link active" href="#">EN</a>
                    <a id="es" class="nav-link" href="#">ES</a>
                </li>
            </ul>
        </div>
    </div>
    <div class="navbar-border-bottom-sm"></div>
</nav>`;
};
