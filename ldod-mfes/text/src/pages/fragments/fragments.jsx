
import { dataProxy } from '@src/apiRequests.js';
import './LdodFragments.jsx';


const mount = async (lang, ref) => {
  const encodedFragments = document
    .querySelector(ref)
    .appendChild(<ldod-fragments language={lang}></ldod-fragments>);
  encodedFragments.fragments = await dataProxy.fragments;
  encodedFragments.toggleAttribute('data', true);
};

const unMount = () => document.querySelector('ldod-fragments')?.remove();

const path = '/fragments';

export { mount, unMount, path };
