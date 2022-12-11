import './ldod-encoding.jsx';

const mount = (lang, ref) => {
  document
    .querySelector(ref)
    .appendChild(<ldod-encoding language={lang} title></ldod-encoding>);
};
const unMount = () => document.querySelector('ldod-encoding')?.remove();

const path = '/encoding';
export { mount, unMount, path };
