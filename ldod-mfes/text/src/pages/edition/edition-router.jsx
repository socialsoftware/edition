import { index } from './index/edition-index';
export const isDev = () => import.meta.env.DEV;

const routes = {
  '/acronym': async () => await import('./acronym/edition-acrn'),
};

export default ({ language }) => (
  <ldod-router
    id="edition-router"
    base={isDev() ? '' : import.meta.env.VITE_BASE}
    route="/text/edition"
    index={index}
    routes={routes}
    language={language}></ldod-router>
);
