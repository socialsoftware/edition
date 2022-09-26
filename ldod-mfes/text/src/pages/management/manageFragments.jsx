import { getAdminFragments } from '@src/apiRequests.js';
import './LdodManageFragments.jsx';

const mount = (lang, ref) => {
  getAdminFragments().then((data) => {
    const manageFragments = document.querySelector(
      `${ref}>ldod-manage-fragments`
    );
    manageFragments.fragments = data;
    manageFragments.toggleAttribute('data');
  });
  document
    .querySelector(ref)
    .appendChild(
      <ldod-manage-fragments language={lang}></ldod-manage-fragments>
    );
};
const unMount = () => document.querySelector('ldod-manage-fragments')?.remove();

const path = '/manage-fragments';

export { mount, unMount, path };
