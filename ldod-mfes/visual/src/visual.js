const Visual = async (ref) => (await import('./Visual')).default(ref);

export default {
  path: '/visual',
  mount: async (lang, ref) => {
    Visual(ref);
  },
  unMount: async () => {
    document.querySelectorAll('[temporary]').forEach((ele) => ele.remove());
    document.querySelector('div#visual-container')?.remove();
  },
};
