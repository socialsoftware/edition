let router;

const loadRouter = async () => {
  router = await import('./jsx-transpiled/AboutRouter.js');
};

export default {
  path: '/about',
  mount: async (lang, ref) => {
    await loadRouter();
    router.mount(lang, ref);
  },
  unMount: async () => {
    if (!router) await loadRouter();
    router.unMount();
  },
};
