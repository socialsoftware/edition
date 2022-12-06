export { sleep } from 'shared/utils.js';
export { loadingEvent } from "shared/ldod-events.js";

export const PATH_REGEX = /^\/[^/]*/;
export const isSlash = (path) => path === '/';

export const addSlashes = (path) => addStartSlash(addEndSlash(path));

export const addStartSlash = (path) => {
  if (!path || !path.trim()) return;
  return path.startsWith('/') ? path : `/${path}`;
};

export const addEndSlash = (path) => {
  if (!path || !path.trim()) return;
  return path.endsWith('/') ? path : `${path}/`;
};

export const removeEndSlash = (path) => {
  if (!path || !path.trim()) return;
  return isSlash(path) || !path.endsWith('/')
    ? path
    : path.substring(0, path.length - 1);
};

export const removeStartSlash = (path) => {
  if (!path || !path.trim()) return;
  return isSlash(path) || !path.startsWith('/') ? path : path.substring(1);
};

export const removeSlashes = (path) => removeStartSlash(removeEndSlash(path));

export const navigateTo = (path, ref = window) =>
  ref.dispatchEvent(
    new CustomEvent('ldod-url-changed', {
      detail: { path },
      bubbles: true,
      composed: true,
    })
  );
