import './ldod-team.jsx';

const mount = (lang, ref) => {
  document
    .querySelector(ref)
    .appendChild(<ldod-team language={lang} title></ldod-team>);
};
const unMount = () => document.querySelector('ldod-team')?.remove();

const path = '/team';
export { mount, unMount, path };
