import { store } from './store.js';
window.html = String.raw;

const getLanguage = () => store.getState().language;
const updateLanguage = (newV, oldV) => {
  if (newV.language !== oldV.language) {
    router.language = newV.language;
  }
};

const unsub = store.subscribe(updateLanguage);

const modules = JSON.parse(
  document.querySelector('script#importmap').textContent
).imports;

delete modules['shared/'];

document.querySelector('ldod-navbar').setAttribute('language', getLanguage());

const routes = await Object.keys(modules).reduce(async (acc, name) => {
  try {
    const api = (await import(name))?.default ?? '';
    const path = api.path;
    if (path) (await acc)[path] = () => api;
  } catch (error) {
    console.error(error);
  }
  return await acc;
}, Promise.resolve({}));

const router = document.createElement('ldod-router');
router.id = 'shell';
router.language = getLanguage();
router.routes = routes;

document.getElementById('root').appendChild(router);
