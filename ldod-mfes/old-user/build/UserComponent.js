import { createElement, createFragment } from 'shared/vanilla-jsx.js';
import constants from '../resources/constants.js';
window.html = String.raw;

const getUser = () => ({
  name: 'João Raimundo',
  admin: false
});

const getUserComponentNotAuthenticated = () => createElement("a", {
  id: "login",
  class: "login update-language"
});

const getUserComponentAuthenticated = () => createElement(createFragment, null, createElement("a", {
  id: "login",
  class: "dropdown-toggle"
}, getUser().name, createElement("span", {
  class: "caret"
})), createElement("ul", {
  class: "dropdown-menu"
}, createElement("li", null, createElement("a", {
  class: "update-language",
  id: "logout"
})), createElement("li", null, createElement("a", {
  class: "update-language",
  id: "password"
}))));

export class UserComponent extends HTMLLIElement {
  constructor() {
    super();
  }

  get language() {
    return this.getAttribute('language') ?? 'en';
  }

  set language(language) {
    this.setAttribute('language', language);
  }

  get token() {
    return this.getAttribute('token') ?? 'false';
  }

  set token(token) {
    this.setAttribute('token', token);
  }

  get master() {
    return this.hasAttribute('master');
  }

  static get observedAttributes() {
    return ['language', 'token'];
  }

  connectedCallback() {
    this.render();
  }

  attributeChangedCallback(name, oldValue, newValue) {
    if (oldValue && newValue && oldValue !== newValue) {
      name === 'token' && this.updateComponent();
      name === 'language' && this.updateLanguage(newValue);
    }
  }

  disconnectedCallback() {
    this.removeAnchors();
  }

  removeAnchors() {
    this.querySelectorAll('a').forEach(anchor => anchor.removeEventListener('click', this.handlers(anchor.id).handler));
    this.innerHTML = '';
  }

  render() {
    this.token === 'true' ? this.appendChild(getUserComponentAuthenticated()) : this.appendChild(getUserComponentNotAuthenticated());
    this.listeners();
    this.updateLanguage();
    this.userLoginEvent();
  }

  updateComponent() {
    this.removeAnchors();
    this.render();
  }

  listeners() {
    ['logout', 'password', 'login'].forEach(id => this.querySelector(`#${this.handlers(id).id}`)?.addEventListener('click', this.handlers(id).handler));
  }

  updateLanguage(language = this.language) {
    this.querySelectorAll('a.update-language').forEach(ele => ele.innerHTML = constants[language][ele.id]);
  }

  userLoginEvent(token = this.token) {
    if (this.master) {
      const detail = {
        token,
        user: token === 'true' ? getUser() : undefined
      };
      this.getRootNode().dispatchEvent(new CustomEvent('ldod-user-event', {
        detail
      }));
    }
  }

  handlers(id) {
    const getUserComponents = () => this.getRootNode().querySelectorAll('li#user-component');

    return {
      login: {
        id: 'login',
        handler: () => getUserComponents().forEach(ele => ele.setAttribute('token', 'true'))
      },
      logout: {
        id: 'logout',
        handler: () => getUserComponents().forEach(ele => ele.setAttribute('token', 'false'))
      },
      password: {
        id: 'password',
        handler: () => console.log('password changed')
      }
    }[id];
  }

}
customElements.define('user-component', UserComponent, {
  extends: 'li'
});