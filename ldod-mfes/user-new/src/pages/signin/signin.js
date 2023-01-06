import './ldod-signin';

const mount = (lang, ref) => {
	const signin = document.createElement('ldod-signin');
	signin.setAttribute('language', lang);
	document.querySelector(ref).appendChild(signin);
};
const unMount = () => document.querySelector('ldod-signin')?.remove();

const path = '/signin';

export { mount, unMount, path };
