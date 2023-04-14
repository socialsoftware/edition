/** @format */

const textReferences = (await import('./references')).default;
let text;

const loadText = async () => {
	if (!text) text = await import('./text-router.jsx');
	return text;
};

if (typeof window !== 'undefined') import('./events-module');

export default {
	path: '/text',
	references: textReferences,
	preRender: async (dom, lang) => (await import('./headerSSR.js')).default(dom, lang),
	cleanUp: async dom => (await import('./headerSSR.js')).cleanUpHeaders(dom),
	mount: async (lang, ref) => (await loadText()).mount(lang, ref),
	unMount: async () => (await loadText()).unMount(),
};
