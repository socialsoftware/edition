/** @format */

import '@core';
import style from './style.css?inline';
import { isDev, getContainer } from './utils.js';

const routes = {
	'/archive': async () => await import(`./pages/archive/archive.jsx`),
	'/acknowledgements': async () => await import(`./pages/ack/ack.jsx`),
	'/articles': async () => await import(`./pages/articles/articles.jsx`),
	'/book': async () => await import(`./pages/book/book.jsx`),
	'/conduct': async () => await import(`./pages/conduct/conduct.js`),
	'/copyright': async () => await import(`./pages/copyright/copyright.jsx`),
	'/encoding': async () => await import('./pages/encoding/encoding.jsx'),
	'/faq': async () => await import('./pages/faq/faq.jsx'),
	'/privacy': async () => await import('./pages/privacy/privacy.jsx'),
	'/team': async () => await import('./pages/team/team.jsx'),
	'/tutorials': async () => await import('./pages/tutorials/tutorials.jsx'),
	'/videos': async () => await import('./pages/videos/videos.jsx'),
	'/contact': async () => await import('./pages/contact/contact.jsx'),
};

export const mount = async (lang, ref) => {
	document.querySelector(ref).appendChild(<AboutRouter language={lang} />);
};

export const unMount = async () => {
	getContainer()?.remove();
};

export const AboutRouter = ({ language }) => {
	return (
		<div id="about-container">
			<style>{style}</style>
			<ldod-router
				id="about-router"
				base={isDev() ? '' : import.meta.env.VITE_BASE}
				route="/about"
				routes={routes}
				language={language}></ldod-router>
			<home-info language={language} class="language" hidden></home-info>
		</div>
	);
};
