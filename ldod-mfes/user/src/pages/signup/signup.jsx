import './signup-component.jsx';

const mount = (lang, ref) => {
  document.querySelector(ref).appendChild(<sign-up language={lang}></sign-up>);
};
const unMount = () => document.querySelector('sign-up')?.remove();

const path = '/signup';

export { mount, unMount, path };
