import 'shared/router.js';
import style from './style.css?inline';
import { isDev } from './utils.js';

const ABOUT_SELECTOR = 'div#aboutContainer';
const getContainer = () => document.querySelector(ABOUT_SELECTOR);
const getHomeInfo = () => getContainer().querySelector('home-info');

const routes = {
  '/archive': async () => await import(`./pages/archive/archive.jsx`),
  '/acknowledgements': async () => await import(`./pages/ack/ack.jsx`),
  '/articles': async () => await import(`./pages/articles/articles.jsx`),
  '/book': async () => await import(`./pages/book/book.jsx`),
  '/conduct': async () => await import(`./pages/conduct/conduct.jsx`),
  '/copyright': async () => await import(`./pages/copyright/copyright.jsx`),
  '/encoding': async () => await import('./pages/encoding/encoding.jsx'),
  '/faq': async () => await import('./pages/faq/faq.jsx'),
  '/privacy': async () => await import('./pages/privacy/privacy.jsx'),
  '/team': async () => await import('./pages/team/team.jsx'),
  '/tutorials': async () => await import('./pages/tutorials/tutorials.jsx'),
  '/videos': async () => await import('./pages/videos/videos.jsx'),
};

export const mount = (lang, ref) => {
  document.querySelector(ref).appendChild(<AboutRouter language={lang} />);
};

export const unMount = () => {
  getContainer()?.remove();
};

export const showHomeInfo = () => {
  getHomeInfo().hidden = false;
};

export const hideHomeInfo = () => {
  getHomeInfo().hidden = true;
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
      <home-info language={language} class="language" hidden></home-info>
    </div>
  );
};
