import style from '../style.css?inline';
import ExportComponent from './ExportComponent.js';
import { xmlFileFetcher } from '@dist/fetcher.js';
import { parseHTML } from '@dist/utils.js';
import { dispatchCustomEvent } from '../utils';
window.html = String.raw;

function base64Decoder(str) {
  return decodeURIComponent(
    atob(str)
      .split('')
      .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
      .join('')
  );
}
export class LdodExport extends HTMLElement {
  constructor() {
    super();
    const shadow = this.attachShadow({ mode: 'open' });
    shadow.appendChild(
      parseHTML(
        html`<style>
          ${style}
        </style>`
      )
    );
  }
  static get observedAttributes() {
    return ['title'];
  }

  get title() {
    return this.getAttribute('title');
  }

  get width() {
    return this.getAttribute('width') ?? '';
  }

  get filePrefix() {
    return this.getAttribute('file-prefix');
  }

  get method() {
    return this.getAttribute('method') || 'GET';
  }

  connectedCallback() {
    this.render();
  }

  render() {
    this.shadowRoot.appendChild(ExportComponent({ node: this }));
  }

  handleSubmit = async (e) => {
    console.log(this.body);
    e.preventDefault();
    const res = await xmlFileFetcher({
      url: this.dataset.url,
      method: this.method,
      body: this.method === 'POST' && JSON.stringify(this.body),
      headers: [{ 'Content-Type': 'application/json' }],
    });

    if (!res.xmlData && res.ok !== undefined) {
      return dispatchCustomEvent(
        this,
        { message: res?.message },
        {
          type: res.ok ? 'message' : 'error',
          bubbles: true,
          composed: true,
        }
      );
    }

    const array = [base64Decoder(res.xmlData)];
    const blob = new Blob(array, { type: 'application/xml' });
    const a = document.createElement('a');
    a.href = window.URL.createObjectURL(blob);
    a.download = `${this.filePrefix}-${new Date().getFullYear()}-${
      new Date().getMonth() + 1
    }-${new Date().getDate()}.xml`;
    a.click();
  };

  attributeChangedCallback(name, oldV, newV) {
    oldV && oldV !== newV && this.handleChangeAttribute[name]();
  }

  handleChangeAttribute = {
    title: (o) => {
      this.shadowRoot.querySelector(
        'button#exportBtn>span[label]'
      ).textContent = this.title;
    },
  };

  disconnectedCallback() {}
}
!customElements.get('ldod-export') &&
  customElements.define('ldod-export', LdodExport);
