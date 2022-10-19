import style from './manageUsersStyle.css?inline';
import 'shared/modal.js';
import constants from './resources/constants.js';

import { exportButton, uploadButton } from 'shared/buttons.js';
import { loadAndAssignUsers } from './ManageUsers';
import ManageUsersTable from './ManageUsersTable';

exportButton();
uploadButton();

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

  handleUsersUpload = ({ detail }) =>
    detail.message === 'Users uploaded' && loadAndAssignUsers(this);

  attributeChangedCallback(name, oldV, newV) {
    this.handlers[name](oldV, newV);
  }
  disconnectedCallback() {}

  addEventListeners() {
    this.addEventListener('ldod-message', this.handleUsersUpload);
    this.querySelectorAll('[tooltip-ref]').forEach((tooltipped) => {
      tooltipped.parentNode.addEventListener('pointerenter', loadToolip);
    });
    this.addEventListener('ldod-table-searched', this.updateUsersListTitle);
  }

  updateUsersListTitle = ({ detail }) => {
    if (detail.id === 'user-usersListTable')
      this.querySelector('h1#title span').innerHTML = `&nbsp;(${
        this.querySelectorAll('table#user-usersListTable tr[searched]').length
      })`;
  };

  render() {
    this.innerHTML = '';
    this.appendChild(<style>{style}</style>);
    this.appendChild(<ManageUsersTable node={this} />);
    this.addEventListeners();
  }

  handlers = {
    language: (oldV, newV) => {
      if (oldV && newV !== oldV) this.handleChangeLanguage();
    },
    data: () => {
      this.hasAttribute('data') && this.render();
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

  handleSwitch = (e) => {
    this.querySelectorAll('div.subject').forEach((ele) => {
      ele.toggleAttribute('show');
    });

    e.target.textContent = `Switch to ${
      this.querySelector('div.subject:not([show])').id
    }`;
  };
}
!customElements.get('manage-users') &&
  customElements.define('manage-users', ManageUsers);
