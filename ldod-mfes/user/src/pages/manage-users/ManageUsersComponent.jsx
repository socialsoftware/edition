import style from './manageUsersStyle.css?inline';
import 'shared/modal.js';
import UsersTable from './components/UsersListTable.jsx';
import constants from './resources/constants.js';
import UsersTitle from './components/UsersTitle';
import AdminModeButton from './components/AdminModeButton';
import DeleteSessionsButton from './components/DeleteSessionsButton';
import SessionsListTable from './components/SessionsListTable';
import 'shared/buttons.js';

import.meta.env.DEV
  ? await import('shared/table-dev.js')
  : await import('shared/table.js');

async function loadToolip() {
  await import('shared/tooltip.js');
}

export class ManageUsers extends HTMLElement {
  constructor() {
    super();
  }

  static get observedAttributes() {
    return ['data', 'language'];
  }

  get language() {
    return this.getAttribute('language');
  }

  get usersLength() {
    return this.usersData ? this.usersData.userList.length : 'loading';
  }

  getMode() {
    return this.usersData.ldoDAdmin ? 'admin' : 'user';
  }

  setMode(mode) {
    this.usersData.ldoDAdmin = mode;
  }

  getConstants(key) {
    return constants[this.language][key];
  }

  connectedCallback() {
    this.render();
  }

  attributeChangedCallback(name, oldV, newV) {
    this.handlers[name](oldV, newV);
  }
  disconnectedCallback() {}

  addEventListeners() {
    this.querySelectorAll('[tooltip-ref]').forEach((tooltipped) => {
      tooltipped.parentNode.addEventListener('mouseover', loadToolip);
    });
  }

  render() {
    this.innerHTML = '';
    this.appendChild(<style>{style}</style>);
    this.appendChild(this.getComponent());
  }

  handlers = {
    language: (oldV, newV) => {
      if (oldV && newV !== oldV) this.handleChangeLanguage();
    },
    data: () => {
      this.render();
      this.addEventListeners();
    },
  };

  handleChangeLanguage() {
    this.querySelectorAll('[data-key').forEach((ele) => {
      ele.hasAttribute('title') &&
        ele.setAttribute('title', this.getConstants(ele.dataset.key));
      ele.textContent = this.getConstants(ele.dataset.key);
    });
    this.querySelectorAll('[data-tooltipkey]').forEach((ele) => {
      ele.setAttribute('content', this.getConstants(ele.dataset.tooltipkey));
    });
    this.querySelectorAll('[dynamic]').forEach((ele) => {
      ele.textContent = this.getConstants(ele.dynamicKey());
    });
    this.querySelectorAll('[language]').forEach((ele) =>
      ele.setAttribute('language', this.language)
    );
  }

  updateUsersLength = () =>
    (this.querySelector('h1>span').innerHTML = `&nbsp;(${this.usersLength})`);

  handleSwitch = (e) => {
    this.querySelectorAll('div.subject').forEach((ele) => {
      ele.toggleAttribute('show');
    });

    e.target.textContent = `Switch to ${
      this.querySelector('div.subject:not([show])').id
    }`;
  };

  getComponent() {
    return (
      <div class="container">
        {this.usersData && (
          <>
            <button
              class="btn btn-secondary"
              type="button"
              onClick={this.handleSwitch}>
              Switch to sessions
            </button>
            <div id="users" class="subject" show>
              <UsersTitle node={this} title={this.getConstants('users')} />
              <div class="upload-export-users">
                <ldod-upload
                  data-key="uploadUsers"
                  title={this.getConstants('uploadUsers')}
                  data-url={`${
                    import.meta.env.VITE_HOST
                  }/admin/user/upload-users`}></ldod-upload>
                <ldod-export
                  file-prefix="users"
                  data-key="exportUsers"
                  title={this.getConstants('exportUsers')}
                  data-url={`${
                    import.meta.env.VITE_HOST
                  }/admin/user/export-users`}></ldod-export>
              </div>
              <UsersTable />
            </div>
            <div id="sessions" class="subject">
              <h1 class="text-center" data-key="sessions">
                {this.getConstants('sessions')}
              </h1>
              <AdminModeButton
                node={this}
                buttonLabel={this.getConstants(`${this.getMode()}Mode`)}
                tooltipContent={this.getConstants('changeLdodMode')}
              />
              <DeleteSessionsButton
                node={this}
                content={this.getConstants('deleteUserSessions')}
              />
              <SessionsListTable node={this} />
            </div>
          </>
        )}
      </div>
    );
  }
}
!customElements.get('manage-users') &&
  customElements.define('manage-users', ManageUsers);
