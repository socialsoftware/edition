export default class HeteronymSelect extends HTMLElement {
  constructor(root, form) {
    super();
    this.root = root;
    this.form = form;
    this.hidden = true;
  }

  static get observedAttributes() {
    return ['edition'];
  }

  get edition() {
    return this.getAttribute('edition');
  }

  connectedCallback() {
    this.heteronymCriteriaMode();
    this.render();
  }

  heteronymCriteriaMode = () => {
    this.heteronyms = this.root.data.heteronyms;
  };

  render() {
    this.innerHTML = '';
    this.appendChild(
      <div class="form-floating" id="heteronym-container">
        <select id="heteronym" class="form-select" name="heteronym">
          <option value="all" data-search-key="all">
            {this.root.getConstants('all')}
          </option>
          {this.heteronyms?.map((heteronym) => {
            let name = heteronym.xmlId
              ? heteronym.name
              : this.root.getConstants(heteronym.name);
            return (
              <option data-search-key={heteronym.name} value={heteronym.xmlId}>
                {name}
              </option>
            );
          })}
        </select>
        <label data-search-key="heteronym">
          {this.root.getConstants('heteronyms')}
        </label>
      </div>
    );
  }

  attributeChangedCallback(name) {
    this.onChangeAttribute[name]();
  }

  onChangeAttribute = {
    edition: () => this.onEdition(),
  };

  onEdition = () => {
    if (this.edition === 'all') return (this.hidden = true);
    this.heteronyms = this.root.data.editions.find(
      (ed) => ed.acronym === this.edition
    ).heteronyms;
    this.render();
    this.hidden = false;
  };
}
!customElements.get('heteronym-select') &&
  customElements.define('heteronym-select', HeteronymSelect);
