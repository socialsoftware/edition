import './LdodConduct.jsx';

const mount = (lang, ref) => {
  document
    .querySelector(ref)
    .appendChild(<ldod-conduct language={lang} title></ldod-conduct>);
};
const unMount = () => document.querySelector('ldod-conduct')?.remove();

const path = '/conduct';
export { mount, unMount, path };
