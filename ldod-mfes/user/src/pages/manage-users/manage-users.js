/** @format */

import './ldod-manage-users';
const mount = async (lang, ref) => {
	const manageUsers = document.createElement('ldod-manage-users');
	manageUsers.setAttribute('language', lang);
	document.querySelector(ref).appendChild(manageUsers);
};
const unMount = () => document.querySelector('manage-users')?.remove();

const path = '/manage-users';

export { mount, unMount, path };
