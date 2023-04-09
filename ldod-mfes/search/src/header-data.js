/** @format */

import references from './references';

export default {
	name: 'search',
	data: {
		name: 'search',
		pages: [
			{ id: 'search_simple', route: references.simple() },
			{ id: 'search_advanced', route: references.advanced() },
		],
	},
	constants: {
		pt: {
			search: 'Pesquisa',
			search_simple: 'Pesquisa Simples',
			search_advanced: 'Pesquisa Avançada',
		},
		en: {
			search: 'Search',
			search_simple: 'Simple Search',
			search_advanced: 'Advanced Search',
		},
		es: {
			search: 'Búsqueda',
			search_simple: 'Búsqueda Sencilla',
			search_advanced: 'Búsqueda Avanzada',
		},
	},
};
