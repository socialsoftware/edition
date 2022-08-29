let Social;
const loadSocial = async () => {
  Social = await import('./socialRouter.jsx');
};

export default {
  path: '/social',
  mount: async (lang, ref) => {
    if (!Social) await loadSocial();
    await Social.mount(lang, ref);
  },
  unMount: async () => {
    if (!Social) await loadSocial();
    await Social.unMount();
  },
};
