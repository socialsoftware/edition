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

const routes = await Object.keys(modules).reduce(async (prev, mfeName) => {
  await import(mfeName)
    .then(async (mod) => {
      const api = mod.default;
      const mfePath = api.path;
      if (mfePath === '/') {
        router.index = () => api;
        return;
      }
      if (mfePath) (await prev)[mfePath] = () => api;
    })
    .catch((e) => console.error(e));
  return prev;
}, Promise.resolve({}));

router.routes = routes;
document.getElementById('root').append(router);

const updateLanguage = (newV, oldV) => {
  if (newV.language !== oldV.language) {
    router.language = newV.language;
  }
};
const unsub = store.subscribe(updateLanguage);
