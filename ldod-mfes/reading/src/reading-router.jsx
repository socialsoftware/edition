/** @format */

import '@core';
import { index } from './pages/index/index';
import './store';
import style from './style.css?inline';

const READING_SELECTOR = 'div#reading-container';

const routes = {
	'/fragment/:xmlId/inter/:urlId': async () =>
		await import('./pages/reading-edition/reading-edition'),
};

const isDev = () => import.meta.env.DEV;

export const mount = (lang, ref) => {
	document.querySelector(ref).appendChild(<ReadingRouter language={lang} />);
};

export const unMount = () => {
	document.querySelector(READING_SELECTOR)?.remove();
};

export const ReadingRouter = ({ language }) => {
	return (
		<div id="reading-container">
			<style>{style}</style>
			<div class="container">
				<ldod-router
					id="reading-router"
					base={isDev() ? '' : import.meta.env.VITE_BASE}
					route="/reading"
					routes={routes}
					index={index}
					language={language}></ldod-router>
			</div>
		</div>
	);
};
