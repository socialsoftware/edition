/** @format */

import '@shared/router.js';
import style from '@src/style.css?inline';

import { isDev } from './utils.js';

const routes = {
	'/signin': async () => await import('./pages/signin/signin.js'),
	'/signup': async () => await import('./pages/signup/signup.js'),
	'/change-password': async () => await import('./pages/change-password/change-password.js'),
	'/sign-up-authorization': async () => await import('./pages/authorization.js'),
	'/sign-up-confirmation': async () => await import('./pages/confirmation.js'),
	'/manage-users': async () => await import('./pages/manage-users/manage-users.js'),
};
const userMfeSelector = 'div#user-mfe';

export const mount = (lang, ref) => {
	document.querySelector(ref).appendChild(UserRouter(lang));
};

export const unMount = () => {
	document.querySelector(userMfeSelector)?.remove();
};

const UserRouter = language => {
	const template = document.createElement('template');
	template.innerHTML = /*html*/ `
		<div id="user-mfe" class="container text-center">
			<style>${style}</style>

			<ldod-router
				id="user-router"
				base="${isDev() ? '' : import.meta.env.VITE_BASE}"
				route="/user"
				language=${language}>
			</ldod-router>
		</div>
	`;
	const router = template.content.cloneNode(true);
	router.querySelector('ldod-router').routes = routes;
	return router;
};
