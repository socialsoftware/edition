import Title from './components/Title.jsx';
import constants from './constants.js';
import CitationsTable from './components/CitationsTable.jsx';
import.meta.env.DEV
  ? await import('shared/table-dev.js')
  : await import('shared/table.js');

export class LdodCitations extends HTMLElement {
  constructor() {
    super();
  }

  get language() {
    return this.getAttribute('language');
  }

  get numberOfCitations() {
    return this.citations?.length;
  }

  static get observedAttributes() {
    return ['data', 'language'];
  }

  getConstants(key, ...args) {
    const constant = constants[this.language][key];
    return args.length ? constant(...args) : constant;
  }

  connectedCallback() {
    this.render();
  }

  attributeChangedCallback(name, oldV, newV) {
    this.handleChangeAttribute[name](oldV, newV);
  }

  handleChangeAttribute = {
    data: () => {
      this.render();
    },
    language: (oldV, newV) => {
      if (oldV && oldV !== newV) {
        this.querySelectorAll('[data-key]').forEach(
          (node) =>
            (node.firstChild.textContent = node.dataset.args
              ? this.getConstants(node.dataset.key, node.dataset.args)
              : this.getConstants(node.dataset.key))
        );
      }
    },
  };

  render() {
    this.innerHTML = '';
    if (!this.citations) return;
    this.appendChild(
      <>
        <Title
          citationsTitle={this.getConstants('title', this.numberOfCitations)}
          numberOfCitations={this.numberOfCitations}
        />
        <CitationsTable node={this} constants={constants} />
      </>
    );
  }
}
!customElements.get('ldod-citations') &&
  customElements.define('ldod-citations', LdodCitations);
