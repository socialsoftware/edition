import { LdodEdTaxonomy } from './ldod-ve-taxonomy';

const mount = async (lang, ref) => {
  document.querySelector(ref).appendChild(new LdodEdTaxonomy(lang));
};

const unMount = () => document.querySelector('ldod-ed-taxonomy')?.remove();

const path = '/taxonomy/:acrn';

export { mount, unMount, path };
