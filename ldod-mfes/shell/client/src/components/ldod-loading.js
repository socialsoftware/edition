import { ldodEventBus } from 'shared/ldod-events.js';

export class LdodLoading extends HTMLElement {
  constructor() {
    super();
    this.pendingLoading = 0;
    this.hidden = true;
    if (!this.shadowRoot) this.attachShadow({ mode: 'open' });
  }
  get element() {
    return this.shadowRoot.querySelector('#shell-loadingOverlay');
  }
  get isLoading() {
    return this.hasAttribute('show');
  }

  handleLoading = async (isLoading) => {
    if (this.pendingLoading > 1 && isLoading) return;
    if (this.pendingLoading !== 0 && !isLoading) return;
    this.toggleVisibility(isLoading);
  };

  toggleVisibility = (isLoading) => (this.hidden = !isLoading);

  connectedCallback() {
    this.addEventListeners();
  }

  disconnectedCallback() {
    this.unsubLoading?.();
  }

  render() {
    import('./loading-html.js').then(
      (data) => (this.shadowRoot.innerHTML = data)
    );
  }

  addEventListeners = () => {
    this.unsubLoading = ldodEventBus.subscribe(
      'ldod:loading',
      this.handleLoadingEvent
    ).unsubscribe;
  };

  handleLoadingEvent = ({ payload: isLoading }) => {
    if (isLoading) ++this.pendingLoading;
    else if (this.pendingLoading > 0) --this.pendingLoading;
    else this.pendingLoading = 0;
    this.handleLoading(isLoading);
  };
}

customElements.define('ldod-loading', LdodLoading);
