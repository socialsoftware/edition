import { userRequest } from './apiRequests';
import { getState, setState, storage } from './store';
import { tokenEvent } from './utils';

if (storage?.token) {
  userRequest(getState().token)
    .then((user) => setState({ user }))
    .catch((error) => {
      console.error(error);
      window.dispatchEvent(tokenEvent());
    });
}
