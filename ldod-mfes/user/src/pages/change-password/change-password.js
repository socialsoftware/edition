import './ldod-change-password';

const mount = (lang, ref) => {
	const changePassword = document.createElement('ldod-change-password');
	changePassword.setAttribute('language', lang);
	document.querySelector(ref).appendChild(changePassword);
};
const unMount = () => document.querySelector('ldod-change-password')?.remove();

const path = '/change-password';

export { mount, unMount, path };
