/** @format */
/// <reference path="../models/edition-link.js" />

import { ldodEventPublisher, ldodEventBus, ldodEventSubscriber } from '@core';

/**
 * @param {string} info
 */
export const errorPublisher = error => ldodEventPublisher('error', error);

/**
 * @param {string} info
 */
export const messagePublisher = info => ldodEventPublisher('message', info);

/**
 * @param {boolean} bool
 */
export const loadingPublisher = bool => ldodEventPublisher('loading', bool);

/**
 * @param {EditionLink[]} links
 */
export const editionsLinksPub = links => ldodEventBus.publish('text:editions-menu-links', links);

export { ldodEventBus, ldodEventSubscriber };
