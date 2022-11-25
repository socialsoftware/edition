let Virtual;

const loadVirtual = async () => {
  Virtual = await import('./virtualRouter.jsx');
};

export const loadVirtualComponents = async () => {
  await import('./Fragment/VirtualNavigation');
  await import('./Fragment/VirtualTranscription');
};

export const virtualReferences = {
  virtualEditions: '/virtual/virtual-editions',
  virtualEdition: (acrn) => `/virtual/edition/acronym/${acrn}`,
  manageVirtualEditions: '/virtual/manage-virtual-editions',
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

if (typeof window === 'object') {
  window.addEventListener('ldod-selectedVE', ({ detail }) => {
    selectedInters = detail.selected
      ? [...selectedInters, detail.name]
      : selectedInters.filter((ed) => ed !== detail.name);
  });
}
