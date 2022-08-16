import { user } from '../ManageUsers';
import constants from '../resources/constants.js';

function getConstants(key) {
  return constants[document.querySelector('manage-users').language][key];
}

export default () => {
  return (
    <div>
      <form role="form" class="form">
        <div class="form-floating">
          <input
            id="firstname"
            class="form-control"
            type="text"
            autoComplete="first-name"
            name="firstName"
            value={user()?.firstName}
          />
          <label data-key="firstName">{getConstants('firstName')}</label>
        </div>
        <div class="form-floating">
          <input
            id="lastname"
            class="form-control"
            type="text"
            name="lastName"
            value={user()?.lastName}
          />
          <label data-key="lastName">{getConstants('lastName')}</label>
        </div>
        <div class="form-floating">
          <input type="hidden" value={user()?.userName} name="oldUsername" />
          <input
            class="form-control"
            id="username"
            type="text"
            name="newUsername"
            value={user()?.userName}
          />
          <label for="username" data-key="userName">
            {getConstants('userName')}
          </label>
        </div>
        <div class="form-floating">
          <input
            id="email"
            class="form-control"
            type="text"
            name="email"
            value={user()?.email}
          />
          <label for="email" data-key="email">
            {getConstants('email')}
          </label>
        </div>
        <div class="form-floating">
          <input
            id="newPassword"
            class="form-control"
            type="text"
            name="newPassword"
            placeholder={getConstants('newPassword')}
          />
          <label for="newPassoword" data-key="newPassword">
            {getConstants('newPassword')}
          </label>
        </div>
        <div class="form-flex">
          <div>
            <label data-key="user">{getConstants('user')}</label>
            <div>
              <label class="switch">
                <input name="role_user" type="checkbox" />
                <span class="slider round"></span>
              </label>
            </div>
          </div>
          <div>
            <label data-key="admin">{getConstants('admin')}</label>
            <div>
              <label class="switch">
                <input name="role_admin" type="checkbox" />
                <span class="slider round"></span>
              </label>
            </div>
          </div>
          <div>
            <label data-key="enabled">{getConstants('enabled')}</label>
            <div>
              <label class="switch">
                <input name="enabled" type="checkbox" />
                <span class="slider round"></span>
              </label>
            </div>
          </div>
        </div>
      </form>
    </div>
  );
};
