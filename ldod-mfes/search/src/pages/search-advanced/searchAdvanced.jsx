import './LdodSearchAdvanced';

const mount = async (lang, ref) => {
  document
    .querySelector(ref)
    .appendChild(<ldod-search-adv language={lang}></ldod-search-adv>);
};

const unMount = () => document.querySelector('ldod-search-adv')?.remove();

const path = '/search-advanced';

export { mount, unMount, path };
