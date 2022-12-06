import './ldod-manage-tweets.jsx';

const mount = (lang, ref) => {
  document
    .querySelector(ref)
    .appendChild(<ldod-manage-tweets language={lang}></ldod-manage-tweets>);
};
const unMount = () => document.querySelector('ldod-citations')?.remove();

const path = '/manage-tweets';

export { mount, unMount, path };
