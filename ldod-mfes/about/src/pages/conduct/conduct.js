/** @format */

import './ldod-conduct.js';

let conductTemplate;

function getConductTemplate(lang) {
	if (!conductTemplate) {
		const template = document.createElement('template');
		template.innerHTML = /*html*/ `<ldod-conduct  title></ldod-conduct>`;
		conductTemplate = template.content.firstElementChild;
	}
	const conduct = conductTemplate.cloneNode(true);
	conduct.setAttribute('language', lang);
	return conduct;
}

const mount = (lang, ref) => {
	document.querySelector(ref).appendChild(getConductTemplate(lang));
};
const unMount = () => document.querySelector('ldod-conduct')?.remove();

const path = '/conduct';
export { mount, unMount, path };
