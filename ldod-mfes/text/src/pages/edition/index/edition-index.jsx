import './ldod-edition';

const path = '/';
const mount = async (lang, ref) => {
  document
    .querySelector(ref)
    .appendChild(<ldod-edition language={lang}></ldod-edition>);
};
const unMount = () => document.querySelector('ldod-edition')?.remove();

export const index = () => ({ mount, unMount, path });
