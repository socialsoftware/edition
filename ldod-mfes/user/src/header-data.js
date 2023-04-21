/** @format */

import { userReferences } from './user-references';

export default {
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
