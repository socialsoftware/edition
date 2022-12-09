import { LdodUserVe } from './ldod-user-ve';

const mount = async (lang, ref) => {
  document.querySelector(ref).appendChild(new LdodUserVe(lang));
};

const unMount = () => document.querySelector('ldod-user-ve')?.remove();

const path = '/user/:username';

export { mount, unMount, path };
