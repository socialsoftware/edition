/** @format */

import { ldodEventSubscriber } from '@shared/ldod-events.js';
import { store } from './store.js';

function notifications(content, theme) {
	const notification = document.body.querySelector('ldod-notification');
	notification.querySelector('div[slot="toast-body"]').innerHTML = content;
	notification.setAttribute('theme', theme);
	notification.toggleAttribute('show', true);
}

const handlers = {
	token: ({ payload }) => store.setState({ token: payload }),
	language: ({ payload }) => store.setState({ language: payload || 'en' }),
	error: ({ payload }) => notifications(payload || 'Something went wrong', 'danger'),
	message: ({ payload }) => notifications(payload || '', 'success'),
};

ldodEventSubscriber('language', handlers.language);
ldodEventSubscriber('token', handlers.token);
ldodEventSubscriber('logout', handlers.token);
ldodEventSubscriber('message', handlers.message);
ldodEventSubscriber('error', handlers.error);
