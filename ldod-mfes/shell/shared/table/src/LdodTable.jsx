import tableStyle from './table.css?inline';

export class LdodTable extends HTMLElement {
  constructor() {
    super();
    this.lastIndex = 0;
  }

  get classes() {
    return this.getAttribute('classes');
  }

  get isFullyLoaded() {
    return this.lastIndex === this.data.length;
  }

  get interval() {
    return this.data.length < 20 ? this.data.length : 20;
  }

  get language() {
    return this.getAttribute('language');
  }

  get searchKey() {
    return this.dataset.searchkey;
  }

  get numberOfRows() {}

  get targetToObserve() {
    let rows = this.querySelectorAll('table>tbody>tr');
    let nrOfTr = rows.length;
    return rows[nrOfTr * 0.75];
  }

  static get observedAttributes() {
    return ['language'];
  }

  getConstants(key) {
    return this.constants[this.language][key];
  }
  render() {
    this.appendChild(this.getTable());
  }
  attributeChangedCallback(name, oldV, newV) {
    if (name === 'language' && oldV && oldV !== newV) {
      this.querySelectorAll('[data-key]').forEach((ele) => {
        ele.textContent = this.getConstants(ele.dataset.key);
      });
    }
  }

  connectedCallback() {
    this.appendChild(<style>{tableStyle}</style>);
    this.render();
    if (!this.isFullyLoaded) this.addObserver();
  }

  disconnectedCallback() {}

  obsCallback = ([entry], observer) => {
    if (entry.intersectionRatio === 1) {
      observer.unobserve(entry.target);
      this.addRows();
      if (!this.isFullyLoaded) observer.observe(this.targetToObserve);
    }
  };

  addObserver() {
    const obs = new IntersectionObserver(this.obsCallback, {
      threshold: 1.0,
    });
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
  };

  loadSearchStyle() {
    import('./tools.css?inline').then((style) => {
      this.querySelector('#table-tools>style').innerHTML = style.default;
    });
  }

  handleSearchInput = ({ target }) => {
    if (!this.isFullyLoaded) {
      this.observer.disconnect();
      this.addRows(this.data.length);
    }

    const searchTerm = target.value;
    const result = this.data
      .filter((item) =>
        item.search?.toLowerCase().includes(searchTerm?.toLowerCase().trim())
      )
      .map((row) => row[this.dataset.searchkey]);
    this.querySelectorAll('tbody>tr').forEach((row) => {
      if (result.indexOf(row.id) === -1)
        return row.toggleAttribute('searched', false);
      row.toggleAttribute('searched', true);
    });
  };

  getSearch() {
    return (
      <input
        type="search"
        name="search"
        placeholder="search"
        onInput={this.handleSearchInput}
      />
    );
  }

  getRows(start, end) {
    return this.data.slice(start, end).map((entry) => {
      this.lastIndex += 1;
      return (
        <tr searched id={entry[this.searchKey]}>
          {this.headers.map((key) => (
            <td>
              {typeof entry[key] === 'function' ? entry[key]() : entry[key]}
            </td>
          ))}
        </tr>
      );
    });
  }

  getTable() {
    return (
      <>
        <div id="table-tools">
          <style></style>
          {this.loadSearchStyle()}
          {this.searchKey && this.getSearch()}
        </div>
        <table id={this.id} class={this.classes}>
          <thead>
            <tr>
              {this.headers.map((key) => (
                <td data-key={key}>{this.getConstants(key)}</td>
              ))}
            </tr>
          </thead>
          <tbody>{this.getRows(0, this.interval)}</tbody>
        </table>
      </>
    );
  }
}

!customElements.get('ldod-table') &&
  customElements.define('ldod-table', LdodTable);
