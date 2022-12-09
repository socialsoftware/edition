import { getCategoryData } from '../api-requests';
import thisConstants from '../constants';
import constants from '../../constants';
import style from '../ve-style.css?inline';
import CategoryVeComponent from './category-ve-component';

export class LdodVeCategory extends HTMLElement {
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

  get name() {
    return this.category?.name;
  }
  get title() {
    return this.category?.title;
  }
  get acronym() {
    return this.category?.acronym;
  }

  fetchData = async ({ acrn, cat }) => {
    await getCategoryData(acrn, cat)
      .then((data) => (this.category = data))
      .catch((error) => console.error(error));
  };

  getConstants = (key) => this.constants[this.language][key];

  async connectedCallback() {
    await this.fetchData(history.state);
    this.render();
  }

  render = () => {
    this.appendChild(<style>{style}</style>);
    this.appendChild(<CategoryVeComponent node={this} />);
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
!customElements.get('ldod-ve-category') &&
  customElements.define('ldod-ve-category', LdodVeCategory);
