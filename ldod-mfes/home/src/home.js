/** @format */

let home;

if (typeof window === 'object') import('./navbar/ldod-navbar.js');

async function loadHome() {
	if (!home) home = await import('./home/home.js');
	return home;
}

export default {
	path: '/',
	mount: async (lang, ref) => (await loadHome()).mount(lang, ref),
	unMount: async () => (await loadHome()).unMount(),
};
