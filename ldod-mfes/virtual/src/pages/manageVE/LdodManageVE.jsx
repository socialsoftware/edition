import { deleteVE } from './apiRequests';
import constants from '../constants';
import VeManageTable from './VeManageTable';

import.meta.env.DEV
  ? await import('shared/table-dev.js')
  : await import('shared/table.js');

import('shared/buttons.js').then(({ exportButton, uploadButton }) => {
  exportButton();
  uploadButton();
});

const HOST = import.meta.env.VITE_HOST;

export class LdodManageVE extends HTMLElement {
  constructor() {
    super();
  }

  get constants() {
    return constants;
  }

  get language() {
    return this.getAttribute('language');
  }

  get wrapper() {
    return (
      this.querySelector('div#manageVeWrapper') || (
        <div id="manageVeWrapper"></div>
      )
    );
  }

  static get observedAttributes() {
    return ['language'];
  }

  getConstants(key, ...args) {
    const constant = constants[this.language][key];
    return args.length ? constant(...args) : constant;
  }

  connectedCallback() {
    this.appendChild(this.wrapper);
    this.render();
    this.addEventListeners();
  }

  render() {
    this.wrapper.innerHTML = '';
    this.wrapper.appendChild(
      <>
        <h3 class="text-center" data-virtual-key="manageVE">
          {this.getConstants('manageVE')}
          <span>{` (${this.virtualEditions.length})`}</span>
        </h3>
        <div class="flex-row">
          <div class="flex-column">
            <ldod-upload
              width="600px"
              data-virtual-button-key="uploadVeCorpus"
              title={this.getConstants('uploadVeCorpus')}
              data-url={`${HOST}/virtual/admin/upload-virtual-corpus`}></ldod-upload>
            <ldod-upload
              width="600px"
              data-virtual-button-key="uploadVeFragments"
              multiple
              title={this.getConstants('uploadVeFragments')}
              data-url={`${HOST}/virtual/admin/upload-virtual-fragments`}></ldod-upload>
          </div>
          <div class="flex-column">
            <ldod-export
              width="350px"
              file-type="zip"
              data-virtual-button-key="exportVe"
              title={this.getConstants('exportVe')}
              file-prefix="VirtualEditionsFragments"
              data-url={`${HOST}/virtual/admin/export-virtual-editions`}
              method="GET"></ldod-export>
          </div>
        </div>
        <div>
          <VeManageTable node={this} />
        </div>
      </>
    );
  }

  attributeChangedCallback(name, oldV, newV) {
    this.handleChangedAttribute[name](oldV, newV);
  }

  handleChangedAttribute = {
    language: (oldV, newV) => {
      if (oldV && oldV !== newV) {
        this.querySelectorAll('[data-virtual-key]').forEach((node) => {
          return (node.firstChild.textContent = node.dataset.args
            ? this.getConstants(
                node.dataset.virtualKey,
                JSON.parse(node.dataset.args)
              )
            : this.getConstants(node.dataset.virtualKey));
        });
        this.querySelectorAll('[data-virtual-tooltip-key]').forEach((ele) => {
          ele.setAttribute(
            'content',
            this.getConstants(ele.dataset.virtualTooltipKey)
          );
        });
        this.querySelectorAll('[data-virtual-button-key]').forEach((btn) => {
          btn.setAttribute(
            'title',
            this.getConstants(btn.dataset.virtualButtonKey)
          );
        });
      }
    },
  };

  addEventListeners = () => {
    this.addEventListener('ldod-table-searched', this.updateTitle);
  };

  updateTitle = ({ detail }) => {
    this.querySelector('h3>span').textContent = ` (${detail.size})`;
  };

  handleRemoveVE = async ({ target }) => {
    if (
      !confirm(
        `Are you sure you want to remove the Virtual Edition ${target.dataset.acrn} ?`
      )
    )
      return;
    deleteVE(target.id)
      .then((data) => {
        this.virtualEditions = data || [];
        this.render();
      })
      .catch((error) => console.error(error));
  };
}

!customElements.get('ldod-manage-ve') &&
  customElements.define('ldod-manage-ve', LdodManageVE);
