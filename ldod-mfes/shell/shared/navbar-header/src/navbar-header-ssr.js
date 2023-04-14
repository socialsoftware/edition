/** @format */

import { headerDataSchemaValidator } from './data-schema-validator-ssr';
import { createDropdownRawHTML } from './li-dropdown-html';
import { parse } from 'node-html-parser';

export default (dom, data, lang = 'en') => {
	if (!headerDataSchemaValidator(data)) return '';
	const container = dom.querySelector('div#navbar-nav ul');
	if (!container) return;
	const reference = container.querySelector("li[key='admin'][is='drop-down']");
	let drops = container.querySelectorAll("li[is='drop-down']:not([key='admin'])");
	drops.forEach(drop => drop.remove());
	drops = [
		...drops.filter(element => element.getAttribute('key') !== data.name),
		createDropdownElement(data, lang).firstChild,
	];
	drops = sortArrayOfHTMLElementsByKey(drops);
	reference.insertAdjacentHTML('beforebegin', reduceElementsToRawHTML(drops));
};

/**
 *
 * @param {[]} array
 */
function sortArrayOfHTMLElementsByKey(array) {
	return array.sort((a, b) => a.getAttribute('key').localeCompare(b.getAttribute('key')));
}

function reduceElementsToRawHTML(elements) {
	return elements.reduce((html, element) => html + element.outerHTML, '');
}

function createDropdownElement(data, lang) {
	return parse(
		/*html*/ `<li key="${
			data.name
		}" is="drop-down" language="${lang}" data-headers='${JSON.stringify(
			data
		)}'>${createDropdownRawHTML(data, lang)}</li>`
	);
}
export function cleanUp(dom, key) {
	dom.querySelector(`div#navbar-nav ul li[key="${key}"]`)?.remove();
}
