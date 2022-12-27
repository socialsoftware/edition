import './service-worker/sw.js';
import 'shared/modal.js';
import 'shared/router.js';
import './components/ldod-loading.js';
import NotFound from './components/not-found.js';
import './events-module.js';
import { store } from './store.js';

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

window.references = {};

const routes = await Object.keys(modules)
  .filter(
    (mod) =>
      mod !== 'shared/' &&
      mod !== 'home' &&
      mod !== 'vendor/' &&
      !mod.startsWith('@')
  )
  .reduce(async (prev, mfeName) => {
    await import(mfeName)
      .then(async (mod) => {
        const api = mod.default;
        const mfePath = api.path;
        window.references[mfeName] = api.references;
        if (mfePath) (await prev)[mfePath] = () => api;
      })
      .catch((e) => console.error(e));
    return await prev;
  }, Promise.resolve({}));

router.routes = routes;
if (modules['home']) {
  const homeApi = (await import('home').catch((e) => e)).default;
  router.index = () => homeApi;
}

document.getElementById('root').replaceWith(router);

const updateLanguage = (newState, currentState) => {
  if (newState.language !== currentState.language) {
    document.body
      .querySelectorAll('[language')
      .forEach((ele) => ele.setAttribute('language', newState.language));
  }
};

store.subscribe(updateLanguage);
