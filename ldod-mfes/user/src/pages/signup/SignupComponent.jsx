import {
  ALPHABETIC_REGEX,
  ALPHANUMERIC_REGEX,
  EMAIL_REGEX,
  PASSWORD_REGEX,
} from '@src/resources/constants.js';
import { setInvalidFor, setValidFor, loadConstants } from '@src/utils';
import check from '@src/resources/icons/check-circle.svg';
import exclamation from '@src/resources/icons/exclamation-circle.svg';
import eye from '@src/resources/icons/eye-solid.svg';
import { signupRequest } from '@src/apiRequests';
import { navigateTo } from 'shared/router.js';
import { setState } from '@src/store';
import { emitMessageEvent } from '@src/utils';

export class SignUp extends HTMLElement {
  constructor() {
    super();
    this.isValid = true;
    this.firstname = {
      value: '',
      test: (val) => ALPHABETIC_REGEX.test(val),
      message: () => this.getConstants('req-alphabetic'),
    };
    this.lastname = {
      value: '',
      test: (val) => ALPHABETIC_REGEX.test(val),
      message: () => this.getConstants('req-alphabetic'),
    };
    this.username = {
      value: '',
      test: (val) => ALPHANUMERIC_REGEX.test(val),
      message: () => this.getConstants('req-alphanumeric'),
    };
    this.password = {
      value: '',
      test: (val) => PASSWORD_REGEX.test(val),
      message: () => this.getConstants('min-6'),
    };
    this.email = {
      value: '',
      test: (val) => EMAIL_REGEX.test(val),
      message: () => this.getConstants('email-pattern'),
    };
    this.conduct = {
      value: false,
      test: (val) => val === true,
      message: () => this.getConstants('conduct-check'),
    };

    this.socialMedia = { value: '' };
    this.socialId = { value: '' };
  }

  static get observedAttributes() {
    return ['language'];
  }

  get language() {
    return this.getAttribute('language');
  }

  getState(name) {
    return this[name];
  }

  getStateKeys() {
    return [
      'username',
      'firstname',
      'lastname',
      'password',
      'email',
      'socialMedia',
      'socialId',
      'conduct',
    ];
  }

  getConstants(key) {
    return this.constants[key] ?? key;
  }

  importHistoryState() {
    this.getStateKeys().forEach((state) => {
      this[state].value = history.state[state] ?? '';
    });
  }

