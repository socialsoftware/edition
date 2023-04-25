/** @format */

import constants from './constants';
import { selectedVe } from '../event-module';
import { addInterRequest, getVirtualFragmentNavInters } from './api-requests';
import fragNav from './frag-nav';

import style from './style.css?inline';
const sheet = new CSSStyleSheet();
sheet.replaceSync(style);

export class VirtualFragNav extends HTMLElement {
	constructor() {
		super();
		this.attachShadow({ mode: 'open' });
		this.shadowRoot.adoptedStyleSheets = [sheet];
		this.intersChecked = [];
		this.veId = '';
		this.interId = '';
	}

	get urlId() {
		return this.getAttribute('urlid');
	}

	get fragment() {
		return this.getAttribute('fragment');
	}

	get language() {
		return this.getAttribute('language');
	}

	static get observedAttributes() {
		return ['language'];
	}

	getConstants(key) {
		return constants[this.language][key];
	}

	updateSelectedInters = () => {
		if (this.urlId)
			this.intersChecked.push(
				this.inters
					.map(obj => obj.inters)
					.map(([inter]) => inter)
					.find(inter => inter?.urlId === this.urlId)
			);
	};

	async connectedCallback() {
		this.fragment && (await this.fetchData());
		this.updateSelectedInters();
		this.render();
		this.addEventListener('virtual:changed', this.onCheckboxChange);
		this.addEventListener('virtual:clicked', this.addInterToVe);
	}

	fetchData = async () => {
		await getVirtualFragmentNavInters(this.fragment, {
			inters: selectedVe,
			currentInterId:
				this.intersChecked?.length === 1 ? this.intersChecked[0].externalId : null,
			urlId: this.urlId,
		})
			.then(data => (this.inters = data))
			.catch(error => console.error(error));
	};

	fetchAddInter = async () => {
		await addInterRequest(this.fragment, this.veId, this.interId, {
			inters: selectedVe,
			currentInterId: this.intersChecked.length === 1 && this.intersChecked[0].externalId,
		})
			.then(data => (this.inters = data))
			.catch(error => console.error(error));
	};

	render() {
		this.shadowRoot.innerHTML = fragNav(this);
	}

	onCheckboxChange = async ({ detail }) => {
		this.updateInters(detail);
		this.dispatchEvent(
			new CustomEvent('virtual:inter-selected', {
				detail: detail.selected,
				composed: true,
				bubbles: true,
			})
		);
		if (!this.intersChecked.length) return;
		await this.fetchData();
		this.render();
	};

	updateInters = inters => {
		this.intersChecked = inters.selected.map(inter => ({
			externalId: inter.id,
			urlId: inter.name,
		}));
	};

	addInterToVe = async ({ detail }) => {
		const target = detail.target;
		this.veId = target.dataset.veId;
		this.interId = target.dataset.interId;
		await this.fetchAddInter();
		this.render();
	};
}
!customElements.get('virtual-frag-nav') &&
	customElements.define('virtual-frag-nav', VirtualFragNav);
