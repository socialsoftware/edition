import EditionRouter from './EditionRouter';

const path = '/edition';
const mount = async (lang, ref) => {
  document.querySelector(ref).appendChild(<EditionRouter language={lang} />);
};
const unMount = () => document.querySelector('ldod-edition')?.remove();

export { path, mount, unMount };
