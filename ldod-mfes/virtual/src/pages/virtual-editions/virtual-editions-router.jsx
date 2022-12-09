import { index } from './pages/index/virtual-editions';

const path = '/virtual-editions';

const mount = async (lang, ref) => {
  document
    .querySelector(ref)
    .appendChild(<VirtualEditionsRouter language={lang} />);
};
const unMount = () => {
  document.querySelector('ldod-router#virtualEditions-router')?.remove();
};
export { path, mount, unMount };

export const isDev = () => import.meta.env.DEV;

const routes = {
  '/:veId/game/:gameId': async () => await import('./pages/game/ve-class-game'),
};

const VirtualEditionsRouter = ({ language }) => (
  <ldod-router
    id="virtualEditions-router"
    base={isDev() ? '' : import.meta.env.VITE_BASE}
    route="/virtual/virtual-editions"
    index={index}
    routes={routes}
    language={language}></ldod-router>
);
