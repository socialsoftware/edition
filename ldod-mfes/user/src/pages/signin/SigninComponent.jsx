import check from '@src/resources/icons/check-circle.svg';
import exclamation from '@src/resources/icons/exclamation-circle.svg';
import eye from '@src/resources/icons/eye-solid.svg';
import facebook from '@src/resources/icons/facebook.svg';
import google from '@src/resources/icons/google.svg';
import linkedin from '@src/resources/icons/linkedin.svg';
import twitter from '@src/resources/icons/twitter.svg';
import { authRequest } from '@src/apiRequests.js';
import { socialAuth } from '@src/socialAuth.js';
import {
  setInvalidFor,
  setValidFor,
  capitalizeFirstLetter,
  loadConstants,
} from '@src/utils';
import { setState, getState } from '@src/store';
import { navigateTo } from 'shared/router.js';

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
    ['[key]'].forEach((selector) =>
      this.querySelectorAll(selector).forEach((ele) => {
        if (ele instanceof HTMLInputElement) {
          ele.placeholder = this.getConstants(ele.getAttribute('key'));
          ele.title = this.getConstants('required');
          return;
        }
        ele.textContent = this.getConstants(ele.getAttribute('key'));
      })
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
    this.querySelectorAll('.form-control').forEach((div) =>
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

  handleSubmit = (e) => {
    e.preventDefault();
    this.checkInputs();
    if (this.isValid) {
      authRequest({
        username: this.username,
        password: this.password,
      }).catch((error) => {
        console.log(error);
        setState({ token: '' });
      });
    }
    this.clearStyleInputs();
    this.clearDataInputs();
    this.isValid = true;
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
        <h2 key="signin-title">{this.getConstants('signin-title')}</h2>
        <form role="form" onSubmit={this.handleSubmit} class="form">
          <div class="col-md-offset-4 col-md-4">
            <div class="form-control">
              <input
                key="username"
                type="text"
                autoComplete="username"
                name="username"
                value={this.username}
                onKeyUp={({ target: { value } }) => (this.username = value)}
                placeholder={this.getConstants('username')}
                title={this.getConstants('required')}
              />
              <img src={check} class="icon-validation valid" />
              <img src={exclamation} class="icon-validation invalid" />
              <small key="required"></small>
            </div>
          </div>
          <div class="col-md-offset-4 col-md-4">
            <div class="form-control">
              <input
                key="password"
                type="password"
                autoComplete="current-password"
                name="password"
                value={this.password}
                onKeyUp={({ target: { value } }) => (this.password = value)}
                placeholder={this.getConstants('password')}
                title={this.getConstants('required')}
              />
              <img
                src={eye}
                class="icon"
                onMouseDown={this.revealPassword}
                onMouseUp={this.hidePassword}
              />
              <img src={check} class="icon-validation valid" />
              <img src={exclamation} class="icon-validation invalid" />
              <small key="required"></small>
            </div>
          </div>
          <div class="col-md-offset-5 col-md-2">
            <button
              key="sign-in"
              class="btn-signin"
              type="submit"
              style={{ width: '100%' }}>
              {this.getConstants('sign-in')}
            </button>
          </div>
        </form>
        <div style={{ padding: '4px 16px' }}>
          {[
            //     ['twitter', twitter],
            ['google', google],
            //   ['facebook', facebook],
            // ['linkedin', linkedin],
          ].map(([provider, src]) => (
            <div class="col-md-offset-5 col-md-2">
              <button
                class={`btn-signin social ${provider}`}
                type="button"
                onClick={() => socialAuth(provider, this.constants)}
                style={{ width: '100%' }}>
                <img src={src} class="social-icon" />
                {capitalizeFirstLetter(provider)}
              </button>
            </div>
          ))}
        </div>
      </div>
      <div class="row">
        <a key="signup" is="nav-to" to="/user/signup">
          {this.getConstants('signup') ?? ''}
        </a>
      </div>
    </div>
  );
}

!customElements.get('sign-in') && customElements.define('sign-in', SignIn);
