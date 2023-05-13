/** @format */

import { ldodEventSubscriber, ldodEventPublisher } from '@core';
import { getLanguage, store } from './shell-store.js';
let notification;

const loadNotification = async () => {
	notification = (await import('./components/notification.js')).default;
};

const notificationContainer = document.getElementById('notification-container');

async function notifications(content, theme) {
	if (!notification) await loadNotification();
	notificationContainer.appendChild(notification(content, theme));
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
ldodEventPublisher('language', getLanguage());
