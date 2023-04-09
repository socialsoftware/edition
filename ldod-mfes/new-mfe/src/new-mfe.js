/** @format */

import references from './references';

if (typeof window !== 'undefined') {
	import('./headerCSR.js');
}

export default {
	path: '/new-mfe',
	mount: async (ref, lang) => {
		console.log(ref, lang);
	},
	unMount: async () => {},
	references,
};
