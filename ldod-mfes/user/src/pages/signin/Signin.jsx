import './SigninComponent.jsx';

const mount = (lang, ref) => {
  document.querySelector(ref).appendChild(<sign-in language={lang}></sign-in>);
};
const unMount = () => document.querySelector('sign-in')?.remove();

const path = '/signin';

export { mount, unMount, path };
