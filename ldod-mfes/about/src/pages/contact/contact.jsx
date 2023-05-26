/** @format */

import './ldod-contact.jsx';

const mount = (lang, ref) => {
	document.querySelector(ref).appendChild(<ldod-contact language={lang}></ldod-contact>);
};
const unMount = () => document.querySelector('ldod-contact')?.remove();

const path = '/contact';
export { mount, unMount, path };
