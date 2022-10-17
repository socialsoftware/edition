import tableStyle from './table.css?inline';

export class LdodTable extends HTMLElement {
  constructor() {
    super();
    this.lastIndex = 0;
  }

  get classes() {
    return this.getAttribute('classes');
  }

  get visibleRows() {
    return +(this.dataset.rows ?? 20);
  }

  get isFullyLoaded() {
    return (
      this.data.length <= this.visibleRows ||
      this.lastIndex === this.data.length
    );
  }

  get interval() {
    return this.data.length < this.visibleRows
      ? this.data.length
      : this.visibleRows;
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

  getConstants(key) {
    return this.language
      ? this.constants[this.language][key]
      : this.constants[key];
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

  disconnectedCallback() {}

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
    this.dispatchEvent(
      new CustomEvent('ldod-table-increased', { composed: true, bubbles: true })
    );
  };

  loadSearchStyle() {
    import('./tools.css?inline').then((style) => {
      this.querySelector('#table-tools>style').innerHTML = style.default;
    });
  }

  handleSearchInput = () => {
    if (!this.isFullyLoaded) {
      this.observer.disconnect();
      this.addRows(this.data.length);
    }

    const searchTerm = this.querySelector('input#searchField').value?.trim();
    history.replaceState(searchTerm ? { searchTerm } : {}, {});
    const result = this.data
      .filter((row) =>
        row.search?.toLowerCase().includes(searchTerm?.toLowerCase().trim())
      )
      .map((row) => row[this.dataset.searchkey]);
    this.querySelectorAll('tbody>tr').forEach((row) => {
      if (result.indexOf(row.id) === -1)
        return row.toggleAttribute('searched', false);
      row.toggleAttribute('searched', true);
    });

    this.dispatchEvent(
      new CustomEvent('ldod-table-searched', {
        detail: { id: this.id, size: result.length },
        bubbles: true,
        composed: true,
      })
    );
  };

  getSearch() {
    return (
      <input
        id="searchField"
        type="search"
        name="search"
        placeholder="search"
        onInput={this.handleSearchInput}
        value={history.state?.searchTerm ?? ''}
      />
    );
  }

  getRow = (row) => {
    this.lastIndex += 1;
    let entry = row.data && typeof row.data === 'function' ? row.data() : row;
    return (
      <tr searched id={row[this.searchKey]}>
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
          <style></style>
          {this.loadSearchStyle()}
          {this.searchKey && this.getSearch()}
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
}

!customElements.get('ldod-table') &&
  customElements.define('ldod-table', LdodTable);
