import './ldod-book.jsx';

const mount = (lang, ref) => {
  document
    .querySelector(ref)
    .appendChild(<ldod-book language={lang}></ldod-book>);
};
const unMount = () => document.querySelector('ldod-book')?.remove();

const path = '/book';
export { mount, unMount, path };
