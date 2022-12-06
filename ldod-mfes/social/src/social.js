import references from './references.js';
let Social;
const loadSocial = async () => {
  Social = await import('./social-router.jsx');
};

export default {
  path: '/social',
  references,
  mount: async (lang, ref) => {
    if (!Social) await loadSocial();
    await Social.mount(lang, ref);
  },
  unMount: async () => {
    if (!Social) await loadSocial();
    await Social.unMount();
  },
};
