import { getSources } from '@src/apiRequests.js';
import './LdodSources.jsx';

const mount = (lang, ref) => {
  getSources().then((data) => {
    const sources = document.querySelector(`${ref}>ldod-sources`);
    sources.sources = data;
    sources.toggleAttribute('data');
  });
  document
    .querySelector(ref)
    .appendChild(<ldod-sources language={lang}></ldod-sources>);
};
const unMount = () => document.querySelector('ldod-sources')?.remove();

const path = '/sources';

export { mount, unMount, path };
