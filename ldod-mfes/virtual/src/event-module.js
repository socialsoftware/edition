/** @format */

import { virtualReferences } from './virtual';
import { ldodEventPublisher, ldodEventSubscriber } from '@shared/ldod-events.js';
export let selectedVe = ['LdoD-Arquivo'];
const errorPublisher = error => ldodEventPublisher('error', error);
const selectedVePublisher = ve => ldodEventPublisher('selected-ve', ve);
const messagePublisher = info => ldodEventPublisher('message', info);
const loadingPublisher = bool => ldodEventPublisher('loading', bool);

ldodEventSubscriber('selected-ve', selectedVeHandler);
ldodEventSubscriber('login', ({ payload }) => {
	selectedVe = ['LdoD-Arquivo', ...payload.selectedVE];
	updateEditions();
});

ldodEventSubscriber('logout', () => {
	selectedVe = ['LdoD-Arquivo'];
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
	selectedVe = payload.selected
		? [...selectedVe, payload.name]
		: selectedVe.filter(ed => ed !== payload.name);
	updateEditions();
}
function updateEditions() {
	ldodEventPublisher('header', {
		replace: true,
		name: 'editions',
		data: {
			name: 'editions',
			pages: selectedVe.map(ed => ({ id: ed, route: `/virtual/edition/acronym/${ed}` })),
		},
		constants: {
			pt: {},
			en: {},
			es: {},
		},
	});
}

function defaultSelectedVE() {}

export { errorPublisher, loadingPublisher, selectedVePublisher, messagePublisher };
