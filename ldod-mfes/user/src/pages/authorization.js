/** @format */

import { navigateTo } from '@core';
import { tokenAuthRequest } from '../api-requests.js';
import { errorPublisher, messagePublisher } from '../events-modules.js';

const mount = async (lang, ref) => {
	let params = new URL(document.location).searchParams;
	let path = `/sign-up-authorization?token=${params.get('token')}`;
	tokenAuthRequest(path)
		.then(res => res && messagePublisher(res.message))
		.catch(error => {
			console.error(error);
			error && errorPublisher(error.message);
		});
	navigateTo('/');
};
const unMount = () => console.info('unmount');

const path = '/sign-up-authorization';

export { mount, unMount, path };
