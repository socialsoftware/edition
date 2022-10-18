import { registerInstance, getState, userFullName } from '@src/store.js';
import { loadConstants } from '@src/utils.js';
import { navigateTo } from 'shared/router.js';
import { setState } from '../store';
import { loginEvent, logoutEvent } from '../utils';

const NotAuthComponent = () => (
  <a is="nav-to" id="login" class="login update-language" to="/user/signin"></a>
);

const AuthComponent = () => (
  <>
    <a id="loggedIn" class="dropdown-toggle">
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

export class UserComponent extends HTMLLIElement {
  constructor(language) {
    super();
    if (language) this.language = language;
    this.onUserLogout = this.onUserLogout.bind(this);
    this.onUserLogin = this.onUserLogin.bind(this);
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
    if (getState().user) {
      this.onUserLogin();
      this.dispatchEvent(loginEvent(getState().user));
    }
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

  render() {
    getState().user
      ? this.appendChild(<AuthComponent />)
      : this.appendChild(<NotAuthComponent />);
    this.updateLanguage();
    this.addListeners();
  }

  updateComponent(e) {
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
    window.addEventListener('ldod-login', this.onUserLogin);
    window.addEventListener('ldod-logout', this.onUserLogout);
  }

  removeListeners() {
    ['logout', 'password'].forEach((id) =>
      this.querySelector(`#${id}`)?.removeEventListener(
        'click',
        this.handlers(id)?.handler
      )
    );
    this.innerHTML = '';
    window.removeEventListener('ldod-login', this.onUserLogin);
    window.removeEventListener('ldod-logout', this.onUserLogout);
  }

  onUserLogin() {
    this.updateComponent();
    /* import.meta.env.PROD &&
    this.dispatchEvent(
      new CustomEvent('ldod-login', {
        bubbles: true,
        composed: true,
        detail: { user: getState().user },
      })
    );
    );*/
  }

  onUserLogout() {
    this.updateComponent();
    /*  import.meta.env.PROD &&
      this.dispatchEvent(
        new CustomEvent('ldod-logout', {
          composed: true,
          bubbles: true,
        })
      );*/
  }

  async updateLanguage() {
    await this.setConstants();
    this.querySelectorAll('a.update-language').forEach(
      (ele) => (ele.innerHTML = this.getConstants(ele.id))
    );
  }

  logout = () => {
    setState({ token: '', user: '' });
    this.dispatchEvent(logoutEvent);
    navigateTo('/');
  };

  handlers(id) {
    return {
      logout: {
        id: 'logout',
        handler: () => this.logout(),
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
