/** @format */

import { ldodEventBus } from '@core';
import references from './references';
import { editionsMenuLinks } from './header-data';
import linksSchema from './json-schemas/links-schema';

ldodEventBus.register('text:editions-menu-links', linksSchema);

ldodEventBus.subscribe('text:editions-menu-links', updateEditionsMenu);

function updateEditionsMenu({ payload }) {
	ldodEventBus.publish('ldod:header', editionsMenuLinks(payload.links));
}

export const loadingPublisher = isLoading => ldodEventBus.publish('ldod:loading', isLoading);

const fragmentAdminMenuLinks = {
	name: 'admin',
	hidden: true,
	data: {
		name: 'admin',
		links: [{ key: 'fragments_management', route: references.manageFragments() }],
	},
	constants: {
		pt: {
			fragments_management: 'Gerir Fragmentos',
		},
		en: {
			fragments_management: 'Manage Fragments',
		},
		es: {
			fragments_management: 'Administrar Fragmentos',
		},
	},
};

customElements
	.whenDefined('nav-bar')
	.then(() => ldodEventBus.publish('ldod:header', fragmentAdminMenuLinks));
