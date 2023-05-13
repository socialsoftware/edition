/** @format */

import { navigateTo } from '@core';
import { tokenConfirmRequest } from '../api-requests.js';
import { userReferences } from '../user-references';
import { errorPublisher, messagePublisher } from '../events-modules.js';

const mount = async () => {
	let params = new URL(document.location).searchParams;
	let path = `/sign-up-confirmation?token=${params.get('token')}`;
	await tokenConfirmRequest(path)
		.then(res => res && messagePublisher(res.message))
		.catch(error => {
			console.error(error);
			error && errorPublisher(error.message);
		});
	navigateTo(userReferences.signin());
};
const unMount = () => console.info('unmount');

const path = '/sign-up-confirmation';

export { mount, unMount, path };
