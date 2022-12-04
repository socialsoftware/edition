import {
  ALPHABETIC_REGEX,
  ALPHANUMERIC_REGEX,
  EMAIL_REGEX,
  PASSWORD_REGEX,
} from '@src/resources/constants.js';
import { setInvalidFor, setValidFor, loadConstants } from '@src/utils';

import { signupRequest } from '@src/apiRequests';
import { navigateTo } from 'shared/router.js';
import { setState } from '@src/store';
import { errorEvent, messageEvent } from '../../utils';
import SignupForm from './SignupForm';
import { userReferences } from '../../userReferences';

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
      this[state].value = history.state?.[state] ?? '';
    });
  }

  async connectedCallback() {
    await this.setConstants();
    this.importHistoryState();
    this.appendChild(<SignupForm node={this} />);
  }

  attributeChangedCallback(name, oldValue, newValue) {
    if (name === 'language' && oldValue && newValue !== oldValue) {
      this.updateLanguage();
    }
  }

  disconnectedCallback() { }

  setConstants = async () =>
    (this.constants = await loadConstants(this.language));

  async updateLanguage() {
    await this.setConstants();
    ['[data-key]'].forEach((selector) =>
      this.querySelectorAll(selector).forEach(
        (ele) => (ele.innerHTML = this.getConstants(ele.dataset.key))
      )
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
        .then((res) => {
          if (res.ok) {
            this.dispatchEvent(messageEvent(this.getConstants(res.message)));
            this.clearDataInputs();
            return navigateTo(userReferences.signin());
          }
          this.dispatchEvent(errorEvent(this.getConstants(res.message)));
        })
        .catch((error) =>
          this.dispatchEvent(errorEvent(this.getConstants(error.message)))
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
}
!customElements.get('sign-up') && customElements.define('sign-up', SignUp);
