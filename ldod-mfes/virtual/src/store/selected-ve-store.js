/** @format */
/// <reference path="../models/selected-ve.js" />

import references from '../references';
import { editionsLinksPub, ldodEventBus, ldodEventSubscriber } from '../event-bus/event-bus';
import veAdminLinks from '../ve-admin-links';

export let selectedVirtualEditions = ['LdoD-Arquivo'];

customElements.whenDefined('nav-bar').then(() => {
	ldodEventBus.publish('ldod:header', veAdminLinks);
	updateEditionsLinks();
});

ldodEventSubscriber('login', ({ payload }) => {
	if (!payload) return;
	selectedVirtualEditions = ['LdoD-Arquivo', ...payload.selectedVE];
	updateEditionsLinks();
});

ldodEventSubscriber('logout', () => {
	selectedVirtualEditions = ['LdoD-Arquivo'];
	updateEditionsLinks();
});

export function updateEditionsLinks() {
	const links = selectedVirtualEditions.map(ed => ({
		key: ed,
		route: references.virtualEdition(ed),
	}));
	editionsLinksPub(links);
}

/**
 *
 * @param {SelectedVe | SelectedVe[]} selection
 */
function updateVeSelection(selection) {
	if (Array.isArray(selection)) updateVeSelectionList(selection);
	else updateVeSelectionWithSelectedVe(selection);
	updateEditionsLinks();
}

/**
 * @param {SelectedVe} selectedVe
 */
function updateVeSelectionWithSelectedVe(selectedVe) {
	selectedVirtualEditions = selectedVe.selected
		? [...selectedVirtualEditions, selectedVe.name]
		: selectedVirtualEditions.filter(ed => ed !== selectedVe.name);
	selectedVirtualEditions = [...new Set(selectedVirtualEditions)];
}

/**
 * @param {SelectedVe[]} veSelectionList
 */
function updateVeSelectionList(veSelectionList) {
	veSelectionList.forEach(updateVeSelection);
}

export { updateVeSelection };
