import { getFragments } from '@src/apiRequests.js';
import './LdodFragments.jsx';

const mount = (lang, ref) => {
  getFragments().then((data) => {
    const fragments = document.querySelector(`${ref}>ldod-fragments`);
    fragments.fragments = data;
    fragments.toggleAttribute('data');
  });
  document
    .querySelector(ref)
    .appendChild(<ldod-fragments language={lang}></ldod-fragments>);
};
const unMount = () => document.querySelector('ldod-fragments')?.remove();

const path = '/fragments';

export { mount, unMount, path };
