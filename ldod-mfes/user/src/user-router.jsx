import '@shared/router.js';
import style from '@src/resources/style.css?inline';
import switchCSS from './resources/switch.css?inline';
import buttons from '@src/resources/buttons.css?inline';
import { isDev } from './utils.js';

const routes = {
	'/signin': async () => await import('./pages/signin/signin.jsx'),
	'/signup': async () => await import('./pages/signup/signup.jsx'),
	'/change-password': async () => await import('./pages/change-pw/change-password.jsx'),
	'/sign-up-authorization': async () => await import('./pages/authorization.js'),
	'/sign-up-confirmation': async () => await import('./pages/confirmation.js'),
	'/manage-users': async () => await import('./pages/manage-users/manage-users.jsx'),
};
const userMfeSelector = 'div#user-mfe';

export const mount = (lang, ref) => {
	document.querySelector(ref).appendChild(<UserRouter language={lang} />);
};

export const unMount = () => {
	document.querySelector(userMfeSelector)?.remove();
};

const UserRouter = ({ language }) => {
	return (
		<>
			<div id="user-mfe" class="container text-center">
				<style>{style}</style>
				<style>{buttons}</style>
				<style>{switchCSS}</style>
				<ldod-router
					id="user-router"
					base={isDev() ? '' : import.meta.env.VITE_BASE}
					route="/user"
					routes={routes}
					language={language}></ldod-router>
			</div>
		</>
	);
};
