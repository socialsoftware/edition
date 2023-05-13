/** @format */

const eventBus = globalThis.eventBus;
export function ldodEventPublisher(name, payload) {
	eventBus?.publish(`ldod:${name}`, payload);
}

export function ldodEventSubscriber(name, handler, replay = false) {
	return eventBus?.subscribe(`ldod:${name}`, replay, handler).unsubscribe;
}
