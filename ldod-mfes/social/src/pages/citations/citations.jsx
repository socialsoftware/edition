import './ldod-citations';

const mount = (lang, ref) => {
  document
    .querySelector(ref)
    .appendChild(<ldod-citations language={lang}></ldod-citations>);
};
const unMount = () => document.querySelector('ldod-citations')?.remove();

const path = '/citations';

export { mount, unMount, path };
