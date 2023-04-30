/** @format */

import constants from './constants';
import { selectedVEs, updateSelecteVEs } from '../event-module';
import {
	addInterRequest,
	getVirtualFragmentNavInters as getNavigationFragmentData,
} from './api-requests';
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
		VirtualFragNav.instance = this;
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
			this.intersChecked = this.virtualEditions.flatMap(ve =>
				ve.inters.filter(inter => inter.urlId === this.urlId)
			);
	};

	async connectedCallback() {
		this.addEventListener('virtual:changed', this.onCheckboxChange);
		this.addEventListener('virtual:clicked', this.addInterToVe);
		this.fragment && (await this.navDataRequest());
		this.updateSelectedInters();
		this.render();
	}

	navDataRequest = async () => {
		await getNavigationFragmentData(this.fragment, {
			veIds: selectedVEs,
			currentInterId:
				this.intersChecked?.length === 1 ? this.intersChecked[0].externalId : null,
			urlId: this.urlId,
		})
			.then(data => {
				updateSelecteVEs(data.map(ved => ({ name: ved.acronym, selected: true })));
				this.virtualEditions = data;
			})
			.catch(error => console.error(error));
	};

	interAddRequest = async () => {
		await addInterRequest(this.fragment, this.veId, this.interId, {
			veIds: selectedVEs,
			currentInterId: this.intersChecked.length === 1 && this.intersChecked[0].externalId,
		})
			.then(data => (this.virtualEditions = data))
			.catch(error => console.error(error));
	};

	render() {
		this.shadowRoot.innerHTML = fragNav({
			lang: this.language,
			intersChecked: this.intersChecked,
			virtualEditions: this.virtualEditions,
		});
	}

	onCheckboxChange = async ({ detail }) => {
		this.updateInters(detail);
		this.dispatchEvent(
			new CustomEvent('virtual:inter-selected', {
				detail: { virtualInters: detail.selected?.length },
				composed: true,
				bubbles: true,
			})
		);
		if (!this.intersChecked.length) return;
		await this.navDataRequest();
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
		await this.interAddRequest();
		this.render();
	};
}
!customElements.get('virtual-frag-nav') &&
	customElements.define('virtual-frag-nav', VirtualFragNav);
