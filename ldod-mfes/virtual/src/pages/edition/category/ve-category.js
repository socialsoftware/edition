import { LdodVeCategory } from './ldod-ve-category';

const mount = async (lang, ref) => {
  document.querySelector(ref).appendChild(new LdodVeCategory(lang));
};

const unMount = () => document.querySelector('ldod-ve-category')?.remove();

const path = '/acronym/:acrn/category/:cat';

export { mount, unMount, path };
