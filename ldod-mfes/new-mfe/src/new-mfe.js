/** @format */

import references from './references';

let newMFe;
async function loadNewMfe() {
	if (!newMFe) newMFe = await import('./router.js');
	return newMFe;
}

const isBrowserEnv = () =>
	typeof window !== 'undefined' &&
	typeof document !== 'undefined' &&
	typeof navigator !== 'undefined';

if (isBrowserEnv()) {
	import('./navbar-menu-client');
}

export default {
	path: `/${import.meta.env.VITE_MFE_NAME}`,
	mount: async (lang, ref) => (await loadNewMfe()).mount(lang, ref),
	unMount: async () => (await loadNewMfe()).unMount(),
	//preRender: async (dom, lang) => (await import('./pre-render.js')).default(dom, lang),
	references,
};
