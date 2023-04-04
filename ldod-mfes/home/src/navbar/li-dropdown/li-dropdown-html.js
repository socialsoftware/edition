/** @format */
import constants from '../constants';
export default root => {
	const constantsMap = root.constants || constants;
	return /*html*/ `
        <a
            class="nav-link dropdown-toggle"
            role="button"
            data-bs-toggle="dropdown"
            aria-expanded="false"
            data-dropdown-key="${root.data.name}"
        >
            ${constantsMap[root.language][root.data.name] || root.data.name}
        </a>
        <ul class="dropdown-menu">
            <div class="dropdown-menu-bg"></div>
            ${root.data.pages.map(page => getDropItems(page, root.language, constantsMap)).join('')}
        </ul>
    `;
};

function getDropItems({ id, route, link }, lang, constants) {
	return /*html*/ `<li>${
		id === 'divider' ? divider() : dropdownItem(id, route, link, lang, constants)
	}</li>`;
}

function dropdownItem(id, route, link, lang, constants) {
	const a = /* html*/ `
    <a class="dropdown-item" ${route ? `to="${route}"` : 'to'} ${
		link ? `href="${link}" target="_blank"` : 'is="nav-to"'
	} data-dropdown-key="${id}" >${constants[lang][id] || id}</a>
    `;
	return a;
}

function divider() {
	return /* html*/ `<hr class="dropdown-divider">`;
}
