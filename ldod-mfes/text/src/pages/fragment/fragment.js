import { getFragment } from '@src/apiRequests.js';
import { LdodFragment } from './LdodFragment.jsx';
import { getNewInter, isVirtualInter } from './utils.js';
import { isDev } from '../../textRouter.jsx';

const virtual =
  window?.mfes.includes('virtual') && isDev()
    ? await import('virtual/virtual-dev.js').catch((e) => console.error(e))
    : await import('virtual').catch((e) => console.error(e));

const mount = async (lang, ref) => {
  const { xmlId, urlId } = history.state;
  const data = xmlId
    ? urlId && !isVirtualInter(urlId)
      ? await getNewInter(xmlId, urlId)
      : await getFragment(xmlId)
    : '';
  document
    .querySelector(ref)
    .appendChild(new LdodFragment(lang, data, xmlId, urlId));

  await virtual?.loadVirtualComponents();
};

const unMount = () => document.querySelector('ldod-fragment')?.remove();

const path = '/fragment/:xmlId/inter/:urlId';

export { mount, unMount, path };
