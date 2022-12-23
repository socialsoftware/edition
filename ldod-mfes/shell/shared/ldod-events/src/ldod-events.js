import 'vendor/@trutoo/event-bus_2.2.0/dist/index.umd.min.js';
import userSchema from './user.schema.json';
import veSchema from './ve.schema.json';
import urlSchema from './url.schema.json';

export const ldodEventBus = eventBus;

ldodEventBus.register('ldod:url-changed', urlSchema);
ldodEventBus.register('ldod:loading', { type: 'boolean' });
ldodEventBus.register('ldod:language', { type: 'string', required: true });
ldodEventBus.register('ldod:error', { type: 'string', required: true });
ldodEventBus.register('ldod:message', { type: 'string', required: true });
ldodEventBus.register('ldod:token', { type: 'string' });
ldodEventBus.register('ldod:logout');
ldodEventBus.register('ldod:login', userSchema);
ldodEventBus.register('ldod:selected-ve', veSchema);

export function ldodEventPublisher(name, payload) {
  ldodEventBus.publish(`ldod:${name}`, payload);
}

export function ldodEventSubscriber(name, handler) {
  return ldodEventBus.subscribe(`ldod:${name}`, handler).unsubscribe;
}
