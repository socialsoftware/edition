/** @format */

import { ldodEventPublisher } from './events-module.js';
import './ldod-router.js';
import './nav-to.js';
import './nav-to-new.js';

export const navigateTo = (path, state) => {
	ldodEventPublisher('url-changed', { path, state });
};
