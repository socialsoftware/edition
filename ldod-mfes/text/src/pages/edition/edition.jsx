import EditionRouter from './edition-router';

const path = '/edition';
const mount = async (lang, ref) => {
  document.querySelector(ref).appendChild(<EditionRouter language={lang} />);
};
const unMount = () =>
  document.querySelector('ldod-router#edition-router')?.remove();

export { path, mount, unMount };
