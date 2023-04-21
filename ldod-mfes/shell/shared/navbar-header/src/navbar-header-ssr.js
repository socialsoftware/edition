/** @format */

import { headerDataSchemaValidator } from './data-schema-validator-ssr';
import { createDropdownRawHTML } from './li-dropdown-html';
import { parse } from 'node-html-parser';

export default (dom, data, lang = 'en') => {
	const container = dom.querySelector('div#navbar-nav ul');
	if (!headerDataSchemaValidator(data) || !container) return;
	const header = createLiDropdownHTML(data, lang);
	const reference = container.querySelector('div#reference');
	if (data.name === 'admin') {
		reference.insertAdjacentHTML('afterend', header);
	} else {
		reference.insertAdjacentHTML('beforebegin', header);
		processHeaders(container);
	}
};

function processHeaders(container) {
	let drops = container.querySelectorAll("li[is='drop-down']:not([key='admin'])");
	drops.forEach(drop => drop.remove());
	/*drops = [
		...drops.filter(element => element.getAttribute('key') !== headerKey),
		createLiDropdownHTML(data, lang).firstChild,
	];*/
	drops = sortArrayOfHTMLElementsByKey(drops);
	container.insertAdjacentHTML('afterbegin', reduceElementsToRawHTML(drops));
}

function sortArrayOfHTMLElementsByKey(array) {
	return array.sort((a, b) => a.getAttribute('key').localeCompare(b.getAttribute('key')));
}

function reduceElementsToRawHTML(elements) {
	return elements.reduce((html, element) => html + element.outerHTML, '');
}

export function createLiDropdownHTML(data, lang) {
	return /*html*/ `<li key="${
		data.name
	}" is="drop-down" language="${lang}" data-headers='${JSON.stringify(
		data
	)}'>${createDropdownRawHTML(data, lang)}</li>`;
}
