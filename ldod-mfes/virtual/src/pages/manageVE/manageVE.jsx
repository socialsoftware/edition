import { getVirtualEditions4Manage } from '@src/apiRequests.js';
import './LdodManageVE.jsx';

const mount = async (lang, ref) => {
  const virtualEditionsData = await getVirtualEditions4Manage();
  document
    .querySelector(ref)
    .appendChild(
      <ldod-manage-ve
        language={lang}
        virtualEditions={virtualEditionsData}></ldod-manage-ve>
    );
};

const unMount = () => document.querySelector('ldod-manage-ve')?.remove();

const path = '/manage-virtual-editions';

export { mount, unMount, path };
