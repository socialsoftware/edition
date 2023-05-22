/** @format */

import '@core';
import style from '@src/style.css?inline';

import { isDev } from './utils.js';

const routes = {
	'/signin': async () => await import('./pages/signin/signin.js'),
	'/signout': async () => await import('./pages/signout/signout.js'),
	'/signup': async () => await import('./pages/signup/signup.js'),
	'/change-password': async () => await import('./pages/change-password/change-password.js'),
	'/sign-up-authorization': async () => await import('./pages/authorization.js'),
	'/sign-up-confirmation': async () => await import('./pages/confirmation.js'),
	'/manage-users': async () => await import('./pages/manage-users/manage-users.js'),
};
const userMfeSelector = 'div#user-mfe';

export const mount = (lang, ref) => {
	const refElement = document.querySelector(ref);
	refElement.innerHTML = /*html*/ `
		<div id="user-mfe" class="container text-center">
			<style>
				${style}
			</style>
		</div>
	`;
	refElement.firstElementChild.appendChild(userRouter(lang));
};

export const unMount = () => document.querySelector(userMfeSelector)?.remove();

const userRouter = language => {
	const router = document.createElement('ldod-router');
	router.id = 'user-router';
	router.setAttribute('base', isDev() ? '' : import.meta.env.VITE_BASE);
	router.setAttribute('route', '/user');
	router.setAttribute('language', language);
	router.routes = routes;
	return router;
};
