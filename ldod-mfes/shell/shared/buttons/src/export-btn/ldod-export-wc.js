import style from '../style.css?inline';
import ExportComponent from './ldod-export.js';
import { xmlFileFetcher } from 'shared/fetcher.js';
import { parseHTML } from 'shared/utils.js';
import { errorPublisher } from '../events-module';
import { html } from '../utils';

function base64ToBuffer(data) {
  return new Uint8Array(
    atob(data)
      .split('')
      .map((c) => c.charCodeAt(0))
  ).buffer;
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

  get fileType() {
    return this.getAttribute('file-type');
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
      body: this.method === 'POST' && JSON.stringify(this.body),
      headers: [{ 'Content-Type': 'application/json' }],
    });

    if (res && !res.xmlData && res.ok !== undefined) {
      errorPublisher('error', res.message);
      return;
    }

    const blob = new Blob([base64ToBuffer(res?.xmlData)], {
      type: `application/${this.fileType || 'xml'}`,
    });

    const a = document.createElement('a');
    a.href = window.URL.createObjectURL(blob);
    a.download = `${this.filePrefix}-${new Date().getFullYear()}-${
      new Date().getMonth() + 1
    }-${new Date().getDate()}.${this.fileType || 'xml'}`;
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
