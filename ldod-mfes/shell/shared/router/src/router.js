import { ldodEventPublisher } from './events-module.js';
import './ldod-router.js';
import './nav-to.js';

export const navigateTo = (path, state) => {
  ldodEventPublisher('url-changed', { path, state });
};
