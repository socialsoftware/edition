let modalHtml;

window.addEventListener('pointermove', loadModalHtml, { once: true });

async function loadModalHtml() {
  if (modalHtml) return;
  modalHtml = (await import('./components/modal.js')).default;
}

export class LdodModal extends HTMLElement {
  constructor() {
    super();
    this.serverRendered = true;
    if (!this.shadowRoot) {
      this.attachShadow({ mode: 'open' });
      this.serverRendered = false;
    }
  }

  get show() {
    return this.hasAttribute('show');
  }

  get enableDocOverflow() {
    return this.hasAttribute('document-overflow');
  }

  get noFooter() {
    return this.hasAttribute('no-footer');
  }

  get dialogClass() {
    return this.getAttribute('dialog-class');
  }

  get zIndex() {
    return this.getAttribute('z-index');
  }

  static get observedAttributes() {
    return ['show'];
  }
  async connectedCallback() {
    !this.serverRendered && (await this.render());
    this.hydrate();
  }

  async render() {
    if (!modalHtml) await loadModalHtml();
    this.shadowRoot.innerHTML = modalHtml(this.dialogClass, this.noFooter);
  }

  hydrate() {
    this.addEventListeners();
  }

  addEventListeners() {
    this.shadowRoot.querySelector('div#ldod-modal').style.zIndex = this.zIndex;
    this.shadowRoot.querySelector('button.btn-close').onclick =
      this.handleCloseModal;
  }

  attributeChangedCallback(name, oldV, newV) {
    if (name === 'show') this.handleToggleShow();
  }

  disconnectedCallback() {}

  setPageOverflow = (hide = true) =>
    document.querySelector('html').toggleAttribute('open', hide);

  handleToggleShow = () => {
    const modal = this.shadowRoot.querySelector('div#ldod-modal');
    if (!modal) return;
    if (!this.enableDocOverflow) {
      document.body.classList.toggle('modal-open');
      this.show && this.setPageOverflow();
    }

    modal.setAttribute('aria-hidden', !this.show);
    !this.show && this.onClose();
  };

  handleCloseModal = () => {
    this.toggleAttribute('show', false);
  };

  onClose = () => {
    !this.enableDocOverflow && this.setPageOverflow(false);
    this.dispatchEvent(
      new CustomEvent('ldod-modal-close', {
        detail: { id: this.id },
        bubbles: true,
        composed: true,
      })
    );
  };
}
!customElements.get('ldod-modal') &&
  customElements.define('ldod-modal', LdodModal);
