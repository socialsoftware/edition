/** @format */

import references from './references';

const sleep = miliseconds => new Promise(res => setTimeout(res, miliseconds));

export default {
	path: `/${import.meta.env.VITE_MFE_NAME}`,
	mount: async (ref, lang) => await sleep(1000).then(() => console.log('mount')),
	unMount: () => console.log('unmount'),
	preRender: async (dom, lang) => (await import('./server-static-generation')).default(dom, lang),
	references,
};
