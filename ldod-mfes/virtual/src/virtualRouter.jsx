import 'shared/router.js';
import style from './style.css?inline';
export const isDev = () => import.meta.env.DEV;

const VIRTUAL_SELECTOR = 'div#virutalContainer';

const routes = {
  '/virtual-editions': async () =>
    await import('./pages/virtualEditions/virtualEditions.jsx'),
  '/manage-virtual-editions': async () =>
    await import('./pages/manageVE/manageVE.jsx'),
};

export const mount = (lang, ref) => {
  document.querySelector(ref).appendChild(<VirtualRouter language={lang} />);
};

export const unMount = () => {
  document.querySelector(VIRTUAL_SELECTOR)?.remove();
};

export const VirtualRouter = ({ language }) => {
  return (
    <div id="virutalContainer">
      <style>{style}</style>
      <div class="container">
        <ldod-router
          id="virtual-router"
          base={isDev() ? '' : import.meta.env.VITE_BASE}
          route="/virtual"
          routes={routes}
          language={language}></ldod-router>
      </div>
    </div>
  );
};
