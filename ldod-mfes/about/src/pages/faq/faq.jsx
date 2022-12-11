import './ldod-faq.jsx';

const mount = (lang, ref) => {
  document
    .querySelector(ref)
    .appendChild(<ldod-faq language={lang} title></ldod-faq>);
};
const unMount = () => document.querySelector('ldod-faq')?.remove();

const path = '/faq';
export { mount, unMount, path };
