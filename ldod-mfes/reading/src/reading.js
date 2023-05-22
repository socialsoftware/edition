/** @format */

import references from './references';
let reading;

const loadReading = async () => {
	if (!reading) reading = await import('./reading-router');
	return reading;
};

export default {
	path: '/reading',
	references,
	preRender: async (dom, lang) => (await import('./headerSSR.js')).default(dom, lang),
	mount: async (lang, ref) => (await loadReading()).mount(lang, ref),
	unMount: async () => (await loadReading()).unMount(),
};
