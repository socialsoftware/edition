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
    this.render();
  }

  render() {
    this.innerHTML = '';
    this.appendChild(
      <div class="form-floating" id="heteronym-container">
        <select id="heteronym" class="form-select" name="heteronym">
          {this.heteronyms?.map((heteronym) => {
            return <option value={heteronym.xmlId}>{heteronym.name}</option>;
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
