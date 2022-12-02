import { loadAndAssignUsers } from './ManageUsersComponent.jsx';

const mount = async (lang, ref) => {
  document
    .querySelector(ref)
    .appendChild(<manage-users language={lang}></manage-users>);
  const node = document.querySelector(`${ref}>manage-users`);
  loadAndAssignUsers(node);
};
const unMount = () => document.querySelector('manage-users')?.remove();

const path = '/manage-users';

export { mount, unMount, path };
