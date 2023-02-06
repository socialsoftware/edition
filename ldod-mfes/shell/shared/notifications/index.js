/** @format */

import './src/ldod-notification.js';

document.getElementById('toggle-danger').onclick = () =>
	document.querySelectorAll('ldod-notification').forEach(element => {
		element.setAttribute('theme', 'danger');

		element.toggleAttribute('show');
	});

document.getElementById('toggle-success').onclick = () =>
	document.querySelectorAll('ldod-notification').forEach(element => {
		element.setAttribute('theme', 'success');

		element.toggleAttribute('show');
	});
