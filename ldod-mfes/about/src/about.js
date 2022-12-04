import references from './references.js';
let About;

const loadAbout = async () => {
  About = await import('./aboutRouter.jsx');
};

export default {

  path: '/about',
  references,
  mount: async (lang, ref) => {
    if (!About) await loadAbout();
    await About.mount(lang, ref);
  },
  unMount: async () => {
    if (!About) await loadAbout();
    await About.unMount();
  },
};
