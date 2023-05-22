/** @format */

let social;

const isBrowserEnv =
	typeof window !== 'undefined' &&
	typeof document !== 'undefined' &&
	typeof navigator !== 'undefined';

if (isBrowserEnv) {
	import('./menu-links.js');
}

const loadSocial = async () => {
	if (!social) social = await import('./social-router.js');
	return social;
};

export default {
	path: '/social',
	mount: async (lang, ref) => (await loadSocial()).mount(lang, ref),
	unMount: async () => (await loadSocial()).unMount(),
};
