import { store } from './store.js';
import './eventListeners.js';
import './components/loading/LdodLoading.js';
import NotFound from './components/not-found/NotFound.js';

const getLanguage = () => store.getState().language;

const modules = JSON.parse(
  document.querySelector('script#importmap').textContent
).imports;

document.querySelector('ldod-navbar').setAttribute('language', getLanguage());

const router = document.createElement('ldod-router');
router.id = 'shell';
router.setAttribute('base', '/ldod-mfes');
router.setAttribute('language', getLanguage());
router.fallback = NotFound;

delete modules['shared/'];

console.log(await import('user'));

const routes = await Object.keys(modules).reduce(async (acc, name) => {
  try {
    const api = (await import(name)).default;

    const path = api.path;
    if (path === '/') {
      router.index = () => api;
      return await acc;
    }
    if (path) (await acc)[path] = () => api;
  } catch (error) {
    console.error(error);
  }
  return await acc;
}, Promise.resolve({}));
router.routes = routes;

document.getElementById('root').append(router);

const updateLanguage = (newV, oldV) => {
  if (newV.language !== oldV.language) {
    router.language = newV.language;
  }
};
const unsub = store.subscribe(updateLanguage);
