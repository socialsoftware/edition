/** @format */

import { annotatorService } from './src/annotations';
import { fetcher } from '@core';

const referenceNode = document.querySelector('div#virtual-transcriptionWrapper');

document.querySelector('form#tokenForm').onsubmit = async e => {
	e.preventDefault();
	const form = e.target;
	const data = Object.fromEntries(new FormData(form));
	fetcher.post(`${import.meta.env.VITE_HOST}/auth/sign-in`, data).then(data => {
		if (data.accessToken) {
			window.token = data.accessToken;
			form.toggleAttribute('auth', true);
			form.reset();
			annotatorService({
				interId: '281861523768179',
				referenceNode,
			});
		}
	});
};

annotatorService({
	interId: '281861523768179',
	referenceNode,
});
