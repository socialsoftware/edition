/** @format */

import '@core';

export const isDev = () => import.meta.env.DEV;

const SEARCH_SELECTOR = 'div#searchContainer';

const style = /*css*/ `*{ -webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;}.container {padding-right: 15px;  padding-left: 15px;margin-right: auto; margin-left: auto;  font-family: Helvetica Neue, Helvetica, Arial, sans-serif;  font-size: 14px;  line-height: 1.42857143;  color: #333;  background-color: #fff;}@media (min-width: 768px) {.container {width: 750px;}}@media (min-width: 992px) {.container {width: 970px; }}@media (min-width: 1200px) { .container {width: 1170px;}}`;

const routes = {
	'/simple': async () => await import('./pages/search-simple/search-simple'),
	'/advanced': async () => await import('./pages/search-advanced/search-advanced'),
};

export const mount = async (lang, ref) => {
	document.querySelector(ref).appendChild(<SearchRouter language={lang} />);
};

export const unMount = async () => {
	document.querySelector(SEARCH_SELECTOR)?.remove();
};

export const SearchRouter = ({ language }) => {
	return (
		<div id="searchContainer">
			<style>{style}</style>
			<div class="container" style={{ marginBottom: '40px' }}>
				<ldod-router
					id="search-router"
					base={isDev() ? '' : import.meta.env.VITE_BASE}
					route="/search"
					routes={routes}
					language={language}></ldod-router>
			</div>
		</div>
	);
};
