/** @format */

import { getPartialStorage, Store } from '@shared/store.js';
const storage = () => getPartialStorage('ldod-store', ['token', 'language']);
export let storageState;
const getStorageState = () => {
	storageState = storage();
	return {
		token: storageState?.token,
		language: storageState?.language,
	};
};

export const store = new Store({ ...getStorageState(), user: undefined });
export const getState = () => store.getState();
export const setState = state => store.setState(state);
export const userFullName = () => `${getState().user.firstName} ${getState().user.lastName}`;

window.addEventListener('storage', () => {
	setState({ ...getState(), ...getStorageState() });
});
