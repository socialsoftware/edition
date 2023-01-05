const references = (await import('./references.js')).default;
let about;

const loadAbout = async () => {
	if (!about) about = await import('./about-router.jsx');
	return about;
};

export default {
	path: '/about',
	references,
	mount: async (lang, ref) => (await loadAbout()).mount(lang, ref),
	unMount: async () => (await loadAbout()).unMount(),
};
