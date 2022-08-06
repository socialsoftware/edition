import { user } from '../ManageUsers';
import constants from '../resources/constants.js';

function getConstants(key) {
  return constants[document.querySelector('manage-users').language][key];
}

export default () => {
  return (
    <div>
      <form role="form" class="form">
        <div class="form-control form-flex">
          <label data-key="firstName">{getConstants('firstName')}</label>
          <input
            key="firstname"
            type="text"
            autoComplete="first-name"
            name="firstName"
            value={user()?.firstName}
          />
        </div>
        <div class="form-control form-flex">
          <label data-key="lastName">{getConstants('lastName')}</label>
          <input
            key="lastname"
            type="text"
            name="lastName"
            value={user()?.lastName}
          />
        </div>
        <div class="form-control form-flex">
          <input type="hidden" value={user()?.userName} name="oldUsername" />
          <label data-key="userName">{getConstants('userName')}</label>
          <input
            key="userName"
            type="text"
            name="newUsername"
            value={user()?.userName}
          />
        </div>
        <div class="form-control form-flex">
          <label data-key="email">{getConstants('email')}</label>
          <input key="email" type="text" name="email" value={user()?.email} />
        </div>
        <div class="form-control form-flex">
          <label data-key="user">{getConstants('user')}</label>
          <div>
            <label class="switch">
              <input name="role_user" type="checkbox" />
              <span class="slider round"></span>
            </label>
          </div>
        </div>
        <div class="form-control form-flex">
          <label data-key="admin">{getConstants('admin')}</label>
          <div>
            <label class="switch">
              <input name="role_admin" type="checkbox" />
              <span class="slider round"></span>
            </label>
          </div>
        </div>
        <div class="form-control form-flex">
          <label data-key="enabled">{getConstants('enabled')}</label>
          <div>
            <label class="switch">
              <input name="enabled" type="checkbox" />
              <span class="slider round"></span>
            </label>
          </div>
        </div>
        <div class="form-control form-flex">
          <label data-key="newPassword">{getConstants('newPassword')}</label>
          <input key="newPassword" type="text" name="newPassword" />
        </div>
      </form>
    </div>
  );
};
