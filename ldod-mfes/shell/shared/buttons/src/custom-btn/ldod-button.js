import style from '../style.css?inline';
import { parseHTML } from 'shared/utils.js';
import { html } from '../utils';

const styleElement = () =>
  parseHTML(
    html`<style>
      ${style}
    </style>`
  );

export class LdodButton extends HTMLElement {
  constructor() {
    super();
    this.attachShadow({ mode: 'open' });
    this.shadowRoot.appendChild(styleElement());
  }

  get class() {
    return this.getAttribute('class') ?? '';
  }

  get type() {
    return this.getAttribute('type') ?? '';
  }

  get btnId() {
    return this.dataset.btnid;
  }

  get title() {
    return this.getAttribute('title');
  }

  get button() {
    let id = this.btnId ? `#${this.btnId}` : '';
    return this.shadowRoot.querySelector(`button${id}`);
  }

  static get observedAttributes() {
    return ['title'];
  }

  connectedCallback() {
    this.shadowRoot.appendChild(parseHTML(this.getButtonComponent()));
    this.addEventListeners();
  }

  getButtonComponent() {
    return html`<button
      id=${this.btnId}
      class="${this.class}"
      type="${this.type}"
    >
      ${this.title}
    </button>`;
  }

  addEventListeners() {
    typeof this.handlers === 'object' &&
      Object.entries(this.handlers).forEach(([event, listener]) => {
        this.button.addEventListener(event, listener);
      });
  }

  attributeChangedCallback(name, oldV, newV) {
    if (oldV && oldV !== newV) this.button.textContent = newV;
  }

  disconnectedCallback() {}
}

!customElements.get('ldod-button') &&
  customElements.define('ldod-button', LdodButton);
