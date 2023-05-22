/** @format */

import references from './references';

export default {
	name: 'virtual',
	data: {
		name: 'virtual',
		links: [
			{
				key: 'header_virtualeditions',
				route: references.virtualEditions(),
			},
			{
				key: 'general_classificationGame',
				route: references.classificationGames(),
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
