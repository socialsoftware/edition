/** @format */

import { parse } from 'node-html-parser';
import { loadMfes } from './mfes.js';
import { staticPath } from './constants.js';

export async function preRenderIndexHtml(dom) {
	//await preRenderModal(dom);
	await preRenderHeaders(dom);
}

async function preRenderModal(dom) {
	await import('../static/shared/ssr-modal.js')
		.then(({ staticGeneration }) => {
			dom.querySelectorAll('ldod-modal').forEach(modal => {
				modal.querySelector('template')?.remove();
				const template = staticGeneration(
					modal.getAttribute('dialog-class'),
					modal.hasAttribute('no-footer')
				);
				modal.appendChild(parse(template));
			});
		})
		.catch(e => console.error(e));
}
/**
 *
 * @param {HTMLElement} dom
 */
export async function preRenderNavbar(dom) {
	const api = await import(`${staticPath}/home/home.js`).catch(e => console.error(e));
	const navbarPreRender = api.default.preRender?.navbar;
	if (typeof navbarPreRender === 'function') {
		const navbar = parse(await navbarPreRender());
		dom.querySelector('nav-bar').replaceWith(navbar);
	}
}

export async function preRenderHeaders(dom) {
	const mfes = loadMfes().sort();
	const headers = await mfes.reduce(async (headersString, mfe) => {
		const result = await headersString;
		const api = await import(`${staticPath}/${mfe}/${mfe}.js`).catch(e => console.error(e));
		const header = api?.default?.preRender?.header || '';
		if (typeof header === 'function') return result + (await header());
		return result;
	}, Promise.resolve(''));
	const dropdownContainer = dom.querySelector('div#navbar-nav > ul.navbar-nav');
	if (!dropdownContainer) return;
	dropdownContainer
		.querySelectorAll("li[is='drop-down']:not([key='admin'])")
		.forEach(n => n.remove());
	const inner = dropdownContainer.innerHTML;
	dropdownContainer.innerHTML = headers + inner;
}
