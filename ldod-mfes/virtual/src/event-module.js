/** @format */

import { references } from './virtual';
import { ldodEventPublisher, ldodEventSubscriber, ldodEventBus } from '@core';
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

const virtualEditionAdminMenuLinks = {
	name: 'admin',
	hidden: true,
	data: {
		name: 'admin',
		links: [{ key: 've_management', route: references.manageVirtualEditions() }],
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
	ldodEventBus.publish('ldod:header', virtualEditionAdminMenuLinks);
});

function selectedVeHandler({ payload }) {
	selectedVEs = payload.selected
		? [...selectedVEs, payload.name]
		: selectedVEs.filter(ed => ed !== payload.name);
	selectedVEs = [...new Set(selectedVEs)];
	updateEditions();
}

function updateEditions() {
	ldodEventBus.publish('text:editions-menu-links', {
		links: selectedVEs.map(ed => ({ key: ed, route: `/virtual/edition/acronym/${ed}` })),
	});
}

export {
	errorPublisher,
	loadingPublisher,
	selectedVePublisher,
	messagePublisher,
	updateSelecteVEs,
};
