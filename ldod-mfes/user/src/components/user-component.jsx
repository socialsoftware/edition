import { registerInstance, getState, userFullName } from '@src/store.js';
import { loadConstants } from '@src/utils.js';
import { navigateTo } from 'shared/router.js';
import { sleep } from 'shared/utils.js';
import { setState } from '../store';
import { userReferences } from '../user-references';
import {
  loginPublisher,
  loginSubscriber,
  logoutPublisher,
  logoutSubscriber,
} from '../events-modules';
import { AuthComponent } from './auth-component';
import { NonAuthComponent } from './non-auth-component';

export class UserComponent extends HTMLLIElement {
  constructor(language) {
    super();
    if (language) this.language = language;
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
    if (getState().user) loginPublisher(getState().user);
    await sleep(1);
    this.addListeners();
  }

  disconnectedCallback() {
    this.loginUnsub();
    this.logoutUnsub();
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
      ? this.appendChild(<AuthComponent root={this} />)
      : this.appendChild(<NonAuthComponent />);
    this.updateLanguage();
  }

  updateComponent() {
    this.innerHTML = '';
    this.render();
  }

  addListeners() {
    this.loginUnsub = loginSubscriber(this.onUserLogin);
    this.logoutUnsub = logoutSubscriber(this.onUserLogout);
  }

  onUserLogin = () => {
    this.id.endsWith('1') && console.log('test');
    this.updateComponent();
  };

  onUserLogout = () => {
    this.logoutHandler();
    this.updateComponent();
  };

  async updateLanguage() {
    await this.setConstants();
    this.querySelectorAll('a.update-language').forEach(
      (ele) => (ele.innerHTML = this.getConstants(ele.id))
    );
  }

  logoutHandler = () => {
    if (!(getState().user || getState().token)) return;
    setState({ token: '', user: '' });
    logoutPublisher();
    navigateTo(userReferences.signin());
  };
}

!customElements.get('user-component') &&
  customElements.define('user-component', UserComponent, { extends: 'li' });
