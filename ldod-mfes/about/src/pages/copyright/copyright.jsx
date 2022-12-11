import './ldod-copyright.jsx';

const mount = (lang, ref) => {
  document
    .querySelector(ref)
    .appendChild(<ldod-copyright language={lang} title></ldod-copyright>);
};
const unMount = () => {
  document.querySelector('ldod-copyright')?.remove();
};

const path = '/copyright';
export { mount, unMount, path };
