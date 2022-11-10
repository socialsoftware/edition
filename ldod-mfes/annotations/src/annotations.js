let Annotator;

export default {
  path: '',
  mount: () => {},
  unMount: () => {},
};

export const annotatorService = async ({ interId, referenceNode }) => {
  if (!Annotator) Annotator = (await import('./annotator')).default;
  Annotator({ interId, referenceNode });
};
