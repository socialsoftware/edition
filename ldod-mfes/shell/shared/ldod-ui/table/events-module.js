/** @format */

const { eventBus } = globalThis;

export const loadingPublisher = payload => eventBus?.publish('ldod:loading', payload);

// TODO

// register ldod:table-increased and ldod:table-searched
// define json data schema
// implement publishers
// check subscribers
