import { getVirtualEditions } from '@src/apiRequests.js';
import './LdodVirtualEditions.jsx';

const mount = async (lang, ref) => {
  const virtualEditionsData = await getVirtualEditions();
  document
    .querySelector(ref)
    .appendChild(
      <ldod-virtual-editions
        language={lang}
        virtualEditions={virtualEditionsData.virtualEditions}
        user={virtualEditionsData.user}
        selectedVE={
          virtualEditionsData.user?.selectedVE || [
            'LdoD-Twiiter',
            'LdoD-Mallet',
          ]
        }></ldod-virtual-editions>
    );
};

const unMount = () => document.querySelector('ldod-virtual-editions')?.remove();

const path = '/virtual-editions';

export { mount, unMount, path };
