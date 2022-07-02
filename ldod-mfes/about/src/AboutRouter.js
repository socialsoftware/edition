import { parseHTML } from 'shared/public/utils.js';
import { Store } from 'shared/public/store.js';

window.html = String.raw;

const state = { language: undefined };
const store = new Store(state);
const onLanguage = ({ detail }) => setLanguage(detail);
const setLanguage = (language) => store.setState(language);

const importModule = async (module, file) => {
  const { mount, unMount } = await import(`./pages/${module}/${file}.js`);
  return {
    mount: (language, ref) => mount(language, ref),
    unMount: unMount,
  };
};

const routes = {
  '/archive': async () => await importModule('archive', 'Archive'),
};
const aboutRef = 'ldod-router#about>ldod-outlet';

const unsubs = {};

const langSubscribe = () =>
  (unsubs.language = store.subscribe(updateLanguage()));

const AboutRouter = (routes, language) => {
  const aboutRouter = parseHTML(
    html`
      <div id="ldod-about-router-container">
        <div class="container">
          <div class="col-md-8 col-md-offset-2 ldod-about">
            <ldod-router
              base="/about/"
              id="about"
              class="language"
            ></ldod-router>
          </div>
        </div>
        <home-info language=${language} class="language"></home-info>
      </div>
    `
  );
  const router = aboutRouter.querySelector('ldod-router');
  router.routes = routes;
  router.language = language;
  return aboutRouter;
};

export const mount = (language, ref) => {
  document.head.appendChild(
    parseHTML(
      html`<link
        id="about"
        rel="stylesheet"
        href=${new URL(
          import.meta.url.includes('/public/')
            ? './css/about.css'
            : '../css/about.css',
          import.meta.url
        ).href}
      />`
    )
  );
  window.addEventListener('ldod-language', onLanguage);
  langSubscribe();
  setLanguage({ language });
  document.querySelector(ref).appendChild(AboutRouter(routes, language));
};

export const unMount = () => {
  document.querySelector('head>link#about').remove();
  window.removeEventListener('ldod-language-changed', onLanguage);
  unsubs.language();
  const about = document.querySelector('div#ldod-about-router-container');
  about?.remove();
};
function updateLanguage() {
  return async (newV, oldV) => {
    if (oldV.language && newV.language !== oldV.language) {
      document
        .querySelector('div#ldod-about-router-container')
        .querySelectorAll('.language')
        .forEach((node) => {
          node.language = newV.language;
          node.setAttribute('language', newV.language);
        });

      const path = location.pathname.split('/about')[1];
      if (path) {
        const module = await routes[path]();
        module.unMount();
        module.mount(newV.language, aboutRef);
      }
    }
  };
}
