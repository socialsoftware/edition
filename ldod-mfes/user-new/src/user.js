import { userReferences } from './user-references';
let user;

if (typeof window !== 'undefined') {
	await import('./events-modules');
	await import('./store');
	await import('./bootstrap');
}

async function loadUser() {
	if (user) return;
	user = await import('./user-router.js');
}

export default {
	path: '/user',
	references: userReferences,
	bootstrap: () => import('./user-component'),
	mount: async (lang, ref) => {
		await loadUser();
		await user.mount(lang, ref);
	},
	unMount: async () => {
		await loadUser();
		await user.unMount();
	},
};
