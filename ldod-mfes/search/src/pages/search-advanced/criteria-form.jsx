const importCriteriaForm = async (name) =>
  (await import(`./components/${name}.jsx`)).default;

const searchTypeList = [
  'edition',
  'manuscript',
  'dactiloscript',
  'publication',
  'heteronym',
  'taxonomy',
  'virtualedition',
  'date',
  'text',
];

export class CriteriaForm extends HTMLFormElement {
  constructor() {
    super();
  }

  get criteria() {
    return this.getAttribute('criteria');
  }

  get criteriaFormElement() {
    return this.querySelector('#criteria-form');
  }

  static get observedAttributes() {
    return ['criteria'];
  }

  connectedCallback() {
    this.render();
  }

  render() {
    this.appendChild(
      <>
        <hr></hr>
        <div id="criteria-container">
          <div class="form-floating">
            <select
              required
              name="type"
              class="form-select"
              onChange={this.onChangeCriteria}>
              <option selected></option>
              {searchTypeList.map((opt) => (
                <option value={opt} data-search-key={opt}>
                  {this.root.getConstants(opt)}
                </option>
              ))}
            </select>
            <label data-search-key="criteriaType">
              {this.root.getConstants('criteriaType')}
            </label>
          </div>
          <div id="criteria-form"></div>
          <button
            class="btn btn-outline-secondary"
            id="remove-criteria"
            onClick={this.onRemoveCriteria}>
            <span class="icon icon-minus"></span>
          </button>
        </div>
      </>
    );
  }

  attributeChangedCallback(name) {
    this.onChangeAttribute[name]();
  }

  onChangeAttribute = {
    criteria: () => this.changedCriteriaHandler(),
  };

  onChangeCriteria = ({ target }) => {
    this.setAttribute('criteria', target.value);
  };

  changedCriteriaHandler = async () => {
    const criteriaForm = this.criteria ? (
      (await importCriteriaForm(this.criteria))({ root: this.root, form: this })
    ) : (
      <></>
    );
    this.insertCriteriaForm(criteriaForm);
  };

  insertCriteriaForm = (criteriaForm) => {
    this.criteriaFormElement.replaceWith(
      <div id="criteria-form">{criteriaForm}</div>
    );
  };

  onRemoveCriteria = () => {
    --this.root.sequentialId;
    this.remove();
  };
}
!customElements.get('criteria-form') &&
  customElements.define('criteria-form', CriteriaForm, { extends: 'form' });
