import thisConstants from './constants';
import constants from '../../../../constants';
import AssistedTable from './AssistedTable';
import style from './style.css?inline';
import './PropertiesForm';
import { saveLinerVE } from '@src/apiRequests';

export class LdodVeAssisted extends HTMLElement {
  constructor() {
    super();
    this.constants = Object.entries(thisConstants).reduce(
      (prev, [key, value]) => {
        prev[key] =
          value instanceof Array ? value : { ...constants[key], ...value };
        return prev;
      },
      {}
    );
  }

  get language() {
    return this.getAttribute('language');
  }

  get show() {
    return this.hasAttribute('show');
  }

  get modal() {
    return this.querySelector('ldod-modal');
  }

  static get observedAttributes() {
    return ['data', 'show'];
  }
  getConstants(key) {
    return this.constants[this.language][key];
  }
  connectedCallback() {
    this.addEventListener('ldod-modal-close', this.onCloseModal);
  }

  attributeChangedCallback(name, oldV, newV) {
    this.onChangedAttribute[name](oldV, newV);
  }

  disconnectedCallback() {}

  onCloseModal = () => {
    this.toggleAttribute('show', false);
    this.innerHTML = '';
  };

  onSave = async () => {
    const data = await saveLinerVE(
      this.edition.externalId,
      this.inters.map(({ externalId }) => externalId)
    );
    this.querySelector('properties-form')
      .shadowRoot.querySelectorAll('input[type="range"]')
      .forEach((input) => (input.value = 0));
    this.updateData(data);
  };

  render() {
    this.appendChild(<style>{style}</style>);
    this.appendChild(
      <ldod-modal id="virtual-veAssisted" dialog-class="modal-fullscreen">
        <span slot="header-slot">{this.edition?.title}</span>
        <div slot="body-slot">
          <properties-form parent={this}></properties-form>
          <AssistedTable node={this} />
        </div>
        <div slot="footer-slot">
          <button type="button" class="btn btn-primary" onClick={this.onSave}>
            <span class="icon save-icon"></span>
            {this.getConstants('save')}
          </button>
        </div>
      </ldod-modal>
    );
  }

  updateData = ({ inters, selected, properties }) => {
    this.inters = inters;
    this.selected = selected;
    this.properties = properties?.map(({ title, acronym, weight }) => ({
      type: title.toLowerCase(),
      acronym,
      weight,
    }));
    this.toggleAttribute('data');
  };

  onChangedAttribute = {
    data: () => {
      if (this.hasChildNodes()) {
        return this.querySelector(
          'ldod-table#virtual-assistedTable'
        ).replaceWith(<AssistedTable node={this} />);
      }
      this.render();
      this.toggleAttribute('show', true);
    },
    show: () => {
      this.show && this.render();
      this.modal?.toggleAttribute('show', this.show);
    },
  };
}
!customElements.get('ldod-ve-assisted') &&
  customElements.define('ldod-ve-assisted', LdodVeAssisted);
