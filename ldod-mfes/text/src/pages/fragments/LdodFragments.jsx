export class LdodFragments extends HTMLElement {
  constructor() {
    super();
  }
  static get observedAttributes() {}
  connectedCallback() {}
  attributeChangedCallback() {}
  disconnectedCallback() {}
}
!customElements.get('ldod-fragments') &&
  customElements.define('ldod-fragments', LdodFragments);
