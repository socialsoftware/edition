import references from './references.js';
let social;
const loadSocial = async () => {
  if (!social) social = await import('./social-router.jsx');
  return social;
};

export default {
  path: '/social',
  references,
  mount: async (lang, ref) => (await loadSocial()).mount(lang, ref),
  unMount: async () => (await loadSocial()).unMount(),
};
