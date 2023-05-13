/** @format */

import constants from '../../constants';
import SearchComponent from './search-component';
import { simpleSearchRequest } from '../../api-requests';
import style from '@src/style.css?inline';
import SimpleSearchTable from './simple-search-table';

import.meta.env.DEV ? await import('@ui/table-dev.js') : await import('@ui/table.js');

export class LdodSearchSimple extends HTMLElement {
	constructor(options) {
		super();
		this.attachShadow({ mode: 'open' });
		this.shadowRoot.appendChild(<style>{style}</style>);
		this.mode = options?.isFragmentMode;
		this.language = options?.language;
	}

	static get observedAttributes() {
		return ['language'];
	}

	set language(lang) {
		lang && this.setAttribute('language', lang);
	}

	get language() {
		return this.getAttribute('language');
	}

	get numberOfFragments() {
		return new Set(this.data?.map(inter => inter.xmlId)).size;
	}

	get numberOfInters() {
		return this.data?.length;
	}

	get isFragmentMode() {
		return this.hasAttribute('fragment');
	}

	set isFragmentMode(mode) {
		this.toggleAttribute('fragment', mode);
	}
	get selectedInters() {
		return Array.from(this.shadowRoot.querySelectorAll('tr[selected]')).map(row => row.id);
	}

	getConstants(key) {
		return constants(this.numberOfFragments, this.numberOfInters)[this.language][key];
	}
	connectedCallback() {
		this.render();
	}

	render() {
		this.shadowRoot.appendChild(
			<>
				{!this.isFragmentMode && (
					<h3 class="text-center" data-search-key="searchSimple">
						{this.getConstants('searchSimple')}
					</h3>
				)}
				<SearchComponent node={this} />
				<br />
				<br />
				<div id="search-tableContainer"></div>
			</>
		);
	}

	renderTable() {
		this.shadowRoot
			.querySelector('div#search-tableContainer')
			.replaceWith(<SimpleSearchTable node={this} />);
		this.addEventListeners();
	}

	attributeChangedCallback(name, oldV, newV) {
		this.onChangeAttribute[name](oldV, newV);
	}
	getSelectedInters = () => {
		const selected = this.selectedInters;
		return this.data?.filter(inter => selected.indexOf(inter.externalId) !== -1);
	};

	onChangeAttribute = {
		language: (oldV, newV) => {
			oldV &&
				oldV !== newV &&
				this.shadowRoot.querySelectorAll('[data-search-key]').forEach(element => {
					element.innerHTML = this.getConstants(element.dataset.searchKey);
				});
		},
	};

	disconnectedCallback() {}

	addEventListeners = () => {
		if (this.isFragmentMode) {
			this.shadowRoot.querySelectorAll('table#search-simpleTable>tbody>tr').forEach(row => {
				row.addEventListener('click', this.handleRowClick);
			});
		}
	};

	handleRowClick() {
		this.toggleAttribute('selected');
	}

	searchRequest = async e => {
		e.preventDefault();
		const searchBody = Object.fromEntries(new FormData(e.target));
		this.data = await simpleSearchRequest(searchBody);
		this.renderTable();
	};
}
!customElements.get('ldod-search-simple') &&
	customElements.define('ldod-search-simple', LdodSearchSimple);
