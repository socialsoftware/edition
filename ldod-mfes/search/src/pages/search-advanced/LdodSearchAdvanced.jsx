export class LdodSearchAdvanced extends HTMLElement {
  constructor() {
    super();
  }
  static get observedAttributes() {}
  connectedCallback() {}
  attributeChangedCallback() {}
  disconnectedCallback() {}
}
!customElements.get('ldod-search-adv') &&
  customElements.define('ldod-search-adv', LdodSearchAdvanced);
