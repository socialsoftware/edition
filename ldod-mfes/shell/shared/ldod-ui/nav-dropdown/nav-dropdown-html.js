/** @format */

export function createDropdownRawHTML({ name, data, constants }, lang = 'en') {
	const links = data.links.map(link => addLiItem(link, lang, constants)).join('');
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
            <div class="container-links">
            ${links}
            </div>
        </ul>
    `;
}

export function addLiItem(page, lang, constants) {
	const { key, route, link } = page;
	return /*html*/ `<li>${
		key === 'divider' ? divider() : dropdownItem(key, route, link, lang, constants)
	}</li>`;
}

function dropdownItem(key, route, link, lang, constants) {
	const a = /* html*/ `
    <a class="dropdown-item" ${route ? `to="${route}"` : 'to'} ${
		link ? `href="${link}" target="_blank"` : 'is="nav-to"'
	} data-dropdown-key="${key}" >${constants[lang][key] || key}</a>
    `;
	return a;
}

function divider() {
	return /* html*/ `<hr class="dropdown-divider">`;
}
