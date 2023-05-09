/** @format */

import { loadMfes } from './mfes.js';
import { getEntryPoint } from './importmap.js';
import { staticPath } from './constants.js';
import { Worker } from 'worker_threads';
import { parse } from 'node-html-parser';

export async function preRenderIndexHtml(dom) {
	return await preRenderShell(await preRenderMfes(dom));
}

async function preRenderMFE(entryPoint, dom) {
	const worker = new Worker('./server/src/pre-render-worker.js', {
		workerData: `${staticPath}/${entryPoint}`,
	});
	worker.postMessage(dom.outerHTML);
	return new Promise(resolve => {
		worker.on('message', rawDom => {
			resolve(parse(rawDom));
			worker.terminate();
		});
		worker.on('error', err => {
			console.error(err);
			worker.terminate();
			resolve('');
		});
		worker.on('exit', code => console.log('pre-render: ' + entryPoint));
	});
}

async function preRenderMfes(dom) {
	let newDom = dom;
	let entry;
	const mfes = loadMfes().sort(a => a === 'home' && -1);
	for (const mfe of mfes) {
		entry = getEntryPoint(mfe);
		if (entry) newDom = await preRenderMFE(entry, newDom);
	}
	return newDom;
}

async function preRenderShell(dom) {
	const components = ['scroll-btn', 'loading-spinner'];
	for (const component of components) {
		const preRender = (await import(`../static/shell/${component}-ssr.js`)).default;
		dom.querySelector(`body>${component}`).innerHTML = preRender();
	}
	return dom;
}
