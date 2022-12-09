export const isDev = () => import.meta.env.DEV;

const routes = {
  '/acronym/:acrn': async () => await import('./virtual-edition/virtual-edition'),
  '/acronym/:acrn/category/:cat': async () =>
    await import('./category/ve-category'),
  '/acronym/:acrn/taxonomy': async () => await import('./taxonomy/ve-taxonomy'),
  '/user': async () => await import('./user/ve-user'),
};

const VirtualEditionRouter = ({ language }) => (
  <ldod-router
    id="virtual-edition-router"
    base={isDev() ? '' : import.meta.env.VITE_BASE}
    route="/virtual/edition"
    routes={routes}
    language={language}></ldod-router>
);

const path = '/edition';
const mount = async (lang, ref) => {
  document
    .querySelector(ref)
    .appendChild(<VirtualEditionRouter language={lang} />);
};
const unMount = () =>
  document.querySelector('ldod-router#virtual-edition-router')?.remove();

export { path, mount, unMount };
