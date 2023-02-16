/** @format */

import './ldod-home.js';

export const mount = (language, ref) => {
	const template = document.createElement('template');
	template.innerHTML = /*html*/ `<ldod-home language="${language}"></ldod-home>`;
	document.querySelector(ref).appendChild(template.content.cloneNode(true));
};

export const unMount = () => {
	const home = document.querySelector('ldod-home');
	home?.remove();
};
