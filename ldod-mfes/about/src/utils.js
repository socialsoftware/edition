/** @format */

const ABOUT_SELECTOR = 'div#about-container';
export { sleep } from '@core';
export const isDev = () => import.meta.env.DEV;
export const getContainer = () => document.querySelector(ABOUT_SELECTOR);
