/** @format */

import { userReferences } from './user-references';

export default {
	name: 'admin',
	hidden: true,
	data: {
		name: 'admin',
		links: [{ key: 'users_management', route: userReferences.manageUsers() }],
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
