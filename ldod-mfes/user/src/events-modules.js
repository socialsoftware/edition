/** @format */

import { ldodEventPublisher, ldodEventSubscriber, ldodEventBus } from '@core';

export const errorPublisher = message => ldodEventPublisher('error', message);
export const messagePublisher = message => ldodEventPublisher('message', message);

export const tokenPublisher = token => ldodEventPublisher('token', token);
export const logoutPublisher = () => ldodEventPublisher('logout');
export const loginPublisher = user => ldodEventPublisher('login', user);

export const loginSubscriber = handler => ldodEventSubscriber('login', handler);

export const logoutSubscriber = handler => ldodEventSubscriber('logout', handler);

export { ldodEventBus };
