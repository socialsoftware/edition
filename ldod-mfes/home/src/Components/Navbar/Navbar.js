import 'shared/router.js';
import { parseHTML } from 'shared/utils.js';
import headers from '../../../resources/navbar/headers-menus.js';
import style from '../../../style/navbar.css' assert { type: 'css' };
import './DropdownMenu.js';
import './LangMenu.js';
import { checkUserCompCompliance, isMFEAvailable } from '../../utils.js';

const styleSheet = new CSSStyleSheet();
window.html = String.raw;
const loadConstants = async (lang) =>
  (await import(`../../../resources/navbar/constants-${lang}.js`)).default;

export default class LdodNavbar extends HTMLElement {
  constructor() {
    super();
    const shadow = this.attachShadow({ mode: 'open' });
    this.constants = undefined;
    this.user = undefined;
    shadow.adoptedStyleSheets = [styleSheet];
  }

  static get observedAttributes() {
    return ['language'];
  }

  get language() {
    return this.getAttribute('language');
  }

  set language(language) {
    this.setAttribute('language', language);
  }

  get token() {
    return this.getAttribute('token');
  }

  getUser() {
    return this.user;
  }

  isAdmin() {
    return this.getUser()?.admin;
  }

  async connectedCallback() {
    styleSheet.replaceSync(style);
    if (!styleSheet.cssRules.length)
      this.shadowRoot.adoptedStyleSheets = [style];
    this.shadowRoot.addEventListener('ldod-login', this.onUserLogin);
    this.language = this.language;
    await this.setConstants();
    await this.render();
    this.addDropdownEventListeners();
  }

  disconnectedCallback() {
    this.shadowRoot.removeEventListener('ldod-login', this.onUserLogin);
  }

  async attributeChangedCallback(name, oldValue, newValue) {
    if (oldValue === newValue || !newValue || !oldValue) return;
    if (name === 'language') {
      await this.onLanguageChange();
      this.componentsTextContextUpdate();
      this.componentsNameUpdate();
      this.componentsItemsUpdate();
      this.componentsLanguageUpdate();
    }
  }

  async onLanguageChange() {
    await this.setConstants();
  }

  componentsTextContextUpdate() {
    this.shadowRoot
      .querySelectorAll('.update-language')
      .forEach((ele) => (ele.textContent = this.constants[ele.id]));
  }

  componentsNameUpdate() {
    this.shadowRoot
      .querySelectorAll('[menu-name]')
      .forEach(
        (ele) => (ele.menuName = this.constants[this.getHeaders(ele.id).name])
      );
  }

  componentsLanguageUpdate() {
    this.shadowRoot
      .querySelectorAll('[language]')
      .forEach((ele) => ele.setAttribute('language', this.language));
  }

  componentsItemsUpdate() {
    this.shadowRoot
      .querySelectorAll('[is=dropdown-menu]')
      .forEach(
        (ele) => (ele.items = this.getItems(this.getHeaders(ele.id).pages))
      );
  }

  async render() {
    await this.setInnerHTML();
    this.shadowRoot
      .querySelector('.navbar-toggle')
      .addEventListener('click', this.toggleEvent);
  }

  toggleEvent() {
    const element = this.getRootNode().querySelector('.navbar-collapse');
    element.ariaExpanded = element.ariaExpanded === 'true' ? 'false' : 'true';
  }

  onUserLogin({ detail: { user } }) {
    // TODO: Implement correct logic to check if the user has the Admin Role
    if (user && typeof user === 'object') {
      this.user = user;
      const admin = this.querySelector('nav>div.container>div>ul>li#admin');
      admin.ariaHidden = !user.admin;
    }
  }

  getHeaders(selector) {
    return headers[selector];
  }

  async setConstants() {
    this.constants = await loadConstants(this.language);
  }

