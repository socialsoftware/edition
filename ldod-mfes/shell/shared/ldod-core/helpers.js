/** @format */

export const sleep = async ms => new Promise(r => setTimeout(r, ms));
export const htmlRender = (...rawHtml) => document.createRange().createContextualFragment(rawHtml);
globalThis.htmlRender = htmlRender;
globalThis.sleep = sleep;
