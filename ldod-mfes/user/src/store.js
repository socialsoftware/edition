import { getPartialStorage, Store } from 'shared/store.js';
import { navigateTo } from 'shared/router.js';
import { userRequest } from './apiRequests';
const storage = getPartialStorage('ldod-store', ['token', 'language']);

const intialState = {
  token: undefined,
  language: storage?.language,
  user: undefined,
  index: 0,
};
export const store = new Store(intialState);
export const getState = () => store.getState();
export const setState = (state) => store.setState(state);
export const userFullName = () =>
  `${getState().user.firstName} ${getState().user.lastName}`;

const unsub = store.subscribe(async (curr, prev) => {
  if (prev.token !== curr.token && curr.token) {
    await userRequest(curr.token);
    navigateTo('/');
  }
});

setState({ token: storage?.token });

export const registerInstance = () => {
  setState({ index: getState().index + 1 });
  return getState().index;
};
