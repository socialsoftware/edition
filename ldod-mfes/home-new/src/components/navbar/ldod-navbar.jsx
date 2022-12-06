import Navbar from "./navbar";

export class LdodNavbar extends HTMLElement {
  constructor() {
    super();
    this.attachShadow({ mode: "open" })
  }
  static get observedAttributes() { }
  connectedCallback() {
    this.shadowRoot.appendChild(<Navbar />)
  }

  attributeChangedCallback() { }
  disconnectedCallback() { }
}
!customElements.get('ldod-navbar') && customElements.define('ldod-navbar', LdodNavbar);