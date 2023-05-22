/** @format */
let about;

async function loadAbout() {
	if (!about) about = await import('./about-router.jsx');
	return about;
}

export function loadConductCode() {
	import('./pages/conduct/ldod-conduct.js');
}

export default {
	path: '/about',
	preRender: async (dom, lang) => (await import('./navbar-menu-prerender.js')).default(dom, lang),
	mount: async (lang, ref) => (await loadAbout()).mount(lang, ref),
	unMount: async () => (await loadAbout()).unMount(),
};
