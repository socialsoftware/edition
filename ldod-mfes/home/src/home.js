/** @format */

let home;

const isBrowserEnv =
	typeof window !== 'undefined' &&
	typeof document !== 'undefined' &&
	typeof navigator !== 'undefined';

if (isBrowserEnv) {
	import('./nav-bar/nav-bar.js');
	import('./home/home-info.js');
}

async function loadHome() {
	if (!home) home = await import('./home/home.js');
	return home;
}

export default {
	path: '/',
	mount: async (lang, ref) => (await loadHome()).mount(lang, ref),
	unMount: async () => (await loadHome()).unMount(),
	preRender: async (dom, lang) =>
		await (await import('./nav-bar/nav-bar-ssr.js')).default(dom, lang),
};