  async connectedCallback() {
    await this.setConstants();
    this.importHistoryState();
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

  async updateLanguage() {
    await this.setConstants();
    ['[key]'].forEach((selector) =>
      this.querySelectorAll(selector).forEach((ele) => {
        if (ele instanceof HTMLInputElement) {
          ele.placeholder = this.getConstants(ele.getAttribute('key'));
          ele.title = this.getState(ele.name).message();
          return;
        }
        ele.innerHTML = this.getConstants(ele.getAttribute('key'));
      })
    );
    setState({ language: this.language });
  }

  handleSubmit = async (e) => {
    e.preventDefault();
    this.checkInputs();
    if (this.isValid) {
      await signupRequest({
        firstName: this.firstname.value,
        lastName: this.lastname.value,
        username: this.username.value,
        password: this.password.value,
        email: this.email.value,
        conduct: this.conduct.value,
        socialMediaId: this.socialId.value,
        socialMediaService: this.socialMedia.value,
      })
        .then(({ ok, message }) => {
          if (ok) {
            emitMessageEvent(this.getConstants(message));
            console.log('test');
            this.clearDataInputs();
            return navigateTo('/user/signin');
          }
          emitMessageEvent(message, 'error');
        })
        .catch(({ message }) =>
          emitMessageEvent(this.getConstants(message), 'error')
        );
      this.clearStyleInputs();
    }
    this.isValid = true;
  };

  checkInputs = () => {
    const getInput = (name) => this.querySelector(`input[name=${name}]`);
    this.getStateKeys().forEach((input) => {
      if (!getInput(input)) return;
      const state = this.getState(input);
      if (!state.test(state.value)) {
        this.isValid = false;
        return setInvalidFor(getInput(input), state.message());
      }
      setValidFor(getInput(input));
    });
  };

  clearDataInputs() {
    this.querySelectorAll('input').forEach((input) => {
      input.value = '';
      this[input.name].value = '';
    });
  }

  clearStyleInputs() {
    this.querySelectorAll('.form-control').forEach((div) =>
      div.classList.remove('valid')
    );
  }

  revealPassword = ({ target }) => {
    target.parentElement.querySelector('input[name=password]').type = 'text';
  };

  hidePassword = ({ target }) => {
    target.parentElement.querySelector('input[name=password]').type =
      'password';
  };

  getComponent() {
    return (
      <>
        <div class="row">
          <h3 key="register">{this.getConstants('register')}</h3>
        </div>
        <div class="row">
          <form onSubmit={this.handleSubmit} role="form" class="form">
            <div class="col-md-offset-4 col-md-4">
              <div class="form-control">
                <input
                  key="firstname"
                  type="text"
                  autoComplete="first-name"
                  name="firstname"
                  value={this.firstname.value}
                  onKeyUp={({ target: { value } }) =>
                    (this.firstname.value = value)
                  }
                  placeholder={this.getConstants('firstname')}
                  title={this.firstname.message()}
                />
                <img src={check} class="icon-validation valid" />
                <img src={exclamation} class="icon-validation invalid" />
                <small key="req-alphabetic"></small>
              </div>
            </div>
            <div class="col-md-offset-4 col-md-4">
              <div class="form-control">
                <input
                  key="lastname"
                  type="text"
                  autoComplete="family-name"
                  name="lastname"
                  value={this.lastname.value}
                  onKeyUp={({ target: { value } }) =>
                    (this.lastname.value = value)
                  }
                  placeholder={this.getConstants('lastname')}
                  title={this.lastname.message()}
                />
                <img src={check} class="icon-validation valid" />
                <img src={exclamation} class="icon-validation invalid" />
                <small key="req-alphabetic"></small>
              </div>
            </div>
            <div class="col-md-offset-4 col-md-4">
              <div class="form-control">
                <input
                  key="username"
                  type="text"
                  autoComplete="username"
                  name="username"
                  value={this.username.value}
                  onKeyUp={({ target: { value } }) =>
                    (this.username.value = value)
                  }
                  placeholder={this.getConstants('username')}
                  title={this.username.message()}
                />
                <img src={check} class="icon-validation valid" />
                <img src={exclamation} class="icon-validation invalid" />
                <small key="req-alphanumeric"></small>
              </div>
            </div>
            <div class="col-md-offset-4 col-md-4">
              <div class="form-control">
                <input
                  key="password"
                  type="password"
                  autoComplete="current-password"
                  name="password"
                  value={this.password.value}
                  onKeyUp={({ target: { value } }) =>
                    (this.password.value = value)
                  }
                  placeholder={this.getConstants('password')}
                  title={this.password.message()}
                />
                <img
                  src={eye}
                  alt="eye icon"
                  class="icon"
                  onMouseDown={this.revealPassword}
                  onMouseUp={this.hidePassword}
                />
                <img src={check} class="icon-validation valid" />
                <img src={exclamation} class="icon-validation invalid" />
                <small key="min-6"></small>
              </div>
            </div>
            <div class="col-md-offset-4 col-md-4">
              <div class="form-control">
                <input
                  key="email"
                  type="text"
                  autoComplete="email"
                  name="email"
                  value={this.email.value}
                  onKeyUp={({ target: { value } }) =>
                    (this.email.value = value)
                  }
                  placeholder={this.getConstants('email')}
                  title={this.email.message()}
                />
                <img src={check} class="icon-validation valid" />
                <img src={exclamation} class="icon-validation invalid" />
                <small key="email-pattern"></small>
              </div>
            </div>
            <div class="col-md-offset-4 col-md-4">
              <div class="form-control">
                <input
                  type="checkbox"
                  name="conduct"
                  value={this.conduct.value}
                  onChange={({ target }) =>
                    (this.conduct.value = target.checked)
                  }
                />
                <span key="conduct">{this.getConstants('conduct')}</span>
                <img src={check} class="icon-validation valid" />
                <img src={exclamation} class="icon-validation invalid" />
                <img src={check} class="icon-validation valid" />
                <img src={exclamation} class="icon-validation invalid" />
                <small key="conduct-check"></small>
              </div>
            </div>
            <div class="col-sm-12">
              <button key="register" class="btn btn-primary" type="submit">
                {this.getConstants('register')}
              </button>
            </div>
          </form>
        </div>
      </>
    );
  }
}
!customElements.get('sign-up') && customElements.define('sign-up', SignUp);
