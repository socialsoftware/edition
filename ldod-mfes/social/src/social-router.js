/** @format */

import '@core';
import style from './style.css?inline';

export const isDev = () => import.meta.env.DEV;
const SOCIAL_SELECTOR = 'div#social-container';

const routes = {
	'/twitter-citations': async () => await import('./pages/citations/citations.jsx'),
	'/manage-tweets': async () => await import('./pages/manage-tweets/manage-tweets.jsx'),
};

export const mount = (lang, ref) => {
	document.querySelector(ref).appendChild(SocialRouter({ language: lang }));
};

export const unMount = () => {
	document.querySelector(SOCIAL_SELECTOR)?.remove();
};

export const SocialRouter = ({ language }) => {
	const div = document.createElement('div');
	div.id = 'social-container';
	div.innerHTML = /*html*/ `
		<style>
			${style}
		</style>
		<div class="container">
			<ldod-router
				id="social-router"
				base="${isDev() ? '' : import.meta.env.VITE_BASE}"
				route="/social"
				language="${language}"
			></ldod-router>
		</div>
		`;
	div.querySelector('ldod-router').routes = routes;
	return div;
};
