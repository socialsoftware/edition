/** @format */

import { getPartialStorage, Store } from '@core';

export let storageState;

const getStorageState = () => {
	storageState = getPartialStorage('ldod-store', ['token', 'language']);
	return {
		token: storageState?.token,
		language: storageState?.language,
	};
};

export const store = new Store({ ...getStorageState(), user: undefined });
export const getState = () => store.getState();
export const setState = state => store.setState(state);
export const userFullName = () =>
	`${getState().user?.firstName ?? ''} ${getState().user?.lastName ?? ''}`;
export const getUser = () => getState().user;
export const getToken = () => getState().token;

export function isAdmin() {
	const user = getUser();
	return user?.roles?.includes('ROLE_ADMIN');
}

export function isAuth() {
	return getUser() && getToken() ? true : false;
}

window.addEventListener('storage', e => {
	setState({ ...getState(), ...getStorageState() });
});
