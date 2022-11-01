let Virtual;

const loadVirtual = async () => {
  Virtual = await import('./virtualRouter.jsx');
};

export const loadVirtualComponents = async () => {
  await import('./Fragment/VirtualNavigation');
  await import('./Fragment/VirtualTranscription');
};

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

export let selectedInters = ['LdoD-Arquivo', 'LdoD-Mallet', 'LdoD-Twitter'];
(async () => {
  window.addEventListener('ldod-selectedVE', ({ detail }) => {
    selectedInters = detail.selected
      ? [...selectedInters, detail.name]
      : selectedInters.filter((ed) => ed !== detail.name);
  });
})().catch((error) => error);
