/** @format */
import headersMenus from './headers-menus';

export default language => {
	return /*html*/ `
<nav class="navbar navbar-expand-md fixed-top">
    <div class="container-md">
        <a class="navbar-brand" href="#">Arquivo LdoD</a>
        <button
            class="navbar-toggler"
            type="button"
            data-bs-toggle="collapse"
            data-bs-target="#navbarNav"
            aria-controls="navbarNav"
            aria-expanded="false"
            aria-label="Toggle navigation"
        >
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>

        <div class="navbar-collapse user-lg">
            <ul class="navbar-nav ml-auto">
            <div id="user-lg"></div>
            </ul>
        </div>
    </div>
    <div class="navbar-border-bottom"></div>
    <div class="container-md">
        <div class="collapse navbar-collapse nav" id="navbarNav">
            <ul class="navbar-nav">
                    ${Object.entries(headersMenus)
						.map(([key, val]) => getDropdownHtml(key, val))
						.join('')}
               <div id="user-md"></div>
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

function getDropdownHtml(key, { name, pages }) {
	return /*html*/ `
    <li key="${key}" class="nav-item dropdown">
        <a class="nav-link dropdown-toggle" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            ${name}
        </a>
        <ul class="dropdown-menu">
            <div class="dropdown-menu-bg"></div>
            ${pages.map(page => getDropItems(page)).join('')}
        </ul>
    </li>    
    `;
}

function getDropItems({ id, route, link }) {
	return /*html*/ `<li><a class="dropdown-item" ${
		route ? `to="${route}"` : `href="${link}" target="_blank"`
	} >${id}z</a></li>`;
}
