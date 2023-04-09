/** @format */

import newMfeReferences from './references';

export const headerData = {
	name: 'new-mfe',
	data: {
		name: 'new-mfe',
		pages: [
			{ id: 'func-1', route: newMfeReferences.funct1() },
			{ id: 'func-2', route: newMfeReferences.funct2() },
		],
	},
	constants: {
		pt: {
			'new-mfe': 'Novo MFE',
			'func-1': 'Funcionalidade 1',
			'func-2': 'Funcionalidade 2',
		},
		en: {
			'new-mfe': 'New MFE',
			'func-1': 'Feature 1',
			'func-2': 'Feature 2',
		},
		es: {
			'new-mfe': 'Nuevo MFE',
			'func-1': 'Funcionalidade 1',
			'func-2': 'Funcionalidade 2',
		},
	},
};
