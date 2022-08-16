import modalStyle from '@src/modal.css?inline';
import { dom } from '../../dist/utils.js';
window.html = String.raw;

const getStyle = () =>
  dom(
    html`<style>
      ${modalStyle}
    </style>`
  );

export class LdodModal extends HTMLElement {
  constructor() {
    super();
    this.attachShadow({ mode: 'open' });
  }

  get show() {
    return this.hasAttribute('show');
  }

  get dialogClass() {
    return this.getAttribute('dialog-class');
  }

  get templateContent() {
    return this.getElementsByTagName('template')[0].content.cloneNode(true);
  }

  static get observedAttributes() {
    return ['show'];
  }
  connectedCallback() {
    this.shadowRoot.append(getStyle(), this.getComponent());
  }
  attributeChangedCallback(name, oldV, newV) {
    if (name === 'show') this.handleToggleShow();
  }
  disconnectedCallback() {}

  handleToggleShow = () => {
    const modal = this.shadowRoot.querySelector('div#ldod-modal');
    if (!modal) return;

    document.body.classList.toggle('modal-open');
    modal.ariaHidden = !this.show;
  };

  handleCloseModal = () => {
    this.toggleAttribute('show');
  };

  getComponent() {
    const component = dom(html` <div
      class="modal"
      id="ldod-modal"
      tabindex="-1"
      role="dialog"
      aria-hidden="true"
      aria-labelledby="ldod.modal-label"
    >
      <div class=${`"modal-dialog ${this.dialogClass}"`}>
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="exampleModalLabel">
              <slot name="header-slot">Modal Title</slot>
            </h5>
            <button
              type="button"
              class="btn-close"
              onClick="{this.handleCloseModal}"
              data-bs-dismiss="modal"
              aria-label="Close"
            ></button>
          </div>
          <div class="modal-body">
            <slot name="body-slot"></slot>
          </div>
          <div class="modal-footer">
            <slot name="footer-slot"></slot>
          </div>
        </div>
      </div>
    </div>`);

    component.querySelector('button.btn-close').onclick = this.handleCloseModal;

    return component;
  }
}
!customElements.get('ldod-modal') &&
  customElements.define('ldod-modal', LdodModal);
