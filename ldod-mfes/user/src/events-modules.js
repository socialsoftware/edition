/** @format */

import { ldodEventPublisher, ldodEventSubscriber, ldodEventBus } from '@shared/ldod-events.js';
import { userReferences } from './user-references';

export const errorPublisher = message => ldodEventPublisher('error', message);
export const messagePublisher = message => ldodEventPublisher('message', message);

export const tokenPublisher = token => ldodEventPublisher('token', token);
export const logoutPublisher = () => ldodEventPublisher('logout');
export const loginPublisher = user => ldodEventPublisher('login', user);

export const loginSubscriber = handler => ldodEventSubscriber('login', handler);

export const logoutSubscriber = handler => ldodEventSubscriber('logout', handler);

export { ldodEventBus };

const usersManagement = {
	name: 'admin',
	data: {
		name: 'admin',
		pages: [{ id: 'users_management', route: userReferences.manageUsers() }],
	},
	constants: {
		pt: {
			users_management: 'Gerir utilizadores',
		},
		en: {
			users_management: 'Manage users',
		},
		es: {
			users_management: 'Administrar Usuarios',
		},
	},
};

customElements.whenDefined('nav-bar').then(() => ldodEventPublisher('header', usersManagement));
