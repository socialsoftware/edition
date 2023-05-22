/** @format */

const logError = console.error;

let user;
const isBrowserEnv =
	typeof window !== 'undefined' &&
	typeof document !== 'undefined' &&
	typeof navigator !== 'undefined';

if (isBrowserEnv) {
	await import('./events-modules').catch(logError);
	await import('./store').catch(logError);
	await import('./bootstrap').catch(logError);
	import('./admin-header').catch(logError);
	import('./auth-menu-component').catch(logError);
}

const loadUser = async () => {
	if (!user) user = await import('./user-router.js');
	return user;
};

export default {
	path: '/user',
	mount: async (lang, ref) => (await loadUser()).mount(lang, ref),
	unMount: async () => (await loadUser()).unMount(),
	preRender: async (dom, lang) => (await import('./pre-render-navbar-menu')).default(dom, lang),
};
