import constants from './resources/constants.js';
import editIcon from '@src/resources/icons/edit.svg';
import trash from '@src/resources/icons/trash.svg';
import edit from '@src/resources/icons/edit-primary.svg';

import {
  switchModeRequest,
  deleteSessionsRequest,
  changeActiveRequest,
  removeUserRequest,
} from '@src/apiRequests.js';
import './Tooltip.jsx';
import Table from './SessionsTable.jsx';

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
    return this.usersData.ldoDAdmin ? 'Admin' : 'User';
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

  render() {
    this.innerHTML = '';
    this.appendChild(this.getComponent());
  }

  handlers = {
    language: (oldV, newV) => {
      if (oldV && newV !== oldV) this.handleChangeLanguage();
    },
    data: (oldV, newV) => {
      this.render();
    },
  };

  handleChangeLanguage() {
    this.querySelectorAll('[data-key').forEach((ele) => {
      ele.textContent = this.getConstants(ele.dataset.key);
    });
  }

  onSwitchMode = () => {
    switchModeRequest().then((res) => {
      res && this.setMode(res.ok);
      this.querySelector(
        '#adminMode>button>span'
      ).innerHTML = `${this.getMode()} Mode`;
    });
  };

  onDeleteSessions = () => {
    deleteSessionsRequest().then((data) => {
      this.usersData.sessionList = data.sessionList;
      this.querySelector('div#sessions-list').replaceChildren(
        this.getSessionsTable()
      );
    });
  };

  onChangeActive = async (externalId) => {
    const res = await changeActiveRequest(externalId);
    this.usersData.userList.forEach((user) => {
      if (user.externalId === externalId) {
        user.active = res.ok;
        return;
      }
    });
    const newActive = this.getUsersListActive(res.ok, externalId);
    this.querySelector(`#active-${externalId}`).replaceWith(newActive);
  };

  onDeleteUser = async ({ target }) => {
    const res = await removeUserRequest(target.dataset.id);
    this.usersData.userList = res.userList;
    getparentWithTag(target, 'TR').remove();
    this.updateUsersLength();
  };

  getSessionsTable = () => (
    <Table
      id="sessions-list-table"
      classes="table table-responsive-sm table-striped table-bordered"
      headers={constants.sessionListHeaders}
      data={this.usersData.sessionList}
      constants={(key) => this.getConstants(key)}
    />
  );

  getUsersListTable = () => (
    <Table
      id="users-list-table"
      classes="table table-responsive-sm table-striped table-bordered"
      headers={constants.usersListHeaders}
      data={this.usersData.userList.map((user) => ({
        ...user,
        enabled: (
          <div data-key={String(user.enabled).toUpperCase()}>
            {this.getConstants(String(user.enabled).toUpperCase())}
          </div>
        ),
        active: this.getUsersListActive(user.active, user.externalId),
        actions: this.getUsersListActions(user.externalId),
      }))}
      constants={(key) => this.getConstants(key)}
    />
  );

  getUsersListActions(id) {
    return (
      <div class="text-center">
        <img
          id={`edit-icon-${id}`}
          data-id={id}
          src={edit}
          class="btn-icon action"
        />
        <img
          id={`trash-icon-${id}`}
          data-id={id}
          src={trash}
          class="btn-icon action"
          onClick={this.onDeleteUser}
        />
        <ldod-tooltip
          data-ref={`#edit-icon-${id}`}
          placement="top"
          content="Edit user"></ldod-tooltip>
        <ldod-tooltip
          data-ref={`#trash-icon-${id}`}
          placement="top"
          content="Delete user"></ldod-tooltip>
      </div>
    );
  }

  getUsersListActive(active, id) {
    return (
      <div id={`active-${id}`} class="text-center">
        <button
          id={`button-active-${id}`}
          class={`btn ${active ? 'btn-success' : 'btn-secondary'} btn-sm`}
          onClick={() => this.onChangeActive(id)}>
          <span data-key={String(active).toUpperCase()}>
            {this.getConstants(String(active).toUpperCase())}
          </span>
        </button>
        <ldod-tooltip
          placement="top"
          data-ref={`#button-active-${id}`}
          content="Toggle User Active state"></ldod-tooltip>
      </div>
    );
  }

  updateUsersLength = () =>
    (this.querySelector('h1>span').innerHTML = `&nbsp;(${this.usersLength})`);

  getComponent() {
    return (
      <div class="container">
        {this.usersData && (
          <>
            <h1
              class="text-center"
              style={{ display: 'flex', justifyContent: 'center' }}>
              <div data-key="users">{this.getConstants('users')}</div>
              <span>&nbsp;({this.usersLength})</span>
            </h1>
            <div id="userList" class="row">
              {this.getUsersListTable()}
            </div>
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
                <span>{this.getMode()} Mode</span>
              </button>
              <ldod-tooltip
                placement="top"
                data-ref="#switch-button"
                content="Toggle mode"></ldod-tooltip>
            </div>
            <div id="deleteSessions" class="row btn-row">
              <button
                type="button"
                class="btn btn-danger ellipsis"
                onClick={this.onDeleteSessions}>
                <img src={editIcon} class="btn-icon" />
                <span>Delete User Sessions</span>
              </button>
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

function getparentWithTag(ele, tag) {
  if (!(ele instanceof Node)) return;
  return ele.parentNode.tagName === tag
    ? ele.parentNode
    : getparentWithTag(ele.parentNode, tag);
}
