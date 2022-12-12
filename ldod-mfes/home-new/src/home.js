let home;

const loadMfe = async () => {
  if (!home) home = await import('./components/navbar/navbar.jsx');
  return home;
};

export default {
  path: '/home-new',
  mount: async (lang, ref) => (await loadMfe()).mount(lang, ref),
  unMount: async () => (await loadMfe()).unMount(lang, ref),
};
