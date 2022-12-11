import './ldod-tutorials.jsx';

const mount = (lang, ref) => {
  document
    .querySelector(ref)
    .appendChild(<ldod-tutorials language={lang} title></ldod-tutorials>);
};
const unMount = () => document.querySelector('ldod-tutorials')?.remove();

const path = '/tutorials';
export { mount, unMount, path };
