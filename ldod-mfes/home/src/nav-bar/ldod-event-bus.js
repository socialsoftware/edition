/** @format */
const warn = () => console.warn('eventBus not found');
export const ldodEventBus = globalThis.eventBus ?? {
	publisher: warn,
	subscribe: warn,
	register: warn,
};
