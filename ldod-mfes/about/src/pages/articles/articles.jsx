import './ldod-articles.jsx';

const mount = (lang, ref) => {
  document
    .querySelector(ref)
    .appendChild(<ldod-articles language={lang}></ldod-articles>);
};
const unMount = () => document.querySelector('ldod-articles')?.remove();

const path = '/articles';

export { mount, unMount, path };
