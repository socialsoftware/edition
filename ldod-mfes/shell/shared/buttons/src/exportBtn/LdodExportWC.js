import style from '../style.css?inline';
import ExportComponent from './ExportComponent.js';
import { xmlFileFetcher } from '@dist/fetcher.js';
import { parseHTML } from '@dist/utils.js';
window.html = String.raw;

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
    e.preventDefault();
    const res = await xmlFileFetcher({
      url: this.dataset.url,
      method: this.method,
      body: this.method === 'POST' && this.dataset.body,
      headers: [{ 'Content-Type': 'application/json' }],
    });

    const array = [atob(res.xmlData)];
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
