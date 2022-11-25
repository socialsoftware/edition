let Social;
const loadSocial = async () => {
  Social = await import('./socialRouter.jsx');
};

export const socialReferences = {
  twitterCitations: '/social/twitter-citations',
  manageTweets: '/social/manage-tweets',
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
