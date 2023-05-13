/** @format */

import constants from '@src/pages/constants';
import thisConstants from './constants';
import TaxonomyComponent from './taxonomy-component';
import taxonomyStyle from './taxonomy.css?inline';
import style from '../style.css?inline';
import formStyle from '@ui/bootstrap/forms-css.js';
import buttonsStyle from '@ui/bootstrap/buttons-css.js';
import { DeleteButton, MergeButton } from './merge-delete-buttons';
import { computeSelectPureHeight } from '@src/utils';
import {
	addCategory,
	addTopics,
	deleteCategories,
	getVeTaxonomy,
	mergeCategories,
} from './taxonomy-api-requests';
import { errorPublisher, loadingPublisher } from '../../../../../../event-module';

const sheet = new CSSStyleSheet();
sheet.replaceSync(style + taxonomyStyle + buttonsStyle + formStyle);

export class LdodVeTaxonomy extends HTMLElement {
	constructor() {
		super();
		this.attachShadow({ mode: 'open' });
		this.shadowRoot.adoptedStyleSheets = [sheet];
		this.constants = Object.entries(thisConstants).reduce((prev, [key, value]) => {
			prev[key] = value instanceof Array ? value : { ...constants[key], ...value };
			return prev;
		}, {});
	}

	get language() {
		return this.getAttribute('language');
	}

	get selectedCategories() {
		const ids = Array.from(
			this.shadowRoot.querySelectorAll('table#virtual-taxonomyTable>tbody>tr[selected]')
		).map(row => row.id);
		return this.taxonomy.categories
			.map(cat => cat.externalId)
			.filter(catId => ids.indexOf(catId) !== -1);
	}

	get show() {
		return this.hasAttribute('show');
	}

	get taxonomyModal() {
		return this.shadowRoot.querySelector('ldod-bs-modal#virtual-taxonomy-modal');
	}

	get generateTopicsModal() {
		return this.shadowRoot.querySelector('ldod-bs-modal#virtual-generate-topics-modal');
	}

	get editCategoryModal() {
		return this.shadowRoot.querySelector('ldod-bs-modal#virtual-edit-category-modal');
	}

