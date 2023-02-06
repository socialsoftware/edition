/** @format */

import { userRequest } from './api-requests';
import { loginPublisher, logoutPublisher } from './events-modules';
import { getState, setState, storageState } from './store';

if (storageState?.token) {
	userRequest(getState().token)
		.then(user => {
			setState({ user });
			loginPublisher(user);
		})
		.catch(error => {
			console.error(error);
			logoutPublisher();
		});
}
