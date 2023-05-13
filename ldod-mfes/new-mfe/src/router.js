/** @format */
import '@core';
window.html = string => document.createRange().createContextualFragment(string).firstElementChild;
const routes = {
	'/pure': async () => await import('./pages/new-mfe'),
	'/lit': async () => await import('./pages/new-mfe-lit'),
	'/lit-ts': async () => await import('./pages/new-mfe-lit-ts'),
};

let router;
export function mount(lang, ref) {
	console.log(ref);
	router = html`
		<ldod-router id="new-mfe--router" route="/new-mfe" language="${lang}"></ldod-router>
	`;
	router.setAttribute('base', import.meta.env.DEV ? '' : import.meta.env.VITE_BASE);
	router.routes = routes;
	document.querySelector(ref).appendChild(router);
}

export function unMount() {
	router?.remove();
}
