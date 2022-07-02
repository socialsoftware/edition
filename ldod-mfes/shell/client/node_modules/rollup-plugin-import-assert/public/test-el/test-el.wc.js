// import styles from './test-el.wc.css' assert { type: 'css' };

const template = document.createElement('template');
template.innerHTML = `<span class="foo">TestEl, after clicking this button, the text should be blue</span>
<button>Update styles</button>`;

class TestEl extends HTMLElement {
  constructor() {
    super();
    const root = this.attachShadow({ mode: 'open' });
    this.addStyles = this.addStyles.bind(this);
  }

  connectedCallback() {
    const content = template.content.cloneNode(true);

    this.shadowRoot.append(content);
    this.shadowRoot.querySelector('button').addEventListener('click', this.addStyles);
  }

  addStyles(event) {
    import('./test-el.wc.css', { assert: { type: 'css' }})
      .then(module => module.default)
      .then(sheet => this.shadowRoot.adoptedStyleSheets = [ sheet ]);
    event.target.removeEventListener('click', this.addStyles);
    event.target.remove();
  }
}

customElements.define('test-el', TestEl);
