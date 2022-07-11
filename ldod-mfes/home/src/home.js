await import('./Components/Navbar/Navbar.js').catch((e) => e);

let Home;

const loadHome = async () => {
  Home = await import('./Components/HomeView/Home.js');
};

export default {
  path: '/',
  mount: async (lang, ref) => {
    if (!Home) await loadHome();
    Home.mount(lang, ref);
  },
  unMount: async () => {
    if (!Home) await loadHome();
    Home.unMount();
  },
};
