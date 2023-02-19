/** @format */

let home;

if (typeof window !== 'undefined') {
	import('./navbar/ldod-navbar.js');
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
};
