/** @format */

import { Store } from '@core';

const defaultState = {
	heteronymWeight: 0,
	dateWeight: 0,
	textWeight: 1,
	taxonomyWeight: 0,
	read: [],
};

export const readingStore = new Store(defaultState, {
	storageName: 'ldod-reading-store',
	keys: ['heteronymWeight', 'dateWeight', 'textWeight', 'taxonomyWeight', 'read'],
});

export const getState = () => readingStore.getState();
export const resetReadingStore = curr => {
	if (!curr) return readingStore.setState({ read: [] });
	if (getState().read.length <= 1) return;
	readingStore.setState(state => ({
		...state,
		read: [state.read.pop()],
	}));
};
