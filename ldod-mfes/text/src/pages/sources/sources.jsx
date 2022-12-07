import { dataProxy } from '../../api-requests.js';
import './LdodSources.jsx';


const mount = async (lang, ref) => {

  const ldodInterSources = document
    .querySelector(ref)
    .appendChild(<ldod-sources language={lang}></ldod-sources>);
  ldodInterSources.interSources = await dataProxy.sources;
  ldodInterSources.toggleAttribute('data', true);
};

const unMount = () => document.querySelector('ldod-sources')?.remove();

const path = '/sources';

export { mount, unMount, path };
