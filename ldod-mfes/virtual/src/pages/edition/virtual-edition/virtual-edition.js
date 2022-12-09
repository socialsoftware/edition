import { LdodVirtualEdition } from './ldod-virtual-edition';

const mount = async (lang, ref) => {
  document.querySelector(ref).appendChild(new LdodVirtualEdition(lang));
};

const unMount = () => document.querySelector('ldod-virtual-edition')?.remove();

const path = '/acronym/:acrn';

export { mount, unMount, path };
