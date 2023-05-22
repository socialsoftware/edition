/** @format */

import readingReferences from './references';

export const headerData = {
	name: 'reading',
	data: {
		name: 'reading',
		links: [
			{
				key: 'general_reading_sequences',
				route: readingReferences.index(),
			},
			{
				key: 'general_reading_visual_external',
				link: `${import.meta.env.VITE_NODE_HOST}/ldod-visual`,
			},
		],
	},
	constants: {
		pt: {
			reading: 'Leitura',
			general_reading_sequences: 'Sequências de Leitura',
			general_reading_visual_external: 'Livro Visual (externo)',
			general_reading_visual_integrated: 'Livro Visual (integrado)',
		},
		en: {
			reading: 'Reading',
			general_reading_sequences: 'Reading Sequences',
			general_reading_visual_external: 'Visual Book (External)',
			general_reading_visual_integrated: 'Visual Book (Integrated)',
		},
		es: {
			reading: 'Lectura',
			general_reading_sequences: 'Secuencias de lectura',
			general_reading_visual_external: 'Libro Visual (Externo)',
			general_reading_visual_integrated: 'Libro Visual (Integrado)',
		},
	},
};
