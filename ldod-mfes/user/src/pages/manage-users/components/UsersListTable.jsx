import { changeActiveRequest, removeUserRequest } from '@src/apiRequests.js';
import edit from '@src/resources/icons/edit-primary.svg';
import trash from '@src/resources/icons/trash.svg';
import { setUser, usersData } from '../ManageUsers';
import constants from '../resources/constants.js';
import UpdateModal from './UpdateUserModal.jsx';
import UptadeUserForm from './UpdateUserForm.jsx';
const { Table } = import.meta.env.DEV
  ? await import('shared/table-dev.js')
  : await import('shared/table.js');
const manageUsers = () => document.querySelector('manage-users');
const getLanguage = () => manageUsers().language;

function getConstants(key) {
  return constants[getLanguage()][key];
}

const onChangeActive = async (externalId) => {
  const res = await changeActiveRequest(externalId);
  usersData().userList.forEach((user) => {
    if (user.externalId === externalId) {
      user.active = res.ok;
      return;
    }
  });
  replaceActive(res.ok, externalId);
};

const replaceActive = (active, id) => {
  const newActive = getUsersListActive(active, id);
  document.querySelector(`#active-${id}`).replaceWith(newActive);
};

const onDeleteUser = async ({ target }) => {
  const res = await removeUserRequest(target.dataset.id);
  usersData().userList = res.userList;
  let node = target.parentNode;
  while (node.tagName !== 'TR') node = node.parentNode;
  node.remove();
  document.querySelector('manage-users').updateUsersLength();
};

const onEditUser = async ({ target }) => {
  const user = usersData().userList.find(
    ({ externalId }) => externalId === target.dataset.id
  );
  setUser(user);
  const modal = document.querySelector('manage-users ldod-modal');
  modal.toggleAttribute('show');
  const bodySlot = modal.querySelector("div[slot='body-slot']");
  bodySlot.innerHTML = '';
  bodySlot.appendChild(<UptadeUserForm />);
  bodySlot.querySelectorAll("input[type='checkbox']").forEach((input) => {
    input.checked = input.name.startsWith('role')
      ? user?.listOfRoles.includes(input.name.toUpperCase())
      : user?.enabled;
  });
};

const getUsersListActive = (active, id) => {
  return (
    <div id={`active-${id}`} class="text-center">
      <button
        id={`button-active-${id}`}
        tooltip-ref={`button-active-${id}`}
        class={`btn ${active ? 'btn-success' : 'btn-secondary'} btn-sm`}
        onClick={() => onChangeActive(id)}>
        <span data-key={String(active).toUpperCase()}>
          {getConstants(String(active).toUpperCase())}
        </span>
      </button>
      <ldod-tooltip
        placement="top"
        data-ref={`[tooltip-ref='button-active-${id}']`}
        data-tooltipkey="toggleActiveMode"
        content={getConstants('toggleActiveMode')}></ldod-tooltip>
    </div>
  );
};

const getUsersListActions = (id) => {
  return (
    <div class="text-center">
      <img
        id={`edit-icon-${id}`}
        tooltip-ref={`edit-icon-${id}`}
        data-id={id}
        src={edit}
        class="btn-icon action"
        onClick={onEditUser}
      />
      <img
        id={`trash-icon-${id}`}
        tooltip-ref={`trash-icon-${id}`}
        data-id={id}
        src={trash}
        class="btn-icon action"
        onClick={onDeleteUser}
      />
      <ldod-tooltip
        data-ref={`[tooltip-ref='edit-icon-${id}']`}
        data-tooltipkey="edit"
        placement="top"
        content={getConstants('edit')}></ldod-tooltip>
      <ldod-tooltip
        data-ref={`[tooltip-ref='trash-icon-${id}']`}
        data-tooltipkey="remove"
        placement="top"
        content={getConstants('remove')}></ldod-tooltip>
    </div>
  );
};

export default () => {
  return (
    <div>
      <UpdateModal />
      <div id="userList" class="row">
        <ldod-table
          id="users-list-table"
          classes="table table-responsive-sm table-striped table-bordered"
          headers={constants.usersListHeaders}
          data={usersData().userList.map((user) => ({
            ...user,
            enabled: (
              <div data-key={String(user.enabled).toUpperCase()}>
                {getConstants(String(user.enabled).toUpperCase())}
              </div>
            ),
            active: getUsersListActive(user.active, user.externalId),
            actions: getUsersListActions(user.externalId),
            search: Object.values(user).reduce((prev, curr) => {
              return prev.concat(String(curr), ',');
            }, ''),
          }))}
          language={getLanguage()}
          constants={constants}
          data-searchkey="externalId"></ldod-table>
      </div>
    </div>
  );
};
