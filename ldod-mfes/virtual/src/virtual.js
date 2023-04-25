/** @format */

const virtualReferences = (await import('./references')).virtualReferences;
export { virtualReferences };

if (typeof window !== 'undefined') import('./event-module');

let virtual;

const loadVirtual = async () => {
	virtual = await import('./virtual-router.jsx');
};

export default {
	path: '/virtual',
	references: virtualReferences,
	mount: async (lang, ref) => {
		if (!virtual) await loadVirtual();
		await virtual.mount(lang, ref);
	},
	unMount: async () => {
		if (!virtual) await loadVirtual();
		await virtual.unMount();
	},
	preRender: async (dom, lang) => (await import('./headerSSR.js')).default(dom, lang),
};

export function loadFragment() {
	import('./fragment/virtual-frag-nav.js');
	import('./fragment/virtual-transcription');
}
