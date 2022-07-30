import { registerInstance, getState, userFullName } from '@src/store.js';
import { loadConstants } from '@src/utils.js';
import { logout } from '@src/apiRequests';

function getUserComponentNotAuthenticated() {
  return (
    <a
      is="nav-to"
      id="login"
      class="login update-language"
      to="/user/signin"></a>
  );
}

const getUserComponentAuthenticated = () => {
  return (
    <>
      <a id="login" class="dropdown-toggle">
        {userFullName()}
        <span class="caret"></span>
      </a>
      <ul class="dropdown-menu">
        <li>
          <a class="update-language" id="logout"></a>
        </li>
        <li>
          <a
            is="nav-to"
            class="update-language"
            id="change-password"
            to="/user/change-password"></a>
        </li>
      </ul>
    </>
  );
};

export class UserComponent extends HTMLLIElement {
  constructor() {
    super();
    this.updateComponent = this.updateComponent.bind(this);
    this.id = `user-component-${registerInstance()}`;
  }
  get language() {
    return this.getAttribute('language') ?? 'en';
  }

  set language(language) {
    this.setAttribute('language', language);
  }

  static get observedAttributes() {
    return ['language'];
  }

  async connectedCallback() {
    await this.setConstants();
    this.render();
  }

  attributeChangedCallback(name, oldValue, newValue) {
    if (oldValue && newValue && oldValue !== newValue) {
      name === 'language' && this.updateLanguage(newValue);
    }
  }

  disconnectedCallback() {
    this.removeListeners();
  }

  setConstants = async () =>
    (this.constants = await loadConstants(this.language));

  getConstants = (key) => this.constants[key];

  removeListeners() {
    ['logout', 'password'].forEach((id) =>
      this.querySelector(`#${id}`)?.removeEventListener(
        'click',
        this.handlers(id)?.handler
      )
    );
    this.innerHTML = '';
    ['ldod-login', 'ldod-logout'].forEach((event) =>
      window.removeEventListener(event, this.updateComponent)
    );
  }

  render() {
    getState().user
      ? this.appendChild(getUserComponentAuthenticated())
      : this.appendChild(getUserComponentNotAuthenticated());
    this.addListeners();
    this.updateLanguage();
  }

  updateComponent() {
    this.removeListeners();
    this.render();
  }

  addListeners() {
    ['logout', 'password'].forEach((id) =>
      this.querySelector(`#${id}`)?.addEventListener(
        'click',
        this.handlers(id).handler
      )
    );
    ['ldod-login', 'ldod-logout'].forEach((event) =>
      window.addEventListener(event, this.updateComponent)
    );
  }

  async updateLanguage() {
    await this.setConstants();
    this.querySelectorAll('a.update-language').forEach(
      (ele) => (ele.innerHTML = this.getConstants(ele.id))
    );
  }

  handlers(id) {
    return {
      logout: {
        id: 'logout',
        handler: () => logout(),
      },
      password: {
        id: 'password',
        handler: () => console.log('password changed'),
      },
    }[id];
  }
}

!customElements.get('user-component') &&
  customElements.define('user-component', UserComponent, { extends: 'li' });
