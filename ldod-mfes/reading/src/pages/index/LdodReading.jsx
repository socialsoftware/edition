export class LdodReading extends HTMLElement {
  constructor() {
    super();
  }
  static get observedAttributes() {}
  connectedCallback() {}
  attributeChangedCallback() {}
  disconnectedCallback() {}
}
!customElements.get('ldod-reading') &&
  customElements.define('ldod-reading', LdodReading);
