await import('./Components/Navbar/Navbar.js').catch((error) => {
  if (error.code && error.code !== 'ERR_MODULE_NOT_FOUND') throw error;
});

let Home;

const loadHome = async () => {
  Home = await import('./Components/HomeView/Home.js');
};

export default {
  path: '/',
  mount: async (lang, ref) => {
    if (!Home) await loadHome();
    await Home.mount(lang, ref);
  },
  unMount: async () => {
    if (!Home) await loadHome();
    await Home.unMount();
  },
};
