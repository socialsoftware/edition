/** @format */

const references = (await import('./references.js')).default;
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
	references,
	preRender: async (dom, lang) => (await import('./headerSSR.js')).default(dom, lang),
	cleanUp: async dom => (await import('./headerSSR.js')).cleanUpHeader(dom),
	mount: async (lang, ref) => (await loadAbout()).mount(lang, ref),
	unMount: async () => (await loadAbout()).unMount(),
};
