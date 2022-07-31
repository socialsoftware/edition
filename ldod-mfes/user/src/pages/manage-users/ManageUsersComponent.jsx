import constants from './resources/constants.js';
import editIcon from '@src/resources/icons/edit.svg';
import { switchMode } from '@src/apiRequests.js';

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
    switchMode().then((res) => {
      res && this.setMode(res.ok);
      this.querySelector(
        '#adminMode>button>span'
      ).innerHTML = `${this.getMode()} Mode`;
    });
  };

  getComponent() {
    return (
      <div class="container">
        <h1
          class="text-center"
          style={{ display: 'flex', justifyContent: 'center' }}>
          <div data-key="users">{this.getConstants('users')}</div>
          <span>&nbsp;({this.usersLength})</span>
        </h1>
        <div id="userList" class="row"></div>
        <h1 class="text-center" data-key="sessions">
          {this.getConstants('sessions')}
        </h1>
        <div id="adminMode" class="row btn-row">
          {this.usersData && (
            <button
              type="button"
              class="btn btn-danger"
              onClick={this.onSwitchMode}>
              <img src={editIcon} class="btn-edit" />
              <span>{this.getMode()} Mode</span>
            </button>
          )}
        </div>
        <div id="deleteSessions" class="row btn-row">
          {this.usersData && (
            <button type="button" class="btn btn-danger">
              <img src={editIcon} class="btn-edit" />
              <span>Delete User Sessions</span>
            </button>
          )}
        </div>
        <div id="sessionsList" class="row"></div>
      </div>
    );
  }
}
!customElements.get('manage-users') &&
  customElements.define('manage-users', ManageUsers);
