export { textReferences } from './textReferences';

let Text;

const loadText = async () => {
  Text = await import('./textRouter.jsx');
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
