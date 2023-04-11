/** @format */

import thisConstants from '../constants';
import constants from '../../constants';
import VirtualEditionComponent from './virtual-edition-component';
import style from '../ve-style.css?inline';
import { getVirtualEditionByAcronym } from '../api-requests';

export class LdodVirtualEdition extends HTMLElement {
	constructor(lang) {
		super();
		this.language = lang;
		this.constants = Object.entries(thisConstants).reduce((prev, [key, value]) => {
			prev[key] = value instanceof Array ? value : { ...constants[key], ...value };
			return prev;
		}, {});
	}

	set language(lang) {
		this.setAttribute('language', lang);
	}

	get language() {
		return this.getAttribute('language');
	}

	static get observedAttributes() {
		return ['language'];
	}

	get title() {
		return this.virtualEdition?.title;
	}
	get participants() {
		return this.virtualEdition?.participants;
	}

	fetchData = async () => {
		await getVirtualEditionByAcronym(history.state?.acrn)
			.then(data => (this.virtualEdition = data))
			.catch(error => console.error(error));
	};

	getConstants = key => this.constants[this.language][key];

	async connectedCallback() {
		await this.fetchData();
		this.render();
	}

	render = () => {
		this.appendChild(<style>{style}</style>);
		this.appendChild(<VirtualEditionComponent node={this} />);
	};

	attributeChangedCallback(name, oldV, newV) {
		this.onChangeAttribute[name](oldV, newV);
	}

	onChangeAttribute = {
		language: (oldV, newV) => oldV && oldV !== newV && this.handleChangeLanguage(),
	};

	handleChangeLanguage = () => {
		this.querySelectorAll('[data-virtual-key]').forEach(ele => {
			ele.firstChild.textContent = this.getConstants(ele.dataset.virtualKey);
		});
	};
}
!customElements.get('ldod-virtual-edition') &&
	customElements.define('ldod-virtual-edition', LdodVirtualEdition);
