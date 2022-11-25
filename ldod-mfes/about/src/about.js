let About;

const loadAbout = async () => {
  About = await import('./aboutRouter.jsx');
};

export const aboutReferences = {
  archive: '/about/archive',
  videos: '/about/videos',
  tutorials: '/about/tutorials',
  faq: '/about/faq',
  encoding: '/about/encoding',
  articles: '/about/articles',
  book: '/about/book',
  privacy: '/about/privacy',
  team: '/about/team',
  ack: '/about/acknowledgements',
  contact: '/about/contact',
  copyright: '/about/copyright',
};

export default {
  path: '/about',
  mount: async (lang, ref) => {
    if (!About) await loadAbout();
    await About.mount(lang, ref);
  },
  unMount: async () => {
    if (!About) await loadAbout();
    await About.unMount();
  },
};
