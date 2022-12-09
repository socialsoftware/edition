import { ldodEventBus } from 'shared/ldod-events.js';
import userSchema from './user.schema.json';
ldodEventBus.register('user:logout');
ldodEventBus.register('user:token', { type: 'string' });
ldodEventBus.register('user:login', userSchema);
export const errorPublisher = (message) => ldodEventBus('ldod:error', message);
export const messagePublisher = (message) =>
  ldodEventBus('ldod:message', message);

export const tokenPublisher = (token) =>
  ldodEventBus.publish('user:token', token);
export const logoutPublisher = () => ldodEventBus.publish('user:logout');
export const loginPublisher = (user) =>
  ldodEventBus.publish('user:login', user);

export { ldodEventBus };
