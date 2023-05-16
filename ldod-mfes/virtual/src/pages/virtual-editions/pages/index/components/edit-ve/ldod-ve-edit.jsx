/** @format */

import '@ui/modal-bs.js';
import { editVE } from '@src/restricted-api-requests';
import constants from '@src/pages/constants';
import thisConstants from './constants';
import EditionEditForm from './edition-edit-form';
import { errorPublisher } from '../../../../../../event-module';
import style from '../style.css?inline';
import formStyle from '@ui/bootstrap/forms-css.js';
import buttonsStyle from '@ui/bootstrap/buttons-css.js';

const sheet = new CSSStyleSheet();
sheet.replaceSync(style + buttonsStyle + formStyle);
export class LdodVeEdit extends HTMLElement {
	constructor() {
		super();
		this.attachShadow({ mode: 'open' });
		this.shadowRoot.adoptedStyleSheets = [sheet];
		this.constants = Object.entries(thisConstants).reduce((prev, [key, value]) => {
			prev[key] = { ...constants[key], ...value };
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
		return this.shadowRoot.querySelector('ldod-bs-modal');
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

	onCloseModal = () => {
		this.toggleAttribute('show', false);
	};

	render() {
		this.shadowRoot.innerHTML = '';
		this.shadowRoot.appendChild(
			<>
				<ldod-bs-modal id="virtual-veEdit" dialog-class="modal-xl modal-fullscreen-lg-down">
					<h5 slot="header-slot">
						<span>{this.edition?.title}</span>
					</h5>
					<div slot="body-slot">
						<EditionEditForm node={this} />
					</div>
				</ldod-bs-modal>
			</>
		);
	}

	onSave = e => {
		e.preventDefault();
		const selectedCountries = Array.from(this.querySelectorAll('select[multiple] option'))
			.filter(node => node.selected)
			.map(({ value }) => value)
			.join(',');

		const veData = {
			...Object.fromEntries(new FormData(e.target)),
			countries: selectedCountries,
		};
		editVE(this.edition.externalId, veData)
			.then(edition => {
				this.parent.updateEdition(edition);
				this.toggleAttribute('show', false);
			})
			.catch(error => {
				console.error(error);
				if (!error?.ok) errorPublisher(error?.message);
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
!customElements.get('ldod-ve-edit') && customElements.define('ldod-ve-edit', LdodVeEdit);
