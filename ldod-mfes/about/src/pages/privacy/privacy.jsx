import './ldod-privacy.jsx';

const mount = (lang, ref) => {
  document
    .querySelector(ref)
    .appendChild(<ldod-privacy language={lang} title></ldod-privacy>);
};
const unMount = () => document.querySelector('ldod-privacy')?.remove();

const path = '/privacy';
export { mount, unMount, path };
