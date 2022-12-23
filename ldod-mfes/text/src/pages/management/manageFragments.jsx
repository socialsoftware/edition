import './ldod-manage-fragments.jsx';

const mount = async (lang, ref) => {
  document
    .querySelector(ref)
    .appendChild(
      <ldod-manage-fragments language={lang}></ldod-manage-fragments>
    );
};

const unMount = () => document.querySelector('ldod-manage-fragments')?.remove();

const path = '/manage-fragments';

export { mount, unMount, path };
