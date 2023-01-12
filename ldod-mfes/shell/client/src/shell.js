import '@shared/modal.js';
import '@shared/router.js';
import './components/ldod-loading.js';
import NotFound from './components/not-found.js';
import './events-module.js';
import { store } from './store.js';

if (typeof window !== 'undefined') window.onpointermove = import('@shared/ldod-icons.js');

const getLanguage = () => store.getState().language;

const modules = JSON.parse(document.querySelector('script#importmap').textContent).imports;

document.querySelector('ldod-navbar').setAttribute('language', getLanguage());

const router = document.createElement('ldod-router');
router.id = 'shell';
router.setAttribute('base', '/ldod-mfes');
router.setAttribute('language', getLanguage());
router.fallback = NotFound;

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

router.routes = routes;

document.getElementById('root').replaceWith(router);

function updateLanguage(newState, currentState) {
	if (newState.language !== currentState.language)
		document.body.querySelectorAll('[language]').forEach(ele => ele.setAttribute('language', newState.language));
}

store.subscribe(updateLanguage);
