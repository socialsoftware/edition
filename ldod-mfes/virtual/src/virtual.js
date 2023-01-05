const virtualReferences = (await import('./references')).virtualReferences;
export { virtualReferences };
import './event-module';

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
	bootstrap: () => {
		import('./fragment/virtual-navigation');
		import('./fragment/virtual-transcription');
	},
};
