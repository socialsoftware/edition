import 'shared/router.js';
import style from './style.css?inline';

export const isDev = () => import.meta.env.DEV;
const TEXT_SELECTOR = 'div#textContainer';

const routes = {
  '/source-list': async () => await import('./pages/sourceList/sourceList.jsx'),
  '/fragments': async () => await import('./pages/fragments/fragments.jsx'),
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
          id="social-router"
          base={isDev() ? '' : import.meta.env.VITE_BASE}
          route="/text"
          routes={routes}
          language={language}></ldod-router>
      </div>
    </div>
  );
};
