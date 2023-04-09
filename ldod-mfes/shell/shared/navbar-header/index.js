/** @format */

import liDropdownHtml from './src/navbar-header-ssr';
import './src/li-dropdown';

export const newMfeReferences = {
	funct1: () => '/new-mfe/funct1',
	funct2: () => '/new-mfe/funct2',
};

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

document.body
	.querySelector("li[is='drop-down']")
	.setAttribute('data-headers', JSON.stringify(headerData));

const template = document.createElement('template');
template.innerHTML = liDropdownHtml(headerData);
const dropdown = template.content.firstElementChild.cloneNode(true);
dropdown.id = 'server';
document.body.appendChild(dropdown);
