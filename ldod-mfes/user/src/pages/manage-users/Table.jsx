import tableStyle from './table.css?inline';

export class LdodTable extends HTMLElement {
  constructor() {
    super();
  }
  get classes() {
    return this.getAttribute('classes');
  }

  get language() {
    return this.getAttribute('language');
  }

  get searchKey() {
    return this.dataset.searchkey;
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
  }

  disconnectedCallback() {}

  loadSearchStyle() {
    import('./tools.css?inline').then((style) => {
      this.querySelector('#table-tools>style').innerHTML = style.default;
    });
  }

  handleSearchInput = ({ target }) => {
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
          <tbody>
            {this.data.map((entry) => {
              return (
                <tr searched id={entry[this.searchKey]}>
                  {this.headers.map((key) => (
                    <td>
                      {typeof entry[key] === 'function'
                        ? entry[key]()
                        : entry[key]}
                    </td>
                  ))}
                </tr>
              );
            })}
          </tbody>
        </table>
      </>
    );
  }
}
!customElements.get('ldod-table') &&
  customElements.define('ldod-table', LdodTable);
