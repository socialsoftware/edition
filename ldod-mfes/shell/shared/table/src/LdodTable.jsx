import tableStyle from './table.css?inline';
import toolsStyle from './tools.css?inline';
import { sleep } from 'shared/utils.js';

export class LdodTable extends HTMLElement {
  constructor() {
    super();
    this.lastIndex = 0;
  }

  get classes() {
    return this.getAttribute('classes');
  }

  get numberOfVisibleRows() {
    return +(this.dataset.rows ?? 20);
  }

  get isFullyLoaded() {
    return (
      this.data.length <= this.numberOfVisibleRows ||
      this.lastIndex === this.data.length
    );
  }

  get interval() {
    return this.data.length < this.numberOfVisibleRows
      ? this.data.length
      : this.numberOfVisibleRows;
  }

  get language() {
    return this.getAttribute('language');
  }

  get searchKey() {
    return this.dataset.searchkey;
  }

  get targetToObserve() {
    let rows = this.querySelectorAll('table>tbody>tr');
    let nrOfTr = rows.length;
    return rows[Math.floor(nrOfTr * 0.75)];
  }

  static get observedAttributes() {
    return ['language'];
  }

  get searchedRows() {
    return this.allRows.filter((row) => row.hasAttribute('searched'));
  }
  get unSearchedRows() {
    return this.allRows.filter((row) => !row.hasAttribute('searched'));
  }
  get allRows() {
    return Array.from(this.querySelectorAll('table>tbody>tr'));
  }

  getConstants(key) {
    if (!this.constants) return key;
    const constant = this.language
      ? this.constants[this.language][key]
      : this.constants[key];
    return constant || key;
  }

  render() {
    this.appendChild(this.getTable());
  }

  attributeChangedCallback(name, oldV, newV) {
    if (oldV && oldV !== newV) {
      this.querySelectorAll('[data-key]').forEach((ele) => {
        ele.firstChild.textContent = this.getConstants(ele.dataset.key);
      });
    }
  }

  connectedCallback() {
    this.appendChild(<style>{tableStyle}</style>);
    this.render();
    if (!this.isFullyLoaded) this.addObserver();
    history.state?.searchTerm && this.handleSearchInput();
  }

  obsCallback = ([entry], observer) => {
    if (entry.intersectionRatio > 0) {
      observer.unobserve(entry.target);
      this.addRows();
      if (!this.isFullyLoaded) observer.observe(this.targetToObserve);
    }
  };

  addObserver() {
    const obs = new IntersectionObserver(this.obsCallback);
    obs.observe(this.targetToObserve);
    this.observer = obs;
    return obs;
  }

  addRows = (end) => {
    const newRows = this.getRows(
      this.lastIndex,
      end ?? this.lastIndex + this.interval
    );
    newRows.forEach((row) =>
      this.querySelector('table>tbody').appendChild(row)
    );
    this.dispatchCustomEvent('ldod-table-increased');
  };

  loadSearchStyle() {
    import('./tools.css?inline').then((style) => {
      this.querySelector('#table-tools>style').innerHTML = style.default;
    });
  }

  handleSearchInput = async () => {
    if (this.isSearching) return;
    this.isSearching = true;
    this.dispatchCustomEvent('ldod-loading', { isLoading: true });
    await sleep(10);

    if (!this.isFullyLoaded) {
      this.observer.disconnect();
      this.addRows(this.data.length);
    }

    const searchTerm = this.querySelector('input#table-searchField')
      .value?.trim()
      .toLowerCase()
      .toString();

    history.replaceState(searchTerm ? { searchTerm } : {}, {});
    const result = searchTerm
      ? this.data
          .filter((row) => {
            const search =
              typeof row.search === 'string'
                ? row.search.toLowerCase()
                : row.search.toString().toLowerCase();
            return search.includes(searchTerm);
          })
          .map((row) => row[this.dataset.searchkey].toString())
      : this.data.map((row) => row[this.dataset.searchkey].toString());

    this.querySelectorAll('tbody>tr').forEach((row) => {
      if (result.indexOf(row.id) === -1)
        return row.toggleAttribute('searched', false);
      row.toggleAttribute('searched', true);
    });

    this.dispatchCustomEvent('ldod-table-searched', {
      id: this.id,
      size: result.length,
    });
    this.dispatchCustomEvent('ldod-loading', { isLoading: false });
    this.isSearching = false;
  };

  getSearch() {
    return (
      <input
        id="table-searchField"
        type="search"
        name="search"
        placeholder="search"
        onInput={this.handleSearchInput}
      />
    );
  }

  getRow = (row) => {
    this.lastIndex += 1;
    let entry = row.data && typeof row.data === 'function' ? row.data() : row;
    return (
      <tr searched id={row[this.searchKey] || ''}>
        {this.headers.map((key) => {
          return (
            <td>
              {typeof entry[key] === 'function' ? entry[key]() : entry[key]}
            </td>
          );
        })}
      </tr>
    );
  };

  getRows(start, end) {
    return this.data.slice(start, end).map(this.getRow);
  }

  getTable() {
    return (
      <>
        <div id="table-tools">
          {this.searchKey && (
            <>
              <style>{toolsStyle}</style>
              {this.getSearch()}
            </>
          )}
        </div>
        <div class="table-container">
          <div class="table-body">
            <table id={this.id} class={this.classes}>
              <thead>
                <tr>
                  {this.headers.map((key) => {
                    return (
                      <th class="th-inner" data-key={key}>
                        {this.getConstants(key)}
                      </th>
                    );
                  })}
                </tr>
              </thead>
              {this.data.length ? (
                <tbody>{this.getRows(0, this.interval)}</tbody>
              ) : (
                <tbody></tbody>
              )}
            </table>
          </div>
        </div>
      </>
    );
  }

  dispatchCustomEvent = (event, detail) => {
    this.dispatchEvent(
      new CustomEvent(event, { detail, bubbles: true, composed: true })
    );
  };
}

!customElements.get('ldod-table') &&
  customElements.define('ldod-table', LdodTable);
