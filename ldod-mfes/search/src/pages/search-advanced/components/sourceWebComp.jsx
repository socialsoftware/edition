import DateSelect from './dateWebComp';

export default class SourceSelect extends HTMLElement {
  constructor(root, form) {
    super();
    this.root = root;
    this.form = form;
    this.hidden = true;
  }

  get wrapper() {
    return this.querySelector('div#wrapper');
  }

  connectedCallback() {
    this.appendChild(<div id="wrapper"></div>);
    this.render();
  }

  getDateForm = () => {
    const date = new DateSelect(this.root, this.form);
    date.hidden = false;
    date.beginDate = this.beginDate;
    date.endDate = this.endDate;
    return date;
  };

  render() {
    this.wrapper.innerHTML = '';
    this.wrapper.appendChild(
      <>
        <div class="form-floating">
          <select name="hasLdoDMark" class="form-select">
            <option value="all" data-search-key="all">
              {this.root.getConstants('all')}
            </option>
            <option value="true" data-search-key="withLdodLabel">
              {this.root.getConstants('withLdodLabel')}
            </option>
            <option value="false" data-search-key="withoutLdodLabel">
              {this.root.getConstants('withoutLdodLabel')}
            </option>
          </select>
          <label data-search-key="hasLdoDMark">
            {this.root.getConstants('hasLdoDMark')}
          </label>
        </div>
        {this.getDateForm()}
      </>
    );
  }
}

!customElements.get('source-select') &&
  customElements.define('source-select', SourceSelect);
