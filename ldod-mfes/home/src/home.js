/** @format */

let home;

if (typeof window !== 'undefined') {
	import('./navbar/ldod-navbar.js');
	import('./home/home-info.js');
}

export async function registerMFE({ name, data, constants }) {
	if (!name || !data || !constants) return;
	customElements.whenDefined('ldod-navbar').then(navbar => {
		navbar.instance.addHeaderMenu(name, data, constants);
	});
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
