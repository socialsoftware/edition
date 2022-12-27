import { userRequest } from './api-requests';
import { logoutPublisher } from './events-modules';
import { getState, setState, storage } from './store';

if (storage?.token) {
  userRequest(getState().token)
    .then((user) => {
      setState({ user });
      console.log('user setted');
    })
    .catch((error) => {
      console.error(error);
      logoutPublisher();
    });
}
