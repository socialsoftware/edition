import { PASSWORD_REGEX } from '@src/resources/constants.js';
import eye from '@src/resources/icons/eye-solid.svg';
import check from '@src/resources/icons/check-circle.svg';
import exclamation from '@src/resources/icons/exclamation-circle.svg';
import { setState, getState } from '@src/store';
import { navigateTo } from 'shared/router.js';
import { changePasswordRequest } from '@src/apiRequests.js';
import {
  setInvalidFor,
  setValidFor,
  loadConstants,
  emitMessageEvent,
} from '@src/utils';

export class ChangePassword extends HTMLElement {
  constructor() {
    super();
    this.isValid = true;
    this.username = getState().user?.username;
    this.current = {
      value: '',
      test: (val) => val && true,
      message: () => this.getConstants('required'),
    };
    this.new = {
      value: '',
      test: (val) => PASSWORD_REGEX.test(val) && val !== this.current.value,
      message: () => this.getConstants('minCurrent'),
    };
    this.confirm = {
      value: '',
      test: (val) => val && val === this.new.value,
      message: () => this.getConstants('confirmPattern'),
    };
  }
  static get observedAttributes() {
    return ['language'];
  }

  get language() {
    return this.getAttribute('language');
  }

  async connectedCallback() {
    await this.setConstants();
    if (!getState().user) return navigateTo('/user/signin', this);
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
    ['[key]', '[key],[placeholder]'].forEach((selector) =>
      this.querySelectorAll(selector).forEach((ele) => {
        if (ele instanceof HTMLInputElement)
          return (ele.placeholder = this.getConstants(ele.getAttribute('key')));
        ele.innerHTML = this.getConstants(ele.getAttribute('key'));
      })
    );
    setState({ language: this.language });
  }

  handleSubmit = async (e) => {
    e.preventDefault();
    this.checkInputs();
    if (this.isValid) {
      await changePasswordRequest({
        username: this.username,
        currentPassword: this.current.value,
        newPassword: this.new.value,
        retypedPassword: this.confirm.value,
      })
        .then((data) => emitMessageEvent(this.getConstants(data.message)))
        .catch((error) =>
          emitMessageEvent(this.getConstants(error.message), 'error')
        );
      this.clearDataInputs();
      this.clearStyleInputs();
    }
    this.isValid = true;
  };

  checkInputs = () => {
    const getInput = (name) => this.querySelector(`input[name=${name}]`);
    ['current', 'new', 'confirm'].forEach((input) => {
      let state = this[input];
      if (!state.test(state.value)) {
        this.isValid = false;
        return setInvalidFor(getInput(input), state.message());
      }
      setValidFor(getInput(input));
    });
  };

  clearDataInputs() {
    this.querySelectorAll('input[type=password]').forEach((input) => {
      input.value = '';
      this[input.name].value = '';
    });
  }

  clearStyleInputs() {
    this.querySelectorAll('.form-control').forEach((div) =>
      div.classList.remove('valid', 'invalid')
    );
  }

  revealPassword = ({ target }) =>
    (target.parentElement.querySelector('input').type = 'text');

  hidePassword = ({ target }) =>
    (target.parentElement.querySelector('input').type = 'password');

  getComponent() {
    return (
      <>
        <div class="row">
          <h3 key="change-password">{this.getConstants('change-password')}</h3>
        </div>
        <div class="row">
          <form onSubmit={this.handleSubmit} role="form" class="form">
            <input
              name="username"
              autoComplete="username"
              type="hidden"
              value={this.username}
            />
            <div class="col-md-offset-4 col-md-4">
              <div class="form-control">
                <input
                  key="current"
                  name="current"
                  type="password"
                  autoComplete="current-password"
                  value={this.current.value}
                  onKeyUp={({ target: { value } }) =>
                    (this.current.value = value)
                  }
                  placeholder={this.getConstants('current')}
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
                <small key="required"></small>
              </div>
            </div>
            <div class="col-md-offset-4 col-md-4">
              <div class="form-control">
                <input
                  key="new"
                  name="new"
                  type="password"
                  autoComplete="new-password"
                  value={this.new.value}
                  onKeyUp={({ target: { value } }) => (this.new.value = value)}
                  placeholder={this.getConstants('new')}
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
                <small key="minCurrent"></small>
              </div>
            </div>
            <div class="col-md-offset-4 col-md-4">
              <div class="form-control">
                <input
                  key="confirm"
                  name="confirm"
                  type="password"
                  autoComplete="new-password"
                  onKeyUp={({ target: { value } }) =>
                    (this.confirm.value = value)
                  }
                  value={this.confirm.value}
                  placeholder={this.getConstants('confirm')}
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
                <small key="confirmPattern"></small>
              </div>
            </div>

            <div class="form-group row">
              <div class="col-sm-12">
                <button key="update" class="btn-signin" type="submit">
                  {this.getConstants('update')}
                </button>
              </div>
            </div>
          </form>
        </div>
      </>
    );
  }
}
!customElements.get('change-password') &&
  customElements.define('change-password', ChangePassword);
