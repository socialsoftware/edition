import { useState } from 'shared/utils.js';
import { getInterSources } from '@src/apiRequests.js';
import './LdodSources.jsx';

export const [getSources, setSources] = useState();

const mount = async (lang, ref) => {
  document
    .querySelector(ref)
    .appendChild(<ldod-sources language={lang}></ldod-sources>);
  const ldodInterSources = document.querySelector(`${ref}>ldod-sources`);
  if (!getSources()) setSources(await getInterSources());
  ldodInterSources.interSources = getSources();
  ldodInterSources.toggleAttribute('data', true);
};

const unMount = () => document.querySelector('ldod-sources')?.remove();

const path = '/sources';

export { mount, unMount, path };
