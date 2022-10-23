import { getPartialStorage, Store } from 'shared/store.js';
import { userRequest } from './apiRequests';
import { tokenEvent } from './utils';
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

if (storage?.token) {
  console.log(storage?.token);
  setState({ token: storage?.token });
  userRequest(getState().token)
    .then((user) => setState({ user }))
    .catch((error) => window.dispatchEvent(tokenEvent()));
}

export const registerInstance = () => {
  setState({ index: getState().index + 1 });
  return getState().index;
};
