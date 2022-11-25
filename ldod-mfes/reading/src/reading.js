let Reading;

export const readingReferences = {
  index: '/reading',
  editionInterPath: (xmlId, urlId) =>
    `/reading/fragment/${xmlId}/inter/${urlId}`,
};

const loadReading = async () => {
  Reading = await import('./readingRouter');
};

export default {
  path: '/reading',
  mount: async (lang, ref) => {
    if (!Reading) await loadReading();
    await Reading.mount(lang, ref);
  },
  unMount: async () => {
    if (!Reading) await loadReading();
    await Reading.unMount();
  },
};
