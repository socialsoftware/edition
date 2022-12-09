import 'shared/modal.js';
import { editVE } from '@src/restricted-api-requests';
import constants from '@src/pages/constants';
import thisConstants from './constants';
import EditionEditForm from './edition-edit-form';

export class LdodVeEdit extends HTMLElement {
  constructor() {
    super();
    this.constants = Object.entries(thisConstants).reduce(
      (prev, [key, value]) => {
        prev[key] = { ...constants[key], ...value };
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

  disconnectedCallback() { }

  onCloseModal = () => {
    this.toggleAttribute('show', false);
  };

  render() {
    this.innerHTML = '';
    this.appendChild(
      <ldod-modal id="virtual-veEdit" dialog-class="modal-fullscreen" no-footer>
        <span slot="header-slot">{this.edition?.title}</span>
        <div slot="body-slot">
          <EditionEditForm node={this} />
        </div>
      </ldod-modal>
    );
  }

  onSave = (e) => {
    e.preventDefault();
    const selectedCountries = Array.from(
      this.querySelectorAll('select[multiple] option')
    )
      .filter((node) => node.selected)
      .map(({ value }) => value)
      .join(',');

    const veData = {
      ...Object.fromEntries(new FormData(e.target)),
      countries: selectedCountries,
    };
    editVE(this.edition.externalId, veData)
      .then((edition) => {
        this.parent.updateEdition(edition);
        this.toggleAttribute('show', false);
      })
      .catch((error) => {
        if (!error?.ok)
          this.dispatchEvent(
            new CustomEvent('ldod-error', {
              detail: { message: error?.message },
              bubbles: true,
              composed: true,
            })
          );
      });
  };

  onChangedAttribute = {
    data: () => {
      this.render();
      this.modal?.toggleAttribute('show', this.show);
    },
    show: (oldV, newV) => {
      this.render();
      this.modal?.toggleAttribute('show', this.show);
    },
  };
}
!customElements.get('ldod-ve-edit') &&
  customElements.define('ldod-ve-edit', LdodVeEdit);
