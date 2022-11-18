if (typeof window === 'object') {
  import('./Components/Navbar/Navbar.js');
  import('./Components/HomeView/HomeInfo.js');
}

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
