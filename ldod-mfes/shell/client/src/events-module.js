import { ldodEventSubscriber } from '@shared/ldod-events.js';
import { store } from './store.js';

function updateModal(id, content, show) {
	const modal = document.body.querySelector(`ldod-modal#${id}`);
	modal.querySelector('div[slot="body-slot"]').innerHTML = content;
	modal.toggleAttribute('show', show);
}

const handlers = {
	token: ({ payload }) => store.setState({ token: payload }),
	language: ({ payload }) => store.setState({ language: payload || 'en' }),
	error: ({ payload }) => updateModal('error', payload || 'Something went wrong', true),
	message: ({ payload }) => updateModal('success', payload || '', true),
};

ldodEventSubscriber('language', handlers.language);
ldodEventSubscriber('token', handlers.token);
ldodEventSubscriber('logout', handlers.token);
ldodEventSubscriber('message', handlers.message);
ldodEventSubscriber('error', handlers.error);
