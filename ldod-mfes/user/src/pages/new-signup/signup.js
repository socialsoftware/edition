import './ldod-signup.js';

const mount = (lang, ref) => {
	const template = document.createElement('template');
	template.innerHTML = /*html*/ `<ldod-signup language="${lang}"></ldod-signup>`;
	document.querySelector(ref).appendChild(template.content.cloneNode(true));
};
const unMount = () => document.querySelector('ldod-signup')?.remove();

const path = '/signup';

export { mount, unMount, path };
