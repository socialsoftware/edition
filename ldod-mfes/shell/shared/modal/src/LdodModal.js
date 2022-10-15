import modalStyle from '@src/modal.css?inline';
import overflowStyle from '@src/overflow.css?inline';

import { dom } from '../../dist/utils.js';
window.html = String.raw;

const getStyle = () =>
  dom(
    html`<style>
      ${modalStyle}
    </style>`
  );

const htmlOverflowStyle = () =>
  document.head.querySelector('#html-overflow')
    ? ''
    : dom(
        html`<style id="html-overflow">
          ${overflowStyle}
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
  connectedCallback() {
    const overflowStyle = htmlOverflowStyle();
    overflowStyle && document.head.appendChild(overflowStyle);
    this.shadowRoot.append(getStyle(), this.getComponent());
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

    document.body.classList.toggle('modal-open');
    this.show && this.setPageOverflow();

    modal.ariaHidden = !this.show;
    !this.show && this.onClose();
  };

  handleCloseModal = () => {
    this.toggleAttribute('show', false);
  };

  onClose = () => {
    this.setPageOverflow(false);
    this.dispatchEvent(
      new CustomEvent('ldod-modal-close', { bubbles: true, composed: true })
    );
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
              <slot name="header-slot"></slot>
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
          ${this.noFooter
            ? ''
            : html`<div class="modal-footer">
                <slot name="footer-slot"></slot>
              </div>`}
        </div>
      </div>
    </div>`);
    component.querySelector('div#ldod-modal').style.zIndex = this.zIndex;
    component.querySelector('button.btn-close').onclick = this.handleCloseModal;

    return component;
  }
}
!customElements.get('ldod-modal') &&
  customElements.define('ldod-modal', LdodModal);
