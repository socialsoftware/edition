import { parseHTML } from 'shared/utils.js';
import { ldodEventBus } from 'shared/ldod-events.js';
import style from './loading-modal.css' assert { type: 'css' };
window.html = String.raw;

const styleSheet = new CSSStyleSheet();

export class LdodLoading extends HTMLElement {
  constructor() {
    super();
    this.pendingLoading = 0;
    this.hidden = true;
    const shadow = this.attachShadow({ mode: 'open' });
    shadow.adoptedStyleSheets = [styleSheet];
  }
  static get observedAttributes() {}
  get element() {
    return this.shadowRoot.querySelector('#shell-loadingOverlay');
  }
  get isLoading() {
    return this.hasAttribute('show');
  }

  handleLoading = (isLoading) => {
    if (this.pendingLoading > 1 && isLoading) return;
    if (this.pendingLoading !== 0 && !isLoading) return;
    this.toggleVisibility(isLoading);
  };

  toggleVisibility = (isLoading) => (this.hidden = !isLoading);

  connectedCallback() {
    styleSheet.replaceSync(style);
    if (!styleSheet.cssRules.length)
      this.shadowRoot.adoptedStyleSheets = [style];
    this.render();
    this.addEventListeners();
  }

  disconnectedCallback() {
    this.unsubLoading?.();
  }

  render() {
    const loader = parseHTML(
      html` <div id="shell-loadingOverlay">
        <div class="lds-dual-ring"></div>
      </div>`
    );
    this.shadowRoot.appendChild(loader);
  }

  addEventListeners = () => {
    this.unsubLoading = ldodEventBus.subscribe(
      'ldod:loading',
      this.handleLoadingEvent
    ).unsubscribe;
  };

  handleLoadingEvent = ({ payload: isLoading }) => {
    this.pendingLoading = isLoading
      ? ++this.pendingLoading
      : this.pendingLoading > 0
      ? --this.pendingLoading
      : 0;
    this.handleLoading(isLoading);
  };
}

customElements.define('ldod-loading', LdodLoading);
