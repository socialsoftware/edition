import { getFragment } from '@src/apiRequests.js';
import { LdodFragment } from './LdodFragment.jsx';
import { getNewInter } from './utils.js';
import { isDev } from '../../textRouter.jsx';

const { loadVirtualComponents } = isDev()
  ? await import('virtual/virtual-dev.js')
  : await import('virtual/virtual.js');

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

  await loadVirtualComponents();
};

const unMount = () => document.querySelector('ldod-fragment')?.remove();

const path = '/fragment/:xmlId/inter/:urlId';

export { mount, unMount, path };
