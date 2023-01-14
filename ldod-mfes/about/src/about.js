const references = (await import('./references.js')).default;
let about;

async function loadAbout() {
	if (!about) about = await import('./about-router.jsx');
	return about;
}

export function loadConductCode() {
	import('./pages/conduct/conduct.jsx');
}

export default {
	path: '/about',
	references,
	mount: async (lang, ref) => (await loadAbout()).mount(lang, ref),
	unMount: async () => (await loadAbout()).unMount(),
};
