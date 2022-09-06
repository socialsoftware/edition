export class LdodSources extends HTMLElement {
  constructor() {
    super();
  }
  static get observedAttributes() {}
  connectedCallback() {}
  attributeChangedCallback() {}
  disconnectedCallback() {}
}
!customElements.get('ldod-sources') &&
  customElements.define('ldod-sources', LdodSources);
