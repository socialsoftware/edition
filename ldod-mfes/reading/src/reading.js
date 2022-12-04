import references from './references';
let Reading;

const loadReading = async () => {
  Reading = await import('./readingRouter');
};

export default {
  path: '/reading',
  references,
  mount: async (lang, ref) => {
    if (!Reading) await loadReading();
    await Reading.mount(lang, ref);
  },
  unMount: async () => {
    if (!Reading) await loadReading();
    await Reading.unMount();
  },
};
