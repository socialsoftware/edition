import { getSourceList } from '@src/apiRequests.js';
import './LdodSourceList.jsx';

const mount = (lang, ref) => {
  getSourceList().then((data) => {
    const sourceList = document.querySelector(`${ref}>ldod-source-list`);
    sourceList.sources = data;
    sourceList.toggleAttribute('data');
  });
  document
    .querySelector(ref)
    .appendChild(<ldod-source-list language={lang}></ldod-source-list>);
};
const unMount = () => document.querySelector('ldod-source-list')?.remove();

const path = '/source-list';

export { mount, unMount, path };
