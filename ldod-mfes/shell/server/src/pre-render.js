/** @format */

import { parse } from 'node-html-parser';
import { loadMfes } from './mfes.js';
import { getEntryPoint } from './importmap.js';
import { staticPath } from './constants.js';

export async function preRenderIndexHtml(dom) {
	await preRenderMFEs(dom);
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
export async function preRenderMFE(entryPoint, dom) {
	const api = await import(`${staticPath}/${entryPoint}`).catch(e => console.error(e));
	const preRender = api?.default.preRender;
	if (typeof preRender === 'function') await preRender(dom, 'en');
}

export async function preRenderMFEs(dom) {
	const mfes = loadMfes().sort((a, b) => a === 'home' && -1);
	for (const mfe of mfes) {
		const entry = getEntryPoint(mfe);
		await preRenderMFE(entry, dom);
	}
}

export async function cleanUpMFE(entryPoint, dom) {
	const api = await import(`${staticPath}/${entryPoint}`).catch(e => console.error(e));
	const cleanUp = api.default.cleanUp;
	if (typeof cleanUp === 'function') await cleanUp(dom);
}
