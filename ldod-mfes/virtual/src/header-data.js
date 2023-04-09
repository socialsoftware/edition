/** @format */

import { virtualReferences } from './references';

export default {
	name: 'virtual',
	data: {
		name: 'virtual',
		pages: [
			{
				id: 'header_virtualeditions',
				route: virtualReferences.virtualEditions(),
			},
			{
				id: 'general_classificationGame',
				route: '/virtual/classificationGames',
			},
		],
	},
	constants: {
		pt: {
			virtual: 'Virtual',
			header_virtualeditions: 'Edições Virtuais',
			general_classificationGame: 'Jogo de Classificação',
		},
		en: {
			virtual: 'Virtual',
			header_virtualeditions: 'Virtual Editions',
			general_classificationGame: 'Classification Game',
		},
		es: {
			virtual: 'Virtual',
			header_virtualeditions: 'Ediciones Virtuales',
			general_classificationGame: 'Juego de Clasificación',
		},
	},
};
