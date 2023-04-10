/** @format */

const mount = async (lang, ref) => {
	import('./ldod-class-games');
	document.querySelector(ref).innerHTML = /*html*/ `
    <ldod-class-games language="${lang}"></ldod-class-games>
    `;
};

const unMount = () => document.querySelector('ldod-class-games')?.remove();

const path = '/classification-games';

export { mount, unMount, path };
