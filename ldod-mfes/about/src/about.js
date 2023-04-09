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
	preRender: {
		header: async () => (await import('./headerSSR.js')).default(),
	},
	mount: async (lang, ref) => (await loadAbout()).mount(lang, ref),
	unMount: async () => (await loadAbout()).unMount(),
};
