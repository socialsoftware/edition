/** @format */

import { Store } from '@core';

export const store = new Store(
	{ language: 'en', token: '' },
	{ storageName: 'ldod-store', keys: ['language', 'token'] }
);
store.subscribe(updateLanguage);

setLanguage();

export function getLanguage() {
	return store.getState().language;
}

function updateLanguage(newState, currentState) {
	if (newState.language !== currentState.language) setLanguage();
}

export function setLanguage() {
	document.body
		.querySelectorAll('[language]')
		.forEach(ele => ele.setAttribute('language', getLanguage()));
}
