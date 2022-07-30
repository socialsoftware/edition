import './LdodRouter.js';
import './NavTo.js';

export const navigateTo = (path, emiter = window, state) => {
  emiter.dispatchEvent(
    new CustomEvent('ldod-url-changed', {
      detail: { path, state },
      composed: true,
      bubbles: true,
    })
  );
};
