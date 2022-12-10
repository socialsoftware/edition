let annotator;

export default {
  path: '',
  mount: () => {},
  unMount: () => {},
  bootstrap: async ({ interId, referenceNode }) => {
    if (!annotator) annotator = (await import('./annotator')).default;
    annotator({ interId, referenceNode });
  },
};

export const annotatorService = async ({ interId, referenceNode }) => {
  if (!annotator) annotator = (await import('./annotator')).default;
  annotator({ interId, referenceNode });
};
