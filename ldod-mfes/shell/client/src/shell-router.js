/** @format */

import notFound from './components/not-found';
import { getLanguage } from './shell-store';

const modules = JSON.parse(document.querySelector('script#importmap').textContent).imports;
const router = document.body.querySelector('ldod-router');
router.setAttribute('language', getLanguage());
router.fallback = notFound;

const routes = await Object.keys(modules)
	.filter(mod => !mod.startsWith('@'))
	.reduce(async (prev, mfeName) => {
		await import(mfeName)
			.then(async mod => {
				const api = mod.default;
				const mfePath = api.path;

				if (mfePath === '/') router.index = () => api;
				else if (mfePath) (await prev)[mfePath] = () => api;
			})
			.catch(e => console.error(e));
		return await prev;
	}, Promise.resolve({}));

router.processRoutes(routes);