  async setInnerHTML() {
    const nav = parseHTML(
      html`
        <nav
          role="navigation"
          class="ldod-navbar navbar-default navbar navbar-fixed-top"
        >
          <div class="container-fluid">
            <div class="container">
              <div class="navbar-header">
                <button type="button" class="navbar-toggle">
                  <span class="icon-bar"></span>
                  <span class="icon-bar"></span>
                  <span class="icon-bar"></span>
                </button>
                <a
                  is="nav-to"
                  id="header_title"
                  to="/"
                  class="navbar-brand update-language"
                >
                  ${this.constants['header_title']}
                </a>
                <ul class="nav navbar-nav user-component hidden-xs">
                  ${isMFEAvailable('user') && (await checkUserCompCompliance())
                    ? html`
                        <li
                          class="dropdown"
                          is="user-component"
                          language=${this.language}
                        ></li>
                      `
                    : ''}
                </ul>
              </div>
            </div>
          </div>
          <div class="container">
            <div class="navbar-collapse collapse" aria-expanded="false">
              <ul class="nav navbar-nav navbar-nav-flex">
                ${isMFEAvailable('user') && (await checkUserCompCompliance())
                  ? html` <li
                      class="dropdown visible-xs"
                      is="user-component"
                      language=${this.language}
                    ></li>`
                  : ''}
                <li is="lang-menu" language=${this.language}></li>
              </ul>
            </div>
          </div>
        </nav>
      `
    );
    this.appendDropdownMenus(
      nav.querySelector('ul.navbar-nav-flex'),
      nav.querySelector('li.visible-xs')
    );
    this.shadowRoot.appendChild(nav);
  }

  appendDropdownMenus(node, ref) {
    Object.entries(headers).forEach(([key, value]) => {
      if (!value) return;
      const { name, pages } = value;
      const dropdownMenu = parseHTML(html`<li
        is="dropdown-menu"
        id=${key}
        language=${this.language}
        menu-name=${this.constants[name]}
        aria-hidden=${key === 'admin' && !this.isAdmin()}
      ></li>`);
      dropdownMenu.items = this.getItems(pages);
      node.insertBefore(dropdownMenu, ref);
    });
  }

  getItems(pages) {
    return pages.filter(Boolean).map(({ id, route, link }) => ({
      id,
      name: this.constants[id],
      route,
      link,
    }));
  }

  getDropdownElements() {
    return this.shadowRoot.querySelectorAll('.dropdown');
  }

  addDropdownEventListeners() {
    this.getDropdownElements().forEach((drop) => {
      this.bindHandlers(drop);
      drop.addEventListener('click', this.handleDropdownClick);
    });
  }

  bindHandlers(drop) {
    drop.handleClickNavbar = this.handleClickNavbar.bind(drop);
    drop.handleDocumentClick = this.handleDocumentClick.bind(drop);
    drop.removeDropdownEventListeners =
      this.removeDropdownEventListeners.bind(drop);
  }

  handleDropdownClick() {
    if (isOpen(this)) return close(this);

    this.getRootNode()
      .querySelectorAll('.dropdown')
      .forEach((drop) => close(drop));

    this.getRootNode().addEventListener('click', this.handleClickNavbar);
    document.addEventListener('click', this.handleDocumentClick);
    open(this);
  }

  handleClickNavbar(e) {
    if (
      [...this.getRootNode().querySelectorAll('a.dropdown-toggle')].some(
        (drop) => drop === e.target
      ) ||
      [...this.getRootNode().querySelectorAll('li.nav-lang>a')].some(
        (lang) => lang === e.target
      )
    )
      return;
    this.removeDropdownEventListeners();
    close(this);
  }

  handleDocumentClick(e) {
    if (e.target instanceof customElements.get('ldod-navbar')) return;
    this.removeDropdownEventListeners();
    close(this);
  }

  removeDropdownEventListeners() {
    this.getRootNode().removeEventListener('click', this.handleClickNavbar);
    document.removeEventListener('click', this.handleDocumentClick);
  }
}

customElements.define('ldod-navbar', LdodNavbar);

const isOpen = (node) => node.classList.contains('open');
const open = (node) => node.classList.add('open');
const close = (node) => node.classList.remove('open');
