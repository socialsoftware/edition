/** @format */

import aboutReferences from './references';

export const headerData = {
	name: 'about',
	data: {
		name: 'about',
		links: [
			{ key: 'header_archive', route: aboutReferences.archive() },
			{ key: 'header_videos', route: aboutReferences.videos() },
			{ key: 'header_tutorials', route: aboutReferences.tutorials() },
			{ key: 'header_faq', route: aboutReferences.faq() },
			{ key: 'header_encoding', route: aboutReferences.encoding() },
			{ key: 'header_bibliography', route: aboutReferences.articles() },
			{ key: 'header_book', route: aboutReferences.book() },
			{ key: 'header_conduct', route: aboutReferences.conduct() },
			{ key: 'header_privacy', route: aboutReferences.privacy() },
			{ key: 'header_team', route: aboutReferences.team() },
			{ key: 'header_acknowledgements', route: aboutReferences.ack() },
			{ key: 'header_contact', route: aboutReferences.contact() },
			{ key: 'header_copyright', route: aboutReferences.copyright() },
		],
	},
	constants: {
		pt: {
			header_archive: 'Arquivo LdoD',
			about: 'Acerca',
			header_videos: 'Vídeos',
			header_tutorials: 'Tutoriais e Oficinas',
			header_faq: 'Perguntas Frequentes',
			header_encoding: 'Codificação de Texto',
			header_bibliography: 'Bibliografia',
			header_book: 'Livro',
			header_conduct: 'Código de conduta',
			header_contact: 'Contacto',
			header_copyright: 'Copyright',
			header_acknowledgements: 'Agradecimentos',
			header_privacy: 'Política de Privacidade',
			header_team: 'Equipa Editorial',
		},
		en: {
			header_archive: 'LdoD Archive',
			about: 'About',
			header_videos: 'Videos',
			header_tutorials: 'Tutorials and Workshops',
			header_faq: 'Frequently Asked Questions',
			header_encoding: 'Text Encoding',
			header_bibliography: 'Bibliography',
			header_book: 'Book',
			header_contact: 'Contacto',
			header_conduct: 'Código de Conduta',
			header_copyright: 'Copyright',
			header_privacy: 'Privacy Policy',
			header_team: 'Editorial Team',
			header_acknowledgements: 'Acknowledgements',
		},
		es: {
			header_archive: 'Archivo LdoD',
			about: 'Acerca',
			header_videos: 'Vídeos',
			header_tutorials: 'Tutoriales y Talleres',
			header_faq: 'Preguntas Frecuentes',
			header_encoding: 'Codificación de Texto',
			header_bibliography: 'Bibliografía',
			header_book: 'Libro',
			header_conduct: 'Código de Conducta',
			header_contact: 'Contacto',
			header_copyright: 'Copyright',
			header_privacy: 'Política de Privacidad',
			header_team: 'Equipo Editorial',
			header_acknowledgements: 'Agradecimientos',
		},
	},
};
