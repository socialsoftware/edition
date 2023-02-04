export let selectedInters = ['LdoD-Arquivo'];

export let errorPublisher, selectedVePublisher, messagePublisher, loadingPublisher;

function selectedVeHandler({ payload }) {
	selectedInters = payload.selected
		? [...selectedInters, payload.name]
		: selectedInters.filter(ed => ed !== payload.name);
}

if (typeof window !== 'undefined') {
	await import('@shared/ldod-events.js').then(module => {
		errorPublisher = error => module.ldodEventPublisher('error', error);
		selectedVePublisher = ve => module.ldodEventPublisher('selected-ve', ve);
		messagePublisher = info => module.ldodEventPublisher('message', info);
		loadingPublisher = bool => module.ldodEventPublisher('loading', bool);
		module.ldodEventSubscriber('selected-ve', selectedVeHandler);
	});
}
