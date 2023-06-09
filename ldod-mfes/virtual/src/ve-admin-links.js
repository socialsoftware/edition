/** @format */
import references from './references';

export default {
	name: 'admin',
	hidden: true,
	data: {
		name: 'admin',
		links: [{ key: 've_management', route: references.manageVirtualEditions() }],
	},
	constants: {
		pt: {
			ve_management: 'Gerir Edições Virtuais',
		},
		en: {
			ve_management: 'Manage Virtual Editions',
		},
		es: {
			ve_management: 'Administrar Ediciones Virtuales',
		},
	},
};
