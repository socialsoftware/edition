/** @format */

import { virtualReferences } from './virtual';
import { ldodEventPublisher, ldodEventSubscriber } from '@shared/ldod-events.js';
export let selectedVEs = ['LdoD-Arquivo'];
const errorPublisher = error => ldodEventPublisher('error', error);
const selectedVePublisher = ve => ldodEventPublisher('selected-ve', ve);
const updateSelecteVEs = ves => ves.forEach(selectedVePublisher);
const messagePublisher = info => ldodEventPublisher('message', info);
const loadingPublisher = bool => ldodEventPublisher('loading', bool);

ldodEventSubscriber('selected-ve', selectedVeHandler);
ldodEventSubscriber('login', ({ payload }) => {
	selectedVEs = ['LdoD-Arquivo', ...payload.selectedVE];
	updateEditions();
});

ldodEventSubscriber('logout', () => {
	selectedVEs = ['LdoD-Arquivo'];
	updateEditions();
});

const veManagement = {
	name: 'admin',
	data: {
		name: 'admin',
		pages: [{ id: 've_management', route: virtualReferences.manageVirtualEditions() }],
	},
	constants: {
		pt: {
			ve_management: 'Gerir Edições Virtuais',
		},
		en: {
			ve_management: 'Manage Virtual Editions',
		},
		es: {
			ve_management: 'Administrar Ediciones Virtuales',
		},
	},
};

customElements.whenDefined('nav-bar').then(() => {
	updateEditions();
	ldodEventPublisher('header', veManagement);
});

function selectedVeHandler({ payload }) {
	selectedVEs = payload.selected
		? [...selectedVEs, payload.name]
		: selectedVEs.filter(ed => ed !== payload.name);
	selectedVEs = [...new Set(selectedVEs)];
	updateEditions();
}
function updateEditions() {
	ldodEventPublisher('header', {
		replace: true,
		name: 'editions',
		data: {
			name: 'editions',
			pages: selectedVEs.map(ed => ({ id: ed, route: `/virtual/edition/acronym/${ed}` })),
		},
		constants: {
			pt: {},
			en: {},
			es: {},
		},
	});
}

export {
	errorPublisher,
	loadingPublisher,
	selectedVePublisher,
	messagePublisher,
	updateSelecteVEs,
};
