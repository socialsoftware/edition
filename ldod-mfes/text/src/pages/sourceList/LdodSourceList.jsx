export class LdodSourceList extends HTMLElement {
  constructor() {
    super();
  }
  static get observedAttributes() {}
  connectedCallback() {}
  attributeChangedCallback() {}
  disconnectedCallback() {}
}
!customElements.get('ldod-source-list') &&
  customElements.define('ldod-source-list', LdodSourceList);
