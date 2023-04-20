/** @format */

import './ldod-home.js';

export const mount = (language, ref) => {
	const refElement = document.querySelector(ref);
	if (refElement.querySelector('ldod-home')) return;
	const template = document.createElement('template');
	template.innerHTML = /*html*/ `<ldod-home language="${language}"></ldod-home>`;
	refElement.appendChild(template.content.cloneNode(true));
};

export const unMount = () => {
	const home = document.querySelector('ldod-home');
	home?.remove();
};
