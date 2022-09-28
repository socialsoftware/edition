import 'shared/router.js';
import style from './style.css?inline';

export const isDev = () => import.meta.env.DEV;
const TEXT_SELECTOR = 'div#textContainer';

const routes = {
  '/manage-fragments': async () =>
    await import('./pages/management/manageFragments.jsx'),
  '/sources': async () => await import('./pages/sources/sources.jsx'),
  '/fragments': async () => await import('./pages/fragments/fragments.jsx'),
  '/edition': async () => await import('./pages/edition/edition.jsx'),
  '/fragment': async () => await import('./pages/fragment/fragment.js'),
};

export const mount = (lang, ref) => {
  document.querySelector(ref).appendChild(<TextRouter language={lang} />);
};

export const unMount = () => {
  document.querySelector(TEXT_SELECTOR)?.remove();
};

export const TextRouter = ({ language }) => {
  return (
    <div id="textContainer">
      <style>{style}</style>
      <div class="container">
        <ldod-router
          id="text-router"
          base={isDev() ? '' : import.meta.env.VITE_BASE}
          route="/text"
          routes={routes}
          language={language}></ldod-router>
      </div>
    </div>
  );
};
