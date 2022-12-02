export let selectedInters = ['LdoD-Arquivo', 'LdoD-Mallet', 'LdoD-Twitter'];

if (typeof window === 'object') {
  window.addEventListener('ldod-selectedVE', ({ detail }) => {
    selectedInters = detail.selected
      ? [...selectedInters, detail.name]
      : selectedInters.filter((ed) => ed !== detail.name);
  });
}
