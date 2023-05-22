/** @format */

import references from './references.js';
let text;

const loadText = async () => {
	if (!text) text = await import('./text-router.jsx');
	return text;
};
const isBrowserEnv = () =>
	typeof window !== 'undefined' &&
	typeof document !== 'undefined' &&
	typeof navigator !== 'undefined';

if (isBrowserEnv()) {
	import('./events-module');
}

export default {
	path: '/text',
	references,
	preRender: async (dom, lang) => (await import('./headerSSR.js')).default(dom, lang),
	mount: async (lang, ref) => (await loadText()).mount(lang, ref),
	unMount: async () => (await loadText()).unMount(),
};
