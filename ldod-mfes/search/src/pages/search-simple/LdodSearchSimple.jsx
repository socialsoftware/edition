import constants from '../../constants';
import SearchComponent from './SearchComponent';
import { simpleSearchRequest } from '../../apiRequests';
import style from '@src/style.css?inline';

import.meta.env.DEV
  ? await import('shared/table-dev.js')
  : await import('shared/table.js');

export class LdodSearchSimple extends HTMLElement {
  constructor() {
    super();
  }
  static get observedAttributes() {
    return ['language'];
  }

  get language() {
    return this.getAttribute('language');
  }

  get numberOfFragments() {
    return new Set(this.data?.map((inter) => inter.xmlId)).size;
  }

  get numberOfInters() {
    return this.data?.length;
  }

  getConstants(key, args) {
    return constants(this.numberOfFragments, this.numberOfInters)[
      this.language
    ][key];
  }
  connectedCallback() {
    this.render();
  }

  render() {
    this.appendChild(<style>{style}</style>);
    this.appendChild(
      <>
        <h3 class="text-center" data-search-key="searchSimple">
          {this.getConstants('searchSimple')}
        </h3>
        <SearchComponent node={this} />
        <br />
        <br />
        <div id="search-tableContainer"></div>
      </>
    );
  }

  renderTable() {
    const tableData = () => {
      return this.data.map((inter) => {
        return {
          externalId: inter.externalId,
          fragments: (
            <a is="nav-to" to={`/text/fragment/${inter.xmlId}`}>
              {inter.title}
            </a>
          ),
          interpretations: (
            <a
              is="nav-to"
              to={`/text/fragment/${inter.xmlId}/inter/${inter.urlId}`}>
              {`${inter.shortName || inter.interTitle} ${
                inter.acronym ? `(${inter.acronym})` : ''
              }`}
            </a>
          ),
          search: JSON.stringify(inter),
        };
      });
    };

    this.querySelector('div#search-tableContainer').replaceWith(
      <div id="search-tableContainer">
        <hr />
        <ldod-table
          id="search-simpleTable"
          classes="table table-bordered table-hover"
          headers={constants().headers}
          data={tableData()}
          constants={
            constants(this.numberOfFragments, this.numberOfInters)[
              this.language
            ]
          }
          data-searchkey="externalId"></ldod-table>
      </div>
    );
  }

  attributeChangedCallback(name, oldV, newV) {
    this.onChangeAttribute[name](oldV, newV);
  }

  onChangeAttribute = {
    language: (oldV, newV) => {
      oldV &&
        oldV !== newV &&
        this.querySelectorAll('[data-search-key]').forEach((element) => {
          element.innerHTML = this.getConstants(element.dataset.searchKey);
        });
    },
  };

  disconnectedCallback() {}

  searchRequest = async (e) => {
    e.preventDefault();
    const searchBody = Object.fromEntries(new FormData(e.target));
    this.data = await simpleSearchRequest(searchBody);
    this.renderTable();
  };
}
!customElements.get('ldod-search-simple') &&
  customElements.define('ldod-search-simple', LdodSearchSimple);
