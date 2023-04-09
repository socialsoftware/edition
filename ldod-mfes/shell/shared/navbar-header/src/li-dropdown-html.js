/** @format */

export function getDropDownHTML({ name, data, constants }, lang = 'en') {
	return /*html*/ `
        <a
            class="nav-link dropdown-toggle"
            role="button"
            data-bs-toggle="dropdown"
            aria-expanded="false"
            data-dropdown-key="${name}"
        >
            ${constants[lang][name] || name}
        </a>
        <ul class="dropdown-menu">
            <div class="dropdown-menu-bg"></div>
            ${data.pages.map(page => addLiItem(page, lang, constants)).join('')}
            <div id="external-links"></div>
        </ul>
    `;
}

export function addLiItem(page, lang, constants) {
	const { id, route, link } = page;
	return /*html*/ `<li>${
		id === 'divider' ? divider() : dropdownItem(id, route, link, lang, constants)
	}</li>`;
}

function dropdownItem(id, route, link, lang, constants) {
	const a = /* html*/ `
    <a class="dropdown-item" ${route ? `to="${route}"` : 'to'} ${
		link ? `href="${link}" target="_blank"` : 'is="nav-to-new"'
	} data-dropdown-key="${id}" >${constants[lang][id] || id}</a>
    `;
	return a;
}

function divider() {
	return /* html*/ `<hr class="dropdown-divider">`;
}
