const userReferences = (await import('./user-references')).userReferences;
let User;

if (typeof window !== 'undefined') {
	await import('./events-modules');
	await import('./store');
	await import('./bootstrap');
}

const loadUser = async () => {
	if (User) return;
	User = await import('./user-router.js');
};

export default {
	path: '/user',
	references: userReferences,
	bootstrap: () => import('./components/user-component'),
	mount: async (lang, ref) => {
		await loadUser();
		await User.mount(lang, ref);
	},
	unMount: async () => {
		await loadUser();
		await User.unMount();
	},
};
