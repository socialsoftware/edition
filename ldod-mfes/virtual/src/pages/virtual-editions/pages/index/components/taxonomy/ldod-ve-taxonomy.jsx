import 'shared/modal.js';
import constants from '@src/pages/constants';
import thisConstants from './constants';
import TaxonomyComponent from './taxonomy-component';
import style from './style.css?inline';
import { DeleteButton, MergeButton } from './merge-delete-buttons';
import { computeSelectPureHeight } from '@src/utils';
import {
  addCategory,
  addTopics,
  deleteCategories,
  getVeTaxonomy,
  mergeCategories,
} from './taxonomy-api-requests';
import {
  errorPublisher,
  loadingPublisher,
} from '../../../../../../event-module';

const GenerateTopicsModal = async (node, veId) =>
  (await import('./generate-topics-modal')).default({
    node,
    veId,
  });

const EditCategoryModal = async (node, category) =>
  (await import('./edit-category-modal')).default({ node, category });

const ExtractCategoryFragsModal = async (node, category) =>
  (await import('./extract-category-frags-modal')).default({ node, category });

export class LdodVeTaxonomy extends HTMLElement {
  constructor() {
    super();
    this.selectedRows = 0;
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

  get selectedCategories() {
    const ids = Array.from(
      this.querySelectorAll('table#virtual-taxonomyTable>tbody>tr[selected]')
    ).map((row) => row.id);
    return this.taxonomy.categories
      .map((cat) => cat.externalId)
      .filter((catId) => ids.indexOf(catId) !== -1);
  }

  get show() {
    return this.hasAttribute('show');
  }

  get taxonomyModal() {
    return this.querySelector('ldod-modal#virtual-taxonomyModal');
  }

  get generateTopicsModal() {
    return this.querySelector('ldod-modal#virtual-generateTopicsModal');
  }

  get editCategoryModal() {
    return this.querySelector('ldod-modal#virtual-editCategoryModal');
  }

  get extractCategoryFragsModal() {
    return this.querySelector('ldod-modal#virtual-extractCategoryFragsModal');
  }

  static get observedAttributes() {
    return ['data', 'show'];
  }

  getConstants(key) {
    return this.constants[this.language][key];
  }
  connectedCallback() {
    this.addEventListener('ldod-modal-close', this.handleCloseModal);
  }

  attributeChangedCallback(name, oldV, newV) {
    this.onChangedAttribute[name](oldV, newV);
  }
  disconnectedCallback() {}

  render() {
    this.innerHTML = '';
    this.appendChild(<style>{style}</style>);
    this.appendChild(
      <ldod-modal
        id="virtual-taxonomyModal"
        dialog-class="modal-fullscreen"
        no-footer>
        <span slot="header-slot">
          <span>{this.taxonomy.veTitle} - </span>
          <span>{this.getConstants('taxonomy')}</span>
        </span>
        <div slot="body-slot">
          <TaxonomyComponent node={this} />
        </div>
      </ldod-modal>
    );
    this.renderMergeAndDeleteButtons();
    this.addEventListeners();
  }

  onChangedAttribute = {
    data: () => {
      this.render();
      this.taxonomyModal?.toggleAttribute('show', this.show);
    },
    show: () => {
      if (this.show) {
        getVeTaxonomy(this.parent.edition.externalId)
          .then(async (data) => {
            this.emitLoading(true);
            this.taxonomy = data;
            this.render();
            this.taxonomyModal?.toggleAttribute('show', this.show);
            this.emitLoading(false);
          })
          .catch((error) => {
            console.error(error);
            errorPublisher(error);
          });
      }
    },
  };

  addEventListeners = () => {
    this.handleRowsSelection();
    this.addEventListener('pointerenter', this.generateTopicsModalLazyLoad, {
      once: true,
    });
  };

  generateTopicsModalLazyLoad = async () => {
    this.appendChild(
      await GenerateTopicsModal(this, this.taxonomy.veExternalId)
    );
  };

  handleRowsSelection = () => {
    this.querySelectorAll('table>tbody>tr').forEach((row) => {
      row.addEventListener('click', this.onRowSelection);
    });
  };

  onRowSelection = ({ target }) => {
    if (['A', 'SPAN'].some((tag) => tag === target.nodeName)) return;
    while (target.nodeName !== 'TR') target = target.parentElement;
    const isSelected = target.toggleAttribute('selected');
    isSelected ? ++this.selectedRows : --this.selectedRows;
    this.renderMergeAndDeleteButtons();
  };

  renderMergeAndDeleteButtons = () => {
    this.querySelector('div#mergeCategory').replaceWith(
      <MergeButton node={this} />
    );
    this.querySelector('div#deleteCategory').replaceWith(
      <DeleteButton node={this} />
    );
  };

  updateData = (data = this.taxonomy) => {
    this.taxonomy = data;
    this.querySelector('#taxonomyComponent').replaceWith(
      <TaxonomyComponent node={this} />
    );
    this.addEventListeners();
  };

  handleCloseModal = ({ detail: { id } }) => {
    this.onCloseModal[id.replace('virtual-', '')]();
  };

  onCloseModal = {
    taxonomyModal: async () => {
      this.emitLoading(true);
      this.toggleAttribute('show', false);
      this.resetState();
      this.emitLoading(false);
    },
    generateTopicsModal: () => {
      this.toggleAttribute('show', true);
    },
    editCategoryModal: () => {
      this.editCategoryModal.remove();
    },
    extractCategoryFragsModal: () => {
      document.body.removeEventListener('click', this.computeSelectHeight);
      this.extractCategoryFragsModal.remove();
    },
  };

  resetState = () => {
    (this.taxonomy = null), (this.selectedRows = 0);
    this.removeChild(this.taxonomyModal);
  };

  onAddCategory = (e) => {
    e.preventDefault();
    const body = Object.fromEntries(new FormData(e.target));
    addCategory(this.taxonomy.veExternalId, body)
      .then((data) => this.updateData(data))
      .catch((error) => console.error(error));
  };

  onDeleteCategories = (id) => {
    if (
      (!id && this.selectedCategories < 1) ||
      !confirm(`Proceed with deletion ?`)
    )
      return;
    deleteCategories(
      this.taxonomy.externalId,
      id ? [id] : this.selectedCategories
    )
      .then((data) => this.updateData(data))
      .catch((error) => console.error(error));
  };

  onMergeCategories = () => {
    if (this.selectedCategories < 2) return;
    mergeCategories(this.taxonomy.externalId, this.selectedCategories)
      .then((data) => this.updateData(data))
      .catch((error) => console.error(error));
  };

  onGenerateTopics = () => {
    this.taxonomyModal.toggleAttribute('show', false);
    this.generateTopicsModal.toggleAttribute('show', true);
  };

  onAddTopics = (veId) => {
    if (!this.topics?.topics?.length) return;
    addTopics(veId, this.topics.topics)
      .then(() => this.generateTopicsModal.toggleAttribute('show', false))
      .catch((error) => console.error(error));
  };

  onOpenEditModal = async (category) => {
    this.appendChild(await EditCategoryModal(this, category));
    this.editCategoryModal.toggleAttribute('show');
  };

  onExtractFrags = async (category) => {
    this.appendChild(await ExtractCategoryFragsModal(this, category));
    document.body.addEventListener('click', this.computeSelectHeight);
    this.extractCategoryFragsModal.toggleAttribute('show');
  };

  computeSelectHeight = () => {
    computeSelectPureHeight(
      this.querySelector('select-pure#virtual-extractCatModal')
    );
  };

  emitLoading = (isLoading) => loadingPublisher(isLoading);
}
!customElements.get('ldod-ve-taxonomy') &&
  customElements.define('ldod-ve-taxonomy', LdodVeTaxonomy);
