import '@shared/router.js';
import { isDev } from './utils.js';
import style from './style.css?inline';

const USER_MFE_SELECTOR = 'div#user-mfe';
const routes = {
	'/signin': async () => await import('./pages/signin/signin.js'),
	'/signup': async () => await import('./pages/signup/signup.js'),
	/*'/change-password': async () => await import('./pages/change-pw/change-password.jsx'),
	'/sign-up-authorization': async () => await import('./pages/authorization.js'),
	'/sign-up-confirmation': async () => await import('./pages/confirmation.js'),
	'/manage-users': async () => await import('./pages/manage-users/manage-users.jsx'),*/
};

export function mount(lang, ref) {
	document.querySelector(ref).appendChild(router(lang));
}

export function unMount() {
	document.querySelector(USER_MFE_SELECTOR)?.remove();
}

function router(language) {
	const template = document.createElement('template');
	template.innerHTML = /*html*/ `
		<style>${style}</style>
		<div id="user-mfe" class="container text-center">
			<ldod-router
				id="user-router"
				base="${isDev() ? '' : import.meta.env.VITE_BASE}"
				route="/user"
				language="${language}">
			</ldod-router>		
		</div>
`;
	template.content.querySelector('ldod-router').routes = routes;
	return template.content;
}
