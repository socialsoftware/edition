/** @format */

import { fetcher } from '@core';

import router, { loadVirtualComponents } from './src/virtual.js';

loadVirtualComponents();

router.mount('en', '#root');

document.querySelectorAll('button.lang').forEach(btn => {
	btn.addEventListener('click', () =>
		document.querySelectorAll('[language]').forEach(ele => {
			ele.setAttribute('language', btn.id);
		})
	);
});

document.querySelector('form#tokenForm').onsubmit = async e => {
	e.preventDefault();
	const form = e.target;
	const data = Object.fromEntries(new FormData(form));
	fetcher.post(`${import.meta.env.VITE_HOST}/auth/sign-in`, data).then(data => {
		if (data.accessToken) {
			window.token = data.accessToken;
			form.toggleAttribute('auth', true);
			form.reset();
		}
	});
};

document.querySelector('button#fragment-container-button').addEventListener('click', () => {
	let isHidden = document.querySelector('div#fragment-container').hidden;
	document.querySelector('div#fragment-container').hidden = !isHidden;
});

document.body.addEventListener('ldod-virtual-selected', ({ detail }) => {
	console.log(detail);
	const vt = document.querySelector('virtual-transcription').cloneNode();
	document.querySelector('virtual-transcription').replaceWith(vt);
});
