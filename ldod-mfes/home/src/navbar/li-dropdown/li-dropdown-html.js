/** @format */
import constants from '../constants';
export default root => {
	return /*html*/ `
        <a
            class="nav-link dropdown-toggle"
            role="button"
            data-bs-toggle="dropdown"
            aria-expanded="false"
            data-navbar-key="${root.data.name}"
        >
            ${constants[root.language][root.data.name]}
        </a>
        <ul class="dropdown-menu">
            <div class="dropdown-menu-bg"></div>
            ${root.data.pages.map(page => getDropItems(page, root.language)).join('')}
        </ul>
    `;
};

export function getDropItems({ id, route, link }, lang) {
	return /*html*/ `<li>${
		id === 'divider' ? divider() : dropdownItem(id, route, link, lang)
	}</li>`;
}

function dropdownItem(id, route, link, lang) {
	return /* html*/ `<a is="nav-to" class="dropdown-item" ${
		route ? `to="${route}"` : `href="${link}" target="_blank"`
	} data-navbar-key="${id}" >${constants[lang][id]}</a>`;
}

function divider() {
	return /* html*/ `<hr class="dropdown-divider">`;
}
