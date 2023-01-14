import { getPartialStorage, Store } from '@shared/store.js';
export const storage = getPartialStorage('ldod-store', ['token', 'language']);
const intialState = {
	token: storage?.token,
	language: storage?.language,
	user: undefined,
	index: 0,
};
export const store = new Store(intialState);
export const getState = () => store.getState();
export const setState = state => store.setState(state);
export const userFullName = () => `${getState().user.firstName} ${getState().user.lastName}`;