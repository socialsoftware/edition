import './ldod-ack.jsx';

const mount = (lang, ref) => {
  document.querySelector(ref).append(<ldod-ack language={lang}></ldod-ack>);
};
const unMount = () => {
  document.querySelector('ldod-ack')?.remove();
};

const path = '/acknowledgements';

export { mount, unMount, path };
