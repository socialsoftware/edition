import './ldod-signup';

const mount = (lang, ref) => {
	const signin = document.createElement('ldod-signup');
	signin.setAttribute('language', lang);
	document.querySelector(ref).appendChild(signin);
};
const unMount = () => document.querySelector('ldod-signup')?.remove();

const path = '/signup';

export { mount, unMount, path };
