/** @format */
/// <reference path="typedef.js" />

import { ldodEventPublisher, ldodEventSubscriber, ldodEventBus } from '@core';

/**
 * @param {string} message
 */
export const errorPublisher = message => ldodEventPublisher('error', message);

/**
 * @param {string} message
 */
export const messagePublisher = message => ldodEventPublisher('message', message);

/**
 * @param {string} token
 */
export const tokenPublisher = token => ldodEventPublisher('token', token);
export const logoutPublisher = () => ldodEventPublisher('logout');

/**
 * @param {User} user
 */
export const loginPublisher = user => ldodEventPublisher('login', user);

export const loginSubscriber = handler => ldodEventSubscriber('login', handler);

export const logoutSubscriber = handler => ldodEventSubscriber('logout', handler);

export { ldodEventBus };
