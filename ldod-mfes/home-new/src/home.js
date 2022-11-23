let home;

const loadMfe = async () => {
  // load mfe component
};

export default {
  path: '/home-new',
  mount: async (lang, ref) => {
    if (!home) await loadMfe();
    // mfe mount call
  },
  unMount: async () => {
    if (!home) await loadMfe();
    // mfe unMount call
  },
};
