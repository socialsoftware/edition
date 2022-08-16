import './LdodArchive.jsx';

const mount = (lang, ref) => {
  document
    .querySelector(ref)
    .appendChild(<ldod-archive language={lang}></ldod-archive>);
};
const unMount = () => document.querySelector('ldod-archive')?.remove();

const path = '/archive';

export { mount, unMount, path };
