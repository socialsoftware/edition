import 'shared/router.js';
import style from './style.css?inline';

export const isDev = () => import.meta.env.DEV;
const SOCIAL_SELECTOR = 'div#social-container';

const routes = {
  '/twitter-citations': async () =>
    await import('./pages/citations/citations.jsx'),
  '/manage-tweets': async () =>
    await import('./pages/manage-tweets/manage-tweets.jsx'),
};

export const mount = (lang, ref) => {
  document.querySelector(ref).appendChild(<SocialRouter language={lang} />);
};

export const unMount = () => {
  document.querySelector(SOCIAL_SELECTOR)?.remove();
};

export const SocialRouter = ({ language }) => {
  return (
    <div id="social-container">
      <style>{style}</style>
      <div class="container">
        <ldod-router
          id="social-router"
          base={isDev() ? '' : import.meta.env.VITE_BASE}
          route="/social"
          routes={routes}
          language={language}></ldod-router>
      </div>
    </div>
  );
};
