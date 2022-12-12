import Navbar from './navbar';
import scssStyle from './scssStyle';
import style from './style';

export class LdodNavbar extends HTMLElement {
  constructor() {
    super();
    const sheet = new CSSStyleSheet();
    sheet.replaceSync(`${style}${scssStyle}`);
    document.adoptedStyleSheets = [sheet];
  }
  static get observedAttributes() {}
  connectedCallback() {
    this.appendChild(Navbar);
  }

  attributeChangedCallback() {}
  disconnectedCallback() {}
}
!customElements.get('ldod-navbar') &&
  customElements.define('ldod-navbar', LdodNavbar);
