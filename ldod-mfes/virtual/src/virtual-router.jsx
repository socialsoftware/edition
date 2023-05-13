/** @format */

import '@core';
import style from './style.css?inline';
import { isDev } from './utils';

const VIRTUAL_SELECTOR = 'div#virutalContainer';

const routes = {
	'/virtual-editions': async () =>
		await import('./pages/virtual-editions/virtual-editions-router'),
	'/manage-virtual-editions': async () => await import('./pages/manage-ve/manage-ve'),
	'/edition': async () => await import('./pages/edition/edition-router'),
	'/classification-games': async () => await import('./pages/class-games/class-games.js'),
};

export const mount = (lang, ref) => {
	document.querySelector(ref).appendChild(<VirtualRouter language={lang} />);
};

export const unMount = () => {
	document.querySelector(VIRTUAL_SELECTOR)?.remove();
};

export const VirtualRouter = ({ language }) => {
	return (
		<div id="virutalContainer">
			<style>{style}</style>
			<div class="container">
				<ldod-router
					id="virtual-router"
					base={isDev() ? '' : import.meta.env.VITE_BASE}
					route="/virtual"
					routes={routes}
					language={language}></ldod-router>
			</div>
		</div>
	);
};
