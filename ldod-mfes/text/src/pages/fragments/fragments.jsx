import { useState } from 'shared/utils.js';
import { getFragments } from '@src/apiRequests.js';
import './LdodFragments.jsx';

export const [getFrags, setFrags] = useState();

const mount = async (lang, ref) => {
  document
    .querySelector(ref)
    .appendChild(<ldod-fragments language={lang}></ldod-fragments>);
  const encodedFragments = document.querySelector(`${ref}>ldod-fragments`);
  if (!getFrags()) setFrags(await getFragments());
  encodedFragments.fragments = getFrags();
  encodedFragments.toggleAttribute('data', true);
};

const unMount = () => document.querySelector('ldod-fragments')?.remove();

const path = '/fragments';

export { mount, unMount, path };
