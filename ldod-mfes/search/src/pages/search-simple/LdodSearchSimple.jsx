import constants from '../../constants';
import SearchComponent from './SearchComponent';

export class LdodSearchSimple extends HTMLElement {
  constructor() {
    super();
  }
  static get observedAttributes() {}

  get language() {
    return this.getAttribute('language');
  }

  getConstants(key) {
    return constants[this.language][key];
  }
  connectedCallback() {
    this.render();
  }

  render() {
    this.appendChild(
      <>
        <h3 class="text-center" data-search-key="searchSimple">
          {this.getConstants('searchSimple')}
        </h3>
        <SearchComponent />
      </>
    );
  }

  attributeChangedCallback() {}

  disconnectedCallback() {}
}
!customElements.get('ldod-search-simple') &&
  customElements.define('ldod-search-simple', LdodSearchSimple);
