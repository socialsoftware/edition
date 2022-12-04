const ABOUT_SELECTOR = 'div#aboutContainer';
export { sleep } from 'shared/utils.js';
export const isDev = () => import.meta.env.DEV;
export const getContainer = () => document.querySelector(ABOUT_SELECTOR);