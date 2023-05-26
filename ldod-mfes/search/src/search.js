/** @format */

let search;
let searchSimple;

export default {
	path: '/search',
	mount: async (lang, ref) => await (await loadSearch()).mount(lang, ref),
	unMount: async () => await (await loadSearch()).unMount(),
	preRender: async (dom, lang) => (await import('./headerSSR.js')).default(dom, lang),
};

async function loadSearch() {
	if (!search) search = await import('./search-router');
	return search;
}

export async function loadSearchSimple() {
	if (!searchSimple) searchSimple = import('./pages/search-simple/ldod-search-simple');
}
