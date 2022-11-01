import { getVirtualEditions4Manage } from './apiRequests.js';
import './LdodManageVE.jsx';

const mount = async (lang, ref) => {
  getVirtualEditions4Manage()
    .then((data) =>
      document
        .querySelector(ref)
        .appendChild(
          <ldod-manage-ve
            language={lang}
            virtualEditions={data || []}></ldod-manage-ve>
        )
    )
    .catch((error) => console.error(error));
};

const unMount = () => document.querySelector('ldod-manage-ve')?.remove();

const path = '/manage-virtual-editions';

export { mount, unMount, path };
