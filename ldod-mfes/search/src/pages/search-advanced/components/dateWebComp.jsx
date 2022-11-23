export default class DateSelect extends HTMLElement {
  constructor(root, form) {
    super();
    this.root = root;
    this.form = form;
    this.hidden = true;
  }

  get edition() {
    return this.getAttribute('edition');
  }
  get wrapper() {
    return this.querySelector('div#date-wrapper');
  }

  static get observedAttributes() {
    return ['edition'];
  }

  connectedCallback() {
    this.appendChild(<div id="date-wrapper"></div>);
    this.render();
  }

  render() {
    this.wrapper.innerHTML = '';
    this.wrapper.appendChild(
      <div class="form-floating">
        <select name="option" class="form-select" onChange={this.onDateChange}>
          <option value="all" data-search-key="all" selected>
            {this.root.getConstants('all')}
          </option>
          <option value="dated" data-search-key="dated">
            {this.root.getConstants('dated')}
          </option>
          <option value="undated" data-search-key="notDated">
            {this.root.getConstants('notDated')}
          </option>
        </select>
        <label data-search-key="date">{this.root.getConstants('date')}</label>
      </div>
    );
  }

  getYearsForm = () => {
    return (
      <>
        <div class="form-floating">
          <input
            style={{ height: 'auto', width: '175px' }}
            id="begin-date-input"
            class="form-control"
            type="number"
            name="begin"
            min={this.beginDate}
            max={this.endDate}
            onChange={this.onBeginDateChange}
            value={this.beginDate}
            placeholder="beginDate"
          />
          <label data-search-key="beginDate">
            {this.root.getConstants('beginDate')}
          </label>
        </div>
        <div class="form-floating">
          <input
            id="end-date-input"
            style={{ height: 'auto', width: '175px' }}
            class="form-control"
            type="number"
            name="end"
            min={this.beginDate}
            max={this.endDate}
            onChange={this.onEndDateChange}
            value={this.endDate}
            placeholder="endDate"
          />
          <label data-search-key="endDate">
            {this.root.getConstants('endDate')}
          </label>
        </div>
      </>
    );
  };

  onEndDateChange = ({ target }) => {
    const beginDate = this.wrapper.querySelector('#begin-date-input');
    beginDate.max = target.value;
    beginDate.value =
      beginDate.value > target.value ? target.value : beginDate.value;
  };

  onBeginDateChange = ({ target }) => {
    const endDate = this.wrapper.querySelector('#end-date-input');
    endDate.min = target.value;
    endDate.value = endDate.value < target.value ? target.value : endDate.value;
  };

  attributeChangedCallback(name) {
    this.onChangeAttribute[name]();
  }
  onChangeAttribute = {
    edition: () => this.onEdition(),
  };

  onEdition = () => {
    if (this.edition === 'all') return (this.hidden = true);
    const expertEdition = this.root.data.editions.find(
      (ed) => ed.acronym === this.edition
    );
    this.beginDate = expertEdition.beginDate;
    this.endDate = expertEdition.endDate;
    this.render();
    this.hidden = false;
  };

  onDateChange = ({ target }) => {
    if (target.value !== 'dated')
      return this.wrapper
        .querySelectorAll(':has(input)')
        .forEach((inpt) => inpt.remove());
    this.wrapper.appendChild(this.getYearsForm());
  };
}

!customElements.get('date-select') &&
  customElements.define('date-select', DateSelect);
