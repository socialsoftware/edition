import { getFragment } from '@src/apiRequests.js';
import { LdodFragment } from './LdodFragment.jsx';
import { getNewInter } from './utils.js';

const mount = async (lang, ref) => {
  const { xmlId, urlId } = history.state;
  const data = xmlId
    ? urlId
      ? await getNewInter(xmlId, urlId)
      : await getFragment(xmlId)
    : '';
  document
    .querySelector(ref)
    .appendChild(new LdodFragment(lang, data, xmlId, urlId));
};

const unMount = () => document.querySelector('ldod-fragment')?.remove();

const path = '/fragment/:xmlId/inter/:urlId';

export { mount, unMount, path };
