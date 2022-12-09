import references from './references';

let Search;

const loadSearch = async () => {
  Search = await import('./SearchRouter');
};

if (typeof window !== "undefined") {
  import('./pages/search-simple/LdodSearchSimple');
}

export default {
  path: '/search',
  references,
  mount: async (lang, ref) => {
    if (!Search) await loadSearch();
    await Search.mount(lang, ref);
  },
  unMount: async () => {
    if (!Search) await loadSearch();
    await Search.unMount();
  },
};
