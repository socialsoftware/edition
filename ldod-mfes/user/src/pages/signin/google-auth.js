/** @format */

import { socialAuthRequest } from '../../api-requests';
import { errorPublisher, messagePublisher } from '../../events-modules';
import { onAuthSuccess } from '../common-functions';
import constants from '../constants';

const G_CLIENT_ID = {
	client_id: '180526053205-km4c6tgpnh3j7orrmh0sgm81bibnpl1c.apps.googleusercontent.com',
};

let gPrompt;
let language;

const script = document.createElement('script');
script.src = 'https://accounts.google.com/gsi/client';
script.onload = () => {
	const id = window.google.accounts.id;
	id.initialize({
		...G_CLIENT_ID,
		callback: ({ credential }) => handleCredentials(credential, onAuthSuccess),
	});
	gPrompt = id.prompt;
};

document.head.appendChild(script);

function handleCredentials(accessToken, callback) {
	socialAuthRequest('google', { accessToken }, callback)
		.then(
			response =>
				response?.message && messagePublisher(constants[language || 'en'][response.message])
		)
		.catch(error => errorPublisher(constants[error?.message]));
}

export default lang => {
	language = lang;
	if (gPrompt) gPrompt();
};
