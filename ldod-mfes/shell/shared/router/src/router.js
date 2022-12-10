import { ldodEventPublisher } from './events-module.js';
import './ldod-router.js';
import './nav-to.js';

export const navigateToOld = (path, emiter = window, state) => {
  emiter.dispatchEvent(
    new CustomEvent('ldod-url-changed', {
      detail: { path, state },
      composed: true,
      bubbles: true,
    })
  );
};

export const navigateTo = (path, state) => {
  ldodEventPublisher('url-changed', { path, state });
};
