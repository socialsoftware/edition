/** @format */

import { ldodEventPublisher } from '@shared/ldod-events.js';
import references from './references';

export const loadingPublisher = isLoading => ldodEventPublisher('loading', isLoading);

export { ldodEventPublisher };

const fragmentsManagement = {
	name: 'admin',
	data: {
		name: 'admin',
		pages: [{ id: 'fragments_management', route: references.manageFragments() }],
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

customElements.whenDefined('nav-bar').then(() => ldodEventPublisher('header', fragmentsManagement));
