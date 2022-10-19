import { setInvalidFor, setValidFor, loadConstants } from '@src/utils';
import { setState, getState } from '@src/store';
import { navigateTo } from 'shared/router.js';
import { newAuthRequest, userRequest } from '../../apiRequests';
import { eventEmiter, loginEvent, logoutEvent, tokenEvent } from '../../utils';
import SigninForm from './SigninForm';

class SignIn extends HTMLElement {
  constructor() {
    super();
    this.username = '';
    this.password = '';
    this.isValid = true;
  }

  get language() {
    return this.getAttribute('language');
  }

  static get observedAttributes() {
    return ['language'];
  }

  async connectedCallback() {
    if (getState().user) return navigateTo('/');
    await this.setConstants();
    this.appendChild(<SigninForm node={this} />);
  }

  attributeChangedCallback(name, oldValue, newValue) {
    if (name === 'language' && oldValue && newValue !== oldValue) {
      this.updateLanguage();
    }
  }

  disconnectedCallback() {}

  setConstants = async () =>
    (this.constants = await loadConstants(this.language));

  getConstants(key) {
    return this.constants[key];
  }

  async updateLanguage() {
    await this.setConstants();
    ['[data-key]'].forEach((selector) =>
      this.querySelectorAll(selector).forEach(
        (ele) => (ele.textContent = this.getConstants(ele.dataset.key))
      )
    );
    setState({ language: this.language });
  }

  clearDataInputs() {
    this.querySelectorAll('input').forEach((input) => {
      input.value = '';
      this[input.name] = '';
    });
  }

  clearStyleInputs() {
    this.querySelectorAll('.valid').forEach((div) =>
      div.classList.remove('valid')
    );
  }

  checkInputs = () => {
    const getInput = (name) => this.querySelector(`input[name=${name}]`);
    ['username', 'password'].forEach((input) => {
      if (!this[input].trim()) {
        this.isValid = false;
        return setInvalidFor(getInput(input), this.getConstants('required'));
      }
      setValidFor(getInput(input));
    });
  };

  handleSubmit = async (e) => {
    e.preventDefault();
    this.checkInputs();
    if (this.isValid)
      await newAuthRequest({
        username: this.username,
        password: this.password,
      })
        .then((res) => {
          if (res.accessToken) this.onAuthSuccess(res.accessToken);
          if (res.message && res.ok === false) this.onAuthFail(res.message);
        })
        .catch((error) => this.onAuthFail(error?.message));
    this.clearStyleInputs();
    this.clearDataInputs();
    this.isValid = true;
  };

  login = () =>
    userRequest(getState().token)
      .then((user) => {
        setState({ user });
        this.dispatchEvent(loginEvent(user));
        location.pathname.endsWith('/user/signin') && navigateTo('/');
      })
      .catch((error) => error.message === 'unauthorized' && this.logout());

  onAuthSuccess = (token) => {
    if (!token) return this.logout();
    if (token !== getState().token) setState({ token });
    this.dispatchEvent(tokenEvent(token));
    this.login();
  };

  logout = () => {
    setState({ token: '', user: '' });
    this.dispatchEvent(logoutEvent);
    navigateTo('/');
  };

  onAuthFail = (message) => {
    message &&
      eventEmiter(
        'ldod-error',
        {
          detail: { message },
          bubbles: true,
          composed: true,
        },
        this
      );
    setState({ token: '' });
  };

  revealPassword = ({ target }) => {
    target.parentElement.querySelector('input[name=password]').type = 'text';
  };

  hidePassword = ({ target }) => {
    target.parentElement.querySelector('input[name=password]').type =
      'password';
  };
}

!customElements.get('sign-in') && customElements.define('sign-in', SignIn);
