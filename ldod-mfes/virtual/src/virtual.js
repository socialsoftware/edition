let Virtual;

const loadVirtual = async () => {
  Virtual = await import('./virtualRouter.jsx');
};

export const loadVirtualComponents = async () =>
  await import('./Fragment/VirtualNavigation');

export default {
  path: '/virtual',
  mount: async (lang, ref) => {
    if (!Virtual) await loadVirtual();
    await Virtual.mount(lang, ref);
  },
  unMount: async () => {
    if (!Virtual) await loadVirtual();
    await Virtual.unMount();
  },
};
