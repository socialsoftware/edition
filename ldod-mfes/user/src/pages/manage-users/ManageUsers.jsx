import './ManageUsersComponent.jsx';
import { getUsersList } from '@src/apiRequests.js';

export const loadAndAssignUsers = (node) => {
  getUsersList().then((data) => {
    node.toggleAttribute('data', false);
    node.usersData = data;
    node.toggleAttribute('data', true);
  });
};

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
