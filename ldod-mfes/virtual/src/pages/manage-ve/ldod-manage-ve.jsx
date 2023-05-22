/** @format */

import { deleteVE, getVirtualEditions4Manage } from './api-requests';
import constants from '../constants';
import VeManageTable from './ve-manage-table';
import { errorPublisher, messagePublisher } from '../../event-module';
import manageVeComponent from './manage-ve-component';

import.meta.env.DEV ? await import('@ui/table-dev.js') : await import('@ui/table.js');

import('@ui/buttons.js').then(({ exportButton, uploadButton }) => {
	exportButton();
	uploadButton();
});

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
		return this.querySelector('div#manageVeWrapper') || <div id="manageVeWrapper"></div>;
	}

	get table() {
		return this.querySelector('#virtual-manageVeTable');
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
		this.addEventListeners();
	}

	updateData = data => {
		this.virtualEditions = data;
		this.render();
	};

	render() {
		this.wrapper.innerHTML = '';
		this.wrapper.appendChild(manageVeComponent(this));
	}

	attributeChangedCallback(name, oldV, newV) {
		this.handleChangedAttribute[name](oldV, newV);
	}

	handleChangedAttribute = {
		language: (oldV, newV) => {
			if (oldV && oldV !== newV) {
				this.updateElementContent();
				this.updateTooltipContent();
				this.updateButtonContent();
			}
		},
	};

	addEventListeners = () => {
		this.addEventListener('ldod:file-uploaded', this.handleFileUploaded);
		this.addEventListener('ldod-table-searched', this.updateTitle);
	};

	handleFileUploaded = e => {
		e.stopPropagation();
		const payload = e.detail.payload;
		if (!payload.ok) return errorPublisher(payload.message);
		getVirtualEditions4Manage()
			.then(data => {
				this.updateData(data);
				messagePublisher(payload.message);
			})
			.catch(error => console.error(error));
	};

	updateTitle = ({ detail }) => {
		this.querySelector('h3>span').textContent = ` (${detail.size})`;
	};

	handleRemoveVE = async ({ target }) => {
		confirm(`Are you sure you want to remove the Virtual Edition ${target.dataset.acrn} ?`) &&
			deleteVE(target.id)
				.then(data => {
					this.virtualEditions = data || [];
					this.render();
				})
				.catch(console.error);
	};

	updateElementContent = () => {
		this.querySelectorAll('[data-virtual-key]').forEach(node => {
			return (node.firstChild.textContent = node.dataset.args
				? this.getConstants(node.dataset.virtualKey, JSON.parse(node.dataset.args))
				: this.getConstants(node.dataset.virtualKey));
		});
	};

	updateButtonContent = () => {
		this.querySelectorAll('[data-virtual-button-key]').forEach(btn => {
			btn.setAttribute('title', this.getConstants(btn.dataset.virtualButtonKey));
		});
	};

	updateTooltipContent = () => {
		this.querySelectorAll('[data-virtual-tooltip-key]').forEach(ele => {
			ele.setAttribute('content', this.getConstants(ele.dataset.virtualTooltipKey));
		});
	};
}

!customElements.get('ldod-manage-ve') && customElements.define('ldod-manage-ve', LdodManageVE);
