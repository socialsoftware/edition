let home;

if (typeof window === 'object') {
  import('./components/navbar/ldod-navbar.js');
  home = await import('./components/home/home.js');
}

export default {
  path: '/',
  mount: async (lang, ref) => await home.mount(lang, ref),
  unMount: async () => await home.unMount(),
};
