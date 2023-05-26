/** @format */

import thisConstants from './constants';
import constants from '@src/pages/constants';
import AssistedTable from './assisted-table';
import './properties-form';
import { saveLinerVE } from '@src/restricted-api-requests';
import style from './style.css?inline';
import formStyle from '@ui/bootstrap/forms-css.js';
import buttonsStyle from '@ui/bootstrap/buttons-css.js';

const sheet = new CSSStyleSheet();
sheet.replaceSync(style + buttonsStyle + formStyle);
export class LdodVeAssisted extends HTMLElement {
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

	get show() {
		return this.hasAttribute('show');
	}

	get modal() {
		return this.shadowRoot.querySelector('#virtual-veAssisted');
	}

	get table() {
		return this.shadowRoot.querySelector('ldod-table#virtual-assistedTable');
	}

	static get observedAttributes() {
		return ['show'];
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

	onCloseModal = () => {
		this.toggleAttribute('show', false);
		this.shadowRoot.innerHTML = '';
	};

	onSave = async () => {
		const data = await saveLinerVE(
			this.edition.externalId,
			this.inters.map(({ externalId }) => externalId)
		);
		this.shadowRoot
			.querySelector('properties-form')
			.shadowRoot.querySelectorAll('input[type="range"]')
			.forEach(input => (input.value = 0));
		this.updateData(data);
	};

	render() {
		this.shadowRoot.appendChild(
			<ldod-bs-modal id="virtual-veAssisted" dialog-class="modal-fullscreen">
				<h5 slot="header-slot">{this.edition?.title}</h5>
				<div slot="body-slot">
					<properties-form parent={this}></properties-form>
					<AssistedTable node={this} />
				</div>
				<div slot="footer-slot">
					<button type="button" class="btn btn-primary" onClick={this.onSave}>
						<span
							is="ldod-span-icon"
							icon="floppy-disk"
							fill="#fff"
							size="16px"
							style={{ marginRight: '8px' }}></span>
						{this.getConstants('save')}
					</button>
				</div>
			</ldod-bs-modal>
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
		this.reRenderTable();
	};

	reRenderTable() {
		this.table?.replaceWith(<AssistedTable node={this} />);
	}

	onChangedAttribute = {
		show: () => {
			this.show && this.render();
			this.modal?.toggleAttribute('show', this.show);
		},
	};
}
!customElements.get('ldod-ve-assisted') &&
	customElements.define('ldod-ve-assisted', LdodVeAssisted);
