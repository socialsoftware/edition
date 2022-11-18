import 'shared/router.js';
import { index } from './pages/index';
import style from './style.css?inline';

const READING_SELECTOR = 'div#readingContainer';

const routes = {};

const isDev = () => import.meta.env.DEV;

export const mount = (lang, ref) => {
  document.querySelector(ref).appendChild(<ReadingRouter language={lang} />);
};

export const unMount = () => {
  document.querySelector(READING_SELECTOR)?.remove();
};

export const ReadingRouter = ({ language }) => {
  return (
    <div id="readingContainer">
      <style>{style}</style>
      <div class="container">
        <ldod-router
          id="reading-router"
          base={isDev() ? '' : import.meta.env.VITE_BASE}
          route="/reading"
          routes={routes}
          index={index}
          language={language}></ldod-router>
      </div>
    </div>
  );
};
