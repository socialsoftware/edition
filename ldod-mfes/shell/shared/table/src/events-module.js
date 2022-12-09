import { ldodEventBus } from 'shared/ldod-events.js';

export const loadingPublisher = (payload) =>
  ldodEventBus.publish('ldod:loading', payload);

// TODO

// register ldod:table-increased and ldod:table-searched
// define json data schema
// implement publishers
// check subscribers
