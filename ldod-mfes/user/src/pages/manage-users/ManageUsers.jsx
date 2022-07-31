import './ManageUsersComponent.jsx';
import { getUsersList } from '../../apiRequests.js';
const mount = async (lang, ref) => {
  getUsersList().then((data) => {
    const manageUsers = document.querySelector(`${ref}>manage-users`);
    manageUsers.usersData = data;
    manageUsers.setAttribute('data', '');
  });
  document
    .querySelector(ref)
    .appendChild(<manage-users language={lang}></manage-users>);
};
const unMount = () => document.querySelector('manage-users')?.remove();

const path = '/manage-users';

export { mount, unMount, path };
