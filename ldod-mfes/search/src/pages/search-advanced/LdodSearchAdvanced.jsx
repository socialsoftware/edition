import style from '@src/style.css?inline';
import thisStyle from './style.css?inline';

import constants from './constants';
import { getAdvSearchDto } from '../../apiRequests';
import './components/CriteriaForm';

export class LdodSearchAdvanced extends HTMLElement {
  constructor() {
    super();
    this.attachShadow({ mode: 'open' });
    this.constants = constants;
    this.sequentialId = 1;
  }

  get language() {
    return this.getAttribute('language');
  }

  static get observedAttributes() {
    return ['language'];
  }

  getConstants(key) {
    return this.constants[this.language][key];
  }

  CriteriaForm = () => <form is="criteria-form" root={this}></form>;

  fetchData = async () => {
    getAdvSearchDto()
      .then((data) => (this.data = data))
      .catch((e) => console.error(e));
  };

  async connectedCallback() {
    await this.fetchData();
    this.shadowRoot.appendChild(
      <style>
        {style}
        {thisStyle}
      </style>
    );
    this.render();
  }

  render() {
    this.shadowRoot.appendChild(
      <>
        <h3 class="text-center" data-search-key="advancedSearch">
          {this.getConstants('advancedSearch')}
        </h3>
        <div id="main">
          <select class="form-select" name="mode">
            <option value="and" data-search-key="criteriaAnd">
              {this.getConstants('criteriaAnd')}
            </option>
            <option value="or" data-search-key="criteriaOr">
              {this.getConstants('criteriaOr')}
            </option>
          </select>
        </div>
        <div id="criterias-container">
          <this.CriteriaForm />
        </div>
        <hr></hr>
        <div id="actions">
          <button
            class="btn btn-outline-secondary"
            data-search-key="search"
            onClick={this.onSearch}>
            <span class="icon icon-magnifying-glass"></span>
            {this.getConstants('search')}
          </button>
          <button
            class="btn btn-outline-secondary"
            id="add-criteria"
            onClick={this.addCriteria}>
            <span class="icon icon-plus"></span>
          </button>
        </div>
      </>
    );
  }

  addCriteria = () => {
    ++this.sequentialId;
    this.shadowRoot
      .querySelector('#criterias-container')
      .appendChild(<this.CriteriaForm />);
  };

  attributeChangedCallback(name, oldV, newV) {
    this.onChangeAttribute[name]();
  }

  onChangeAttribute = {
    language: () => this.onLanguage(),
  };

  onLanguage = () => {
    this.shadowRoot.querySelectorAll('[data-search-key]').forEach((element) => {
      element.firstChild.textContent = this.getConstants(
        element.dataset.searchKey
      );
    });
  };

  onSearch = () => {
    console.log(this.shadowRoot.querySelectorAll('form'));
    this.shadowRoot.querySelectorAll('form').forEach((form) => {
      const formData = Object.fromEntries(new FormData(form));
      console.log(formData);
    });
  };

  disconnectedCallback() {}
}
!customElements.get('ldod-search-adv') &&
  customElements.define('ldod-search-adv', LdodSearchAdvanced);
