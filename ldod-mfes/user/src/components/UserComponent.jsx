import { registerInstance, getState, userFullName } from '@src/store.js';
import { loadConstants } from '@src/utils.js';
import { navigateTo } from 'shared/router.js';
import { setState } from '../store';
import { userReferences } from '../user-references';
import { ldodEventBus, loginPublisher, logoutPublisher } from '../events-modules';
import { AuthComponent } from './auth-component';
import { NonAuthComponent } from './non-auth-component';

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
      loginPublisher(getState().user);
    }
  }

  attributeChangedCallback(name, oldValue, newValue) {
    if (oldValue && newValue && oldValue !== newValue) {
      name === 'language' && this.updateLanguage(newValue);
    }
  }

  setConstants = async () => {
    this.constants = await loadConstants(this.language);
  };

  getConstants = (key) => this.constants[key];

  render() {
    getState().user
      ? this.appendChild(<AuthComponent logoutHandler={this.logoutHandler} />)
      : this.appendChild(<NonAuthComponent />);
    this.updateLanguage();
    this.addListeners();
  }

  updateComponent(e) {
    this.innerHTML = '';
    this.render();
  }

  addListeners() {
    ldodEventBus.subscribe("user:login", this.onUserLogin)
    ldodEventBus.subscribe("user:logout", this.onUserLogout)
  }

  onUserLogin() {
    this.updateComponent();
  }

  onUserLogout() {
    this.logoutHandler();
    this.updateComponent();
  }

  async updateLanguage() {
    await this.setConstants();
    this.querySelectorAll('a.update-language').forEach(
      (ele) => (ele.innerHTML = this.getConstants(ele.id))
    );
  }

  logoutHandler = () => {
    if (!(getState().user && getState().token)) return;
    setState({ token: '', user: '' });
    logoutPublisher();
    navigateTo(userReferences.signin());
  };

}

!customElements.get('user-component') &&
  customElements.define('user-component', UserComponent, { extends: 'li' });
