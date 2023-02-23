/** @format */

const userReferences = (await import('./user-references')).userReferences;

let user;

if (typeof window !== 'undefined') {
	await import('./events-modules').catch(e => console.error(e));
	await import('./store').catch(e => console.error(e));
	await import('./bootstrap').catch(e => console.error(e));
	import('./components/user-component');
}

const loadUser = async () => {
	if (user) return;
	user = await import('./user-router.js');
};

export default {
	path: '/user',
	references: userReferences,
	mount: async (lang, ref) => {
		await loadUser();
		await user.mount(lang, ref);
	},
	unMount: async () => {
		await loadUser();
		await user.unMount();
	},
};
