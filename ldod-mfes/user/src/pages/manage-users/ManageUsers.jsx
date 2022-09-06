import './ManageUsersComponent.jsx';
import { getUsersList } from '@src/apiRequests.js';

const useState = (initial) => {
  let state = initial;
  const getState = () => state;
  const setState = (newState) => (state = newState);
  return [getState, setState];
};

export const [usersData, setUsersData] = useState();
export const [user, setUser] = useState();

const mount = async (lang, ref) => {
  getUsersList().then((data) => {
    setUsersData(data);
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
