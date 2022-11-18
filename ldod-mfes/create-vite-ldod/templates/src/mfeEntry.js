let mfe;

const loadMfe = async () => {
  // load mfe component
};

export default {
  path: '/mfe-path',
  mount: async (lang, ref) => {
    if (!mfe) await loadMfe();
    // mfe mount call
  },
  unMount: async () => {
    if (!mfe) await loadMfe();
    // mfe unMount call
  },
};
