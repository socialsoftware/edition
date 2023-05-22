/** @format */

let mfe;
const isBrowserEnv = () =>
	typeof window !== 'undefined' &&
	typeof document !== 'undefined' &&
	typeof navigator !== 'undefined';

if (isBrowserEnv()) {
	// dynamic imports to run the code to be executed on start
}

async function loadMfe() {
	if (!mfe) mfe = await import('./root');
	return mfe;
}

export default {
	path: `/${import.meta.env.VITE_MFE_NAME}`,
	mount: async (lang, ref) => (await loadMfe()).mount(lang, ref),
	unMount: async () => (await loadMfe()).unMount(),
	preRender: async (dom, lang) => (await import('./pre-render')).default(dom, lang),
	references: {
		hello: () => `/${import.meta.env.VITE_MFE_NAME}`,
	},
};
