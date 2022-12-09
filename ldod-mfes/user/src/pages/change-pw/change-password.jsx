import './change-password-component.jsx';

const mount = (lang, ref) => {
  document
    .querySelector(ref)
    .appendChild(<change-password language={lang}></change-password>);
};
const unMount = () => document.querySelector('change-password')?.remove();

const path = '/change-password';

export { mount, unMount, path };
