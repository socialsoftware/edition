let Text;

const loadText = async () => {
  Text = await import('./textRouter.jsx');
};

export const textReferences = {
  fragments: '/text/fragments',
  sources: '/text/sources',
  editions: '/text/edition',
  edition: (acrn) => `/text/edition/acronym/${acrn}`,
  fragment: (xmlId) => `/text/fragment/${xmlId}`,
  fragmentInter: (xmlId, urlId) => `/text/fragment/${xmlId}/inter/${urlId}`,
  manageFragments: '/text/manage-fragments',
};

export default {
  path: '/text',
  mount: async (lang, ref) => {
    if (!Text) await loadText();
    await Text.mount(lang, ref);
  },
  unMount: async () => {
    if (!Text) await loadText();
    await Text.unMount();
  },
};
