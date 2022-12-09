import "./manage-users-component"
const mount = async (lang, ref) => {
  document
    .querySelector(ref)
    .appendChild(<manage-users language={lang}></manage-users>);
};
const unMount = () => document.querySelector('manage-users')?.remove();

const path = '/manage-users';

export { mount, unMount, path };
