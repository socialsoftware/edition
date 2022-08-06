import constants from './resources/constants.js';
import editIcon from '@src/resources/icons/edit.svg';
import UsersTable from './components/UsersListTable.jsx';
import { switchModeRequest, deleteSessionsRequest } from '@src/apiRequests.js';
import './LdodTooltip.jsx';
import Table from './LdodTable.jsx';
import './LdodModal.jsx';
import { usersData } from './ManageUsers.jsx';

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
    return usersData() ? usersData().userList.length : 'loading';
  }

  getMode() {
    return usersData().ldoDAdmin ? 'admin' : 'user';
  }

  setMode(mode) {
    usersData().ldoDAdmin = mode;
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

  render() {
    this.innerHTML = '';
    this.appendChild(this.getComponent());
  }

  handlers = {
    language: (oldV, newV) => {
      if (oldV && newV !== oldV) this.handleChangeLanguage();
    },
    data: () => this.render(),
  };

  handleChangeLanguage() {
    this.querySelectorAll('[data-key').forEach((ele) => {
      ele.textContent = this.getConstants(ele.dataset.key);
    });
    this.querySelectorAll('[data-tooltipkey]').forEach((ele) => {
      ele.setAttribute('content', this.getConstants(ele.dataset.tooltipkey));
    });
    this.querySelectorAll('[dynamic]').forEach((ele) => {
      ele.textContent = this.getConstants(ele.dynamicKey());
    });
  }

  onSwitchMode = () => {
    switchModeRequest().then((res) => {
      res && this.setMode(res.ok);
      this.querySelector('#adminMode>button>span').innerHTML =
        this.getConstants(`${this.getMode()}Mode`);
    });
  };

  onDeleteSessions = () => {
    deleteSessionsRequest().then((data) => {
      usersData().sessionList = data.sessionList;
      this.querySelector('div#sessions-list').replaceChildren(
        this.getSessionsTable()
      );
    });
  };

  getSessionsTable = () => (
    <Table
      id="sessions-list-table"
      classes="table table-responsive-sm table-striped table-bordered"
      headers={constants.sessionListHeaders}
      data={usersData().sessionList}
      constants={(key) => this.getConstants(key)}
    />
  );

  updateUsersLength = () =>
    (this.querySelector('h1>span').innerHTML = `&nbsp;(${this.usersLength})`);

  getComponent() {
    return (
      <div class="container">
        {usersData() && (
          <>
            <h1
              class="text-center"
              style={{ display: 'flex', justifyContent: 'center' }}>
              <div data-key="users">{this.getConstants('users')}</div>
              <span>&nbsp;({this.usersLength})</span>
            </h1>
            <UsersTable />
            <h1 class="text-center" data-key="sessions">
              {this.getConstants('sessions')}
            </h1>
            <div id="adminMode" class="row btn-row">
              <button
                id="switch-button"
                type="button"
                class="btn btn-danger ellipsis"
                onClick={this.onSwitchMode}>
                <img src={editIcon} class="btn-icon" />
                <span dynamic dynamicKey={() => `${this.getMode()}Mode`}>
                  {this.getConstants(`${this.getMode()}Mode`)}
                </span>
              </button>
              <ldod-tooltip
                placement="top"
                data-ref="#switch-button"
                data-tooltipkey="changeLdodMode"
                content={this.getConstants('changeLdodMode')}></ldod-tooltip>
            </div>
            <div id="deleteSessions" class="row btn-row">
              <button
                id="delete-sessions-button"
                type="button"
                class="btn btn-danger ellipsis"
                onClick={this.onDeleteSessions}>
                <img src={editIcon} class="btn-icon" />
                <span data-key="deleteUserSessions">
                  {this.getConstants('deleteUserSessions')}
                </span>
              </button>
              <ldod-tooltip
                placement="top"
                data-ref="#delete-sessions-button"
                data-tooltipkey="deleteUserSessions"
                content={this.getConstants(
                  'deleteUserSessions'
                )}></ldod-tooltip>
            </div>
            <div id="sessions-list" class="row">
              {this.getSessionsTable()}
            </div>
          </>
        )}
      </div>
    );
  }
}
!customElements.get('manage-users') &&
  customElements.define('manage-users', ManageUsers);
