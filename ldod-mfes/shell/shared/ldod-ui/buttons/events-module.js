/** @format */

const ldodEventBus = globalThis.eventBus;
export const uploadEvent = (ref, payload) =>
	new CustomEvent('ldod:file-uploaded', {
		detail: { ref, payload },
		bubbles: true,
		composed: true,
	});

export const errorPublisher = message => ldodEventBus?.publish('ldod:error', message);
