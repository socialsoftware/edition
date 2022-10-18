import check from '@src/resources/icons/check-circle.svg';
import exclamation from '@src/resources/icons/exclamation-circle.svg';
import eye from '@src/resources/icons/eye-solid.svg';
import google from '@src/resources/icons/google.svg';
import { socialAuth } from '@src/socialAuth.js';
import {
  setInvalidFor,
  setValidFor,
  capitalizeFirstLetter,
  loadConstants,
} from '@src/utils';
import { setState, getState } from '@src/store';
import { navigateTo } from 'shared/router.js';
import { newAuthRequest, userRequest } from '../../apiRequests';
import { eventEmiter, loginEvent, logoutEvent, tokenEvent } from '../../utils';

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
    this.appendChild(this.getComponent());
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

  getComponent = () => (
    <div class="row">
      <div class="login-form">
        <h2 data-key="signin-title">{this.getConstants('signin-title')}</h2>
        <form role="form" onSubmit={this.handleSubmit} class="form">
          <div class="col-md-offset-4 col-md-4">
            <div class="form-floating">
              <input
                class="form-control"
                id="username"
                type="text"
                autoComplete="username"
                name="username"
                value={this.username}
                onKeyUp={({ target: { value } }) => (this.username = value)}
                placeholder={this.getConstants('username')}
                title={this.getConstants('required')}
              />
              <label data-key="username" for="username">
                {this.getConstants('username')}
              </label>
              <img src={check} class="icon-validation valid" />
              <img src={exclamation} class="icon-validation invalid" />
              <small data-key="required"></small>
            </div>
          </div>
          <div class="col-md-offset-4 col-md-4">
            <div class="form-floating">
              <input
                id="password"
                class="form-control"
                type="password"
                autoComplete="current-password"
                name="password"
                value={this.password}
                onKeyUp={({ target: { value } }) => (this.password = value)}
                placeholder={this.getConstants('password')}
                title={this.getConstants('required')}
              />
              <label data-key="password" for="password">
                {this.getConstants('password')}
              </label>
              <img
                src={eye}
                class="icon"
                onPointerDown={this.revealPassword}
                onPointerUp={this.hidePassword}
              />
              <img src={check} class="icon-validation valid" />
              <img src={exclamation} class="icon-validation invalid" />
              <small data-key="required"></small>
            </div>
          </div>
          <div class="col-md-offset-5 col-md-2">
            <button
              data-key="sign-in"
              class="btn btn-primary"
              type="submit"
              style={{ width: '100%' }}>
              {this.getConstants('sign-in')}
            </button>
          </div>
        </form>
        <div style={{ padding: '4px 16px' }}>
          {[['google', google]].map(([provider, src]) => (
            <div class="col-md-offset-5 col-md-2">
              <button
                class={`btn btn-outline-primary social ${provider}`}
                type="button"
                onClick={() => socialAuth(provider, this)}
                style={{ width: '100%' }}>
                <img src={src} class="social-icon" />
                {capitalizeFirstLetter(provider)}
              </button>
            </div>
          ))}
        </div>
      </div>
      <div class="row">
        <a data-key="signup" is="nav-to" to="/user/signup">
          {this.getConstants('signup') ?? ''}
        </a>
      </div>
    </div>
  );
}

!customElements.get('sign-in') && customElements.define('sign-in', SignIn);
