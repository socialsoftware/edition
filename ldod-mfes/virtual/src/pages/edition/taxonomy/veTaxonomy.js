import { LdodVeTaxonomy } from './LdodVeTaxonomy';

const mount = async (lang, ref) => {
  document.querySelector(ref).appendChild(new LdodVeTaxonomy(lang));
};

const unMount = () => document.querySelector('ldod-ve-taxonomy')?.remove();

const path = '/taxonomy/:acrn';

export { mount, unMount, path };
