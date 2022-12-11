import './ldod-videos.jsx';

const mount = (lang, ref) => {
  document
    .querySelector(ref)
    .appendChild(<ldod-videos language={lang} title></ldod-videos>);
};
const unMount = () => document.querySelector('ldod-videos')?.remove();

const path = '/videos';
export { mount, unMount, path };
