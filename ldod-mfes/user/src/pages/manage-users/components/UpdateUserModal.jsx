import constants from '../resources/constants.js';
import { updateUserRequest } from '@src/apiRequests.js';

const getManageUsers = () => document.querySelector('manage-users');
function getConstants(key) {
  return constants[getManageUsers().language][key];
}
const onSaveChanges = async (rootElement) => {
  const usersData = rootElement.usersData;

  const userUpdated = Array.from(
    document.querySelector('ldod-modal form')
  ).reduce((prev, { name, value, type, checked }) => {
    if (type === 'checkbox') {
      let key = name;
      if (name.startsWith('role_')) key = name.split('role_')[1];
      prev[key] = checked;
      return prev;
    }
    prev[name] = value;
    return prev;
  }, {});

  updateUserRequest(userUpdated).then((data) => {
    usersData.userList = data.userList;
    rootElement
      .querySelector('ldod-modal#user-updateUserModal')
      .toggleAttribute('show');
    rootElement.render();
  });
};

export default ({ node }) => {
  return (
    <ldod-modal dialog-class="modal-lg" id="user-updateUserModal">
      <span data-key="editUserHeader" slot="header-slot">
        {getConstants('editUserHeader')}
      </span>
      <div slot="body-slot"></div>
      <div slot="footer-slot">
        <button
          class="btn btn-primary"
          onClick={() => onSaveChanges(node)}
          data-key="update">
          {getConstants('update')}
        </button>
      </div>
    </ldod-modal>
  );
};
