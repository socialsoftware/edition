import 'shared/router.js';
import style from './style.css?inline';
export const isDev = () => import.meta.env.DEV;

const SEARCH_SELECTOR = 'div#searchContainer';

const routes = {
  '/search-simple': async () =>
    await import('./pages/search-simple/searchSimple'),
  '/search-advanced': async () =>
    await import('./pages/search-advanced/searchAdvanced'),
};

export const mount = (lang, ref) => {
  document.querySelector(ref).appendChild(<SearchRouter language={lang} />);
};

export const unMount = () => {
  document.querySelector(SEARCH_SELECTOR)?.remove();
};

export const SearchRouter = ({ language }) => {
  return (
    <div id="searchContainer">
      <style>{style}</style>
      <div class="container">
        <ldod-router
          id="search-router"
          base={isDev() ? '' : import.meta.env.VITE_BASE}
          route="/search"
          routes={routes}
          language={language}></ldod-router>
      </div>
    </div>
  );
};
