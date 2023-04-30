/** @format */
import { parse } from 'node-html-parser';
import { workerData, parentPort } from 'worker_threads';

const api = (await import(workerData)).default;

parentPort.once('message', async rawDom => {
	const preRender = api.preRender;
	const dom = parse(rawDom);
	if (typeof preRender === 'function') await api.preRender(dom, 'en');
	parentPort.postMessage(dom.outerHTML);
});
