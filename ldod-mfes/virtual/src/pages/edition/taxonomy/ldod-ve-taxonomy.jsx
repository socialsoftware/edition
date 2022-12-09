import { getVeTaxonomy } from '../api-requests';
import thisConstants from '../constants';
import constants from '../../constants';
import style from '../ve-style.css?inline';
import VeTaxonomyComponent from './ve-taxonomy-component';

export class LdodEdTaxonomy extends HTMLElement {
  constructor(lang) {
    super();
    this.language = lang;
    this.constants = Object.entries(thisConstants).reduce(
      (prev, [key, value]) => {
        prev[key] =
          value instanceof Array ? value : { ...constants[key], ...value };
        return prev;
      },
      {}
    );
  }

  set language(lang) {
    this.setAttribute('language', lang);
  }

  get language() {
    return this.getAttribute('language');
  }

  static get observedAttributes() {
    return ['language'];
  }

  fetchData = async () => {
    await getVeTaxonomy(history.state?.acrn)
      .then((data) => (this.taxonomy = data))
      .catch((error) => console.error(error));
  };

  getConstants = (key) => this.constants[this.language][key];

  async connectedCallback() {
    await this.fetchData();
    this.render();
  }

  render = () => {
    this.appendChild(<style>{style}</style>);
    this.appendChild(<VeTaxonomyComponent node={this} />);
  };

  attributeChangedCallback(name, oldV, newV) {
    this.onChangeAttribute[name](oldV, newV);
  }

  onChangeAttribute = {
    language: (oldV, newV) =>
      oldV && oldV !== newV && this.handleChangeLanguage(),
  };

  handleChangeLanguage = () => {
    this.querySelectorAll('[data-virtual-key]').forEach((ele) => {
      ele.firstChild.textContent = this.getConstants(ele.dataset.virtualKey);
    });
  };

  disconnectedCallback() { }
}
!customElements.get('ldod-ede-taxonomy') &&
  customElements.define('ldod-ed-taxonomy', LdodEdTaxonomy);
