/** @format */

import references from './references';

let search;
let searchSimple;

const loadSearch = async () => {
	if (!search) search = await import('./search-router');
	return search;
};

export default {
	path: '/search',
	references,
	mount: async (lang, ref) => (await loadSearch()).mount(lang, ref),
	unMount: async () => (await loadSearch()).unMount(),
	bootstrap: () => {
		if (!searchSimple) searchSimple = import('./pages/search-simple/ldod-search-simple');
	},
};
