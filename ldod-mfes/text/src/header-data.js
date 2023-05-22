/** @format */

import textReferences from './references';

export const documentsMenuLinks = {
	name: 'text',
	data: {
		name: 'text',
		links: [
			{ key: 'authorial_source', route: textReferences.sources() },
			{ key: 'fragment_codified', route: textReferences.fragments() },
		],
	},
	constants: {
		pt: {
			text: 'Documentos',
			authorial_source: 'Testemunhos',
			fragment_codified: 'Fragmentos Codificados',
		},
		en: {
			text: 'Documents',
			authorial_source: 'Witnesses',
			fragment_codified: 'Encoded Fragments',
		},
		es: {
			text: 'Documentos',
			authorial_source: 'Testimonios',
			fragment_codified: 'Fragmentos Codificados',
		},
	},
};

export const editionsMenuLinks = (links = []) => ({
	name: 'editions',
	replace: true,
	data: {
		name: 'editions',
		links: [
			{
				key: 'general_editor_prado',
				route: textReferences.edition('JPC'),
			},
			{
				key: 'general_editor_cunha',
				route: textReferences.edition('TSC'),
			},
			{
				key: 'general_editor_zenith',
				route: textReferences.edition('RZ'),
			},
			{
				key: 'general_editor_pizarro',
				route: textReferences.edition('JP'),
			},
			{ key: 'divider' },
			...links,
		],
	},
	constants: {
		pt: {
			editions: 'Edições',
			header_title: 'Arquivo LdoD',
			general_editor_prado: 'Jacinto do Prado Coelho',
			general_editor_zenith: 'Richard Zenith',
			general_editor_cunha: 'Teresa Sobral Cunha',
			general_editor_pizarro: 'Jerónimo Pizarro',
		},
		en: {
			header_title: 'LdoD Archive',
			editions: 'Editions',
			general_editor_prado: 'Jacinto do Prado Coelho',
			general_editor_zenith: 'Richard Zenith',
			general_editor_cunha: 'Teresa Sobral Cunha',
			general_editor_pizarro: 'Jerónimo Pizarro',
		},
		es: {
			header_title: 'Arquivo LdoD',
			editions: 'Ediciones',
			general_editor_prado: 'Jacinto do Prado Coelho',
			general_editor_zenith: 'Richard Zenith',
			general_editor_cunha: 'Teresa Sobral Cunha',
			general_editor_pizarro: 'Jerónimo Pizarro',
		},
	},
});
