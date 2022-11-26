import style from '../style.css?inline';
import UploadComponent from './UploadComponent.js';
import { xmlFileFetcher } from '@dist/fetcher.js';
import { parseHTML } from '@dist/utils.js';
import { dispatchCustomEvent } from '../utils';
window.html = String.raw;

export class LdodUpload extends HTMLElement {
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

  get isMultiple() {
    return this.hasAttribute('multiple');
  }
  get width() {
    return this.getAttribute('width') ?? '';
  }
  connectedCallback() {
    this.render();
  }

  render() {
    this.shadowRoot.appendChild(
      UploadComponent({ node: this, isMultiple: this.isMultiple })
    );
  }

  handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const res = await xmlFileFetcher({
      url: this.dataset.url,
      method: 'POST',
      body: formData,
    });

    if (res?.ok !== undefined) {
      dispatchCustomEvent(
        this,
        { message: res.message },
        {
          type: res.ok ? 'message' : 'error',
          bubbles: true,
          composed: true,
        }
      );
    }

    if (!res?.ok || res.ok)
      dispatchCustomEvent(
        this,
        { data: res },
        {
          type: 'file-uploaded',
          bubbles: true,
          composed: true,
        }
      );
  };

  handleInput = (e) => {
    const toggleDisabled = (value) =>
      this.shadowRoot
        .querySelector('#loadBtn')
        .toggleAttribute('disabled', value);
    if (e.target.value.endsWith('.xml') || e.target.value.endsWith('.XML')) {
      return toggleDisabled(false);
    }
    toggleDisabled(true);
  };

  attributeChangedCallback(name, oldV, newV) {
    oldV && oldV !== newV && this.handleChangeAttribute[name]();
  }

  handleChangeAttribute = {
    title: (o) => {
      this.shadowRoot.querySelector('button#loadBtn>span[label]').textContent =
        this.title;
    },
  };

  disconnectedCallback() {}
}
!customElements.get('ldod-upload') &&
  customElements.define('ldod-upload', LdodUpload);
