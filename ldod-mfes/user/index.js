import '@src/components/user-component.js';
import router from '@src/user.js';
import { ldodEventSubscriber } from 'shared/router/src/events-module';

router.mount('en', '#root');

window.addEventListener('ldod-url-changed', ({ detail: { path } }) => {
	if (path === '/') {
		history.pushState({}, undefined, '/');
		document.querySelector('ldod-outlet').childNodes.forEach(node => node.remove());
	}
});

window.addEventListener('ldod-loading', ({ detail: { isLoading } }) => {
	const div = document.querySelector('div#loading');
	div.ariaHidden = String(!isLoading);
});

window.addEventListener('ldod-message', ({ detail: { message } }) => {
	const div = document.querySelector('div#message');
	div.innerHTML = message;
});

window.addEventListener('ldod-error', ({ detail: { message } }) => {
	const div = document.querySelector('div#error');
	div.innerHTML = message;
});

document.querySelector('#clear-message').addEventListener('click', clearMessage);

document.querySelectorAll('button.lang').forEach(btn => {
	btn.addEventListener('click', () => {
		document.querySelectorAll('[language]').forEach(ele => {
			ele.setAttribute('language', btn.id);
		});
	});
});

function clearMessage() {
	document.getElementById('message').innerHTML = '';
	document.getElementById('error').innerHTML = '';
}

ldodEventSubscriber('logout', event => {
	console.log(event);
});