	get extractCategoryFragsModal() {
		return this.shadowRoot.querySelector('ldod-bs-modal#virtual-extract-category-frags-modal');
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

	render() {
		this.shadowRoot.innerHTML = '';
		this.shadowRoot.appendChild(
			<ldod-bs-modal
				id="virtual-taxonomy-modal"
				dialog-class="modal-xl modal-dialog-scrollable">
				<h5 slot="header-slot">
					<span>{this.taxonomy.veTitle} - </span>
					<span>{this.getConstants('taxonomy')}</span>
				</h5>
				<div slot="body-slot">
					<TaxonomyComponent node={this} />
				</div>
			</ldod-bs-modal>
		);
		this.renderMergeAndDeleteButtons();
		this.addEventListeners();
	}

	onChangedAttribute = {
		show: () => {
			if (this.show) {
				getVeTaxonomy(this.parent.edition.externalId)
					.then(async data => {
						this.emitLoading(true);
						this.taxonomy = data;
						this.render();
						this.taxonomyModal?.toggleAttribute('show', this.show);
						this.emitLoading(false);
					})
					.catch(error => {
						console.error(error);
						errorPublisher(error.msg);
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
		this.shadowRoot.appendChild(await GenerateTopicsModal(this, this.taxonomy.veExternalId));
	};

	handleRowsSelection = () => {
		this.shadowRoot.querySelectorAll('table>tbody>tr').forEach(row => {
			row.addEventListener('click', this.onRowSelection);
		});
	};

	onRowSelection = ({ target }) => {
		if (['A', 'SPAN'].some(tag => tag === target.nodeName)) return;
		while (target.nodeName !== 'TR') target = target.parentElement;
		target.toggleAttribute('selected');
	};

	renderMergeAndDeleteButtons = () => {
		this.shadowRoot.querySelector('div#mergeCategory').replaceWith(<MergeButton node={this} />);
		this.shadowRoot
			.querySelector('div#deleteCategory')
			.replaceWith(<DeleteButton node={this} />);
	};

	updateData = (data = this.taxonomy) => {
		this.taxonomy = data;
		const taxComponent = this.shadowRoot.querySelector('#taxonomyComponent');
		if (taxComponent) {
			taxComponent.replaceWith(<TaxonomyComponent node={this} />);
			this.addEventListeners();
		}
	};

	handleCloseModal = ({ detail: { id } }) => {
		this.onCloseModal[id.replace('virtual-', '')]();
	};

	onCloseModal = {
		'taxonomy-modal': async () => {
			this.emitLoading(true);
			this.toggleAttribute('show', false);
			this.resetState();
			this.emitLoading(false);
		},
		'generate-topics-modal': () => {
			this.toggleAttribute('show', true);
		},
		'edit-category-modal': () => {
			this.editCategoryModal.remove();
			this.toggleAttribute('show', true);
		},
		'extract-category-frags-modal': () => {
			document.body.removeEventListener('click', this.computeSelectHeight);
			this.extractCategoryFragsModal.remove();
			this.toggleAttribute('show', true);
		},
	};

	resetState = () => {
		this.taxonomy = null;
		this.shadowRoot.removeChild(this.taxonomyModal);
	};

	onAddCategory = e => {
		e.preventDefault();
		const body = Object.fromEntries(new FormData(e.target));
		addCategory(this.taxonomy.veExternalId, body)
			.then(data => this.updateData(data))
			.catch(error => console.error(error));
	};

	onDeleteCategories = id => {
		if ((!id && this.selectedCategories < 1) || !confirm(`Proceed with deletion ?`)) return;
		deleteCategories(this.taxonomy.externalId, id ? [id] : this.selectedCategories)
			.then(data => this.updateData(data))
			.catch(error => console.error(error));
	};

	onMergeCategories = () => {
		if (this.selectedCategories < 2) return;
		mergeCategories(this.taxonomy.externalId, this.selectedCategories)
			.then(data => this.updateData(data))
			.catch(error => console.error(error));
	};

	onGenerateTopics = () => {
		this.taxonomyModal.toggleAttribute('show', false);
		this.generateTopicsModal.toggleAttribute('show', true);
	};

	onAddTopics = veId => {
		if (!this.topics?.topics?.length) return;
		addTopics(veId, this.topics.topics)
			.then(() => this.generateTopicsModal.toggleAttribute('show', false))
			.catch(error => console.error(error));
	};

	onOpenEditModal = async category => {
		this.taxonomyModal.toggleAttribute('show', false);
		this.shadowRoot.appendChild(await EditCategoryModal(this, category));
		this.editCategoryModal.toggleAttribute('show');
	};

	onExtractFrags = async category => {
		this.taxonomyModal.toggleAttribute('show', false);
		this.shadowRoot.appendChild(await ExtractCategoryFragsModal(this, category));
		document.body.addEventListener('click', this.computeSelectHeight);
		this.extractCategoryFragsModal.toggleAttribute('show');
	};

	computeSelectHeight = () => {
		computeSelectPureHeight(this.querySelector('select-pure#virtual-extractCatModal'));
	};

	emitLoading = isLoading => loadingPublisher(isLoading);
}
!customElements.get('ldod-ve-taxonomy') &&
	customElements.define('ldod-ve-taxonomy', LdodVeTaxonomy);

async function GenerateTopicsModal(node, veId) {
	return (await import('./generate-topics-modal')).default({
		node,
		veId,
	});
}

async function EditCategoryModal(node, category) {
	return (await import('./edit-category-modal')).default({ node, category });
}

async function ExtractCategoryFragsModal(node, category) {
	return (await import('./extract-category-frags-modal')).default({ node, category });
}
