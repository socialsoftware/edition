import { parseHTML } from 'shared/utils.js';
import style from './loading-modal.css' assert { type: 'css' };
window.html = String.raw;

const styleSheet = new CSSStyleSheet();

export class LdodLoading extends HTMLElement {
  constructor() {
    super();
    const shadow = this.attachShadow({ mode: 'open' });
    shadow.adoptedStyleSheets = [styleSheet];
  }
  static get observedAttributes() {}

  connectedCallback() {
    styleSheet.replaceSync(style);
    if (!styleSheet.cssRules.length)
      this.shadowRoot.adoptedStyleSheets = [style];
    this.render();
    this.addEventListener();
  }

  attributeChangedCallback() {}
  disconnectedCallback() {
    window.removeEventListener('ldod-loading', this.handleLoadingEvent);
  }

  render() {
    const loader = parseHTML(
      html` <div>
        <div id="overlay" class="overlay-modal" aria-hidden="true">
          <div class="lds-dual-ring"></div>
        </div>
      </div>`
    );
    this.shadowRoot.appendChild(loader);
  }

  addEventListener = () => {
    window.addEventListener('ldod-loading', this.handleLoadingEvent);
  };

  handleLoadingEvent = ({ detail: { isLoading } }) => {
    this.shadowRoot
      .querySelector('#overlay')
      .setAttribute('aria-hidden', String(!isLoading));
  };
}

customElements.define('ldod-loading', LdodLoading);
