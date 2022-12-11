import textReferences from './references';
let text;

const loadText = async () => {
  if (!text) text = await import('./text-router.jsx');
  return text;
};

export default {
  path: '/text',
  references: textReferences,
  mount: async (lang, ref) => (await loadText()).mount(lang, ref),
  unMount: async () => (await loadText()).unMount(),
};
