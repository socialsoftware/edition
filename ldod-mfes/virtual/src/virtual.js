/** @format */

import references from './references';
export { references };

const isBrowserEnv =
	typeof window !== 'undefined' &&
	typeof document !== 'undefined' &&
	typeof navigator !== 'undefined';

if (isBrowserEnv) {
	import('./event-module');
}

let virtual;

const loadVirtual = async () => {
	if (!virtual) virtual = await import('./virtual-router.jsx');
	return virtual;
};

export default {
	path: '/virtual',
	references,
	mount: async (lang, ref) => (await loadVirtual()).mount(lang, ref),
	unMount: async () => (await loadVirtual()).unMount(),
	preRender: async (dom, lang) => (await import('./headerSSR.js')).default(dom, lang),
};

export function loadFragment() {
	import('./fragment/virtual-frag-nav.js');
	import('./fragment/virtual-transcription');
}
