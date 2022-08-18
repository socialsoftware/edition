import 'shared/router.js';
import style from './style.css?inline';
import { isDev } from './utils.js';

const ABOUT_SELECTOR = 'div#aboutContainer';

const routes = {
  '/archive': async () => await import(`./pages/archive/archive.jsx`),
  '/acknowledgements': async () => await import(`./pages/ack/ack.jsx`),
  '/articles': async () => await import(`./pages/articles/articles.jsx`),
};

export const mount = (lang, ref) => {
  document.querySelector(ref).appendChild(<AboutRouter language={lang} />);
};

export const unMount = () => {
  document.querySelector(ABOUT_SELECTOR)?.remove();
};

export const AboutRouter = ({ language }) => {
  return (
    <div id="aboutContainer">
      <style>{style}</style>
      <div class="container">
        <ldod-router
          id="about-router"
          base={isDev() ? '' : import.meta.env.VITE_BASE}
          route="/about"
          routes={routes}
          language={language}></ldod-router>
      </div>
      <home-info language={language} class="language"></home-info>
    </div>
  );
};
