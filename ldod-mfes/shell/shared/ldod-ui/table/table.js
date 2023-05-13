/** @format */

import { sleep } from '../../ldod-core/helpers';
import { loadingPublisher } from './events-module.js';
import { getRow, tableComponent, tableRows } from './table-component.js';
import { debounce } from './debounce.js';

export class LdodTable extends HTMLElement {
	constructor() {
		super();
		this.lastIndex = 0;
		this.getRow = getRow.bind();
	}

	get classes() {
		return this.getAttribute('classes');
	}

	get numberOfVisibleRows() {
		return +(this.dataset.rows ?? 20);
	}

	get isFullyLoaded() {
		return this.data.length <= this.numberOfVisibleRows || this.lastIndex === this.data.length;
	}

	get interval() {
		return this.data.length < this.numberOfVisibleRows
			? this.data.length
			: this.numberOfVisibleRows;
	}

	get language() {
		return this.getAttribute('language');
	}

	get searchKey() {
		return this.dataset.searchkey;
	}

	get searchInput() {
		return this.querySelector('input#table-search-field');
	}

	get targetToObserve() {
		let rows = this.querySelectorAll('table>tbody>tr');
		let nrOfTr = rows.length;
		return rows[Math.floor(nrOfTr * 0.75)];
	}

	static get observedAttributes() {
		return ['language'];
	}

	get searchedRows() {
		return this.allRows.filter(row => row.hasAttribute('searched'));
	}
	get unSearchedRows() {
		return this.allRows.filter(row => !row.hasAttribute('searched'));
	}
	get allRows() {
		return Array.from(this.querySelectorAll('table>tbody>tr'));
	}

	connectedCallback() {
		this.render();
		this.handleLazyRenderRows();
		history.state?.searchTerm && this.handleSearchInput();
	}

	render() {
		this.append(...tableComponent(this));
		this.addEventListeners();
	}

	getConstants = key => {
		if (!this.constants) return key;
		const constant = this.language ? this.constants[this.language][key] : this.constants[key];
		return constant || key;
	};

	addEventListeners = () => {
		this.searchInput?.addEventListener('input', () => debounce(500, this.handleSearchInput));
	};

	handleLazyRenderRows = () => {
		if (!this.isFullyLoaded) this.addObserver();
	};

	attributeChangedCallback(name, oldV, newV) {
		this.onChangeAttribute[name](oldV, newV);
	}

	onChangeAttribute = {
		language: (oldV, newV) => {
			if (oldV && oldV !== newV) {
				this.querySelectorAll('[data-key]').forEach(ele => {
					ele.firstChild.textContent = this.getConstants(ele.dataset.key);
				});
			}
		},
	};

	obsCallback = ([entry], observer) => {
		if (entry.intersectionRatio > 0) {
			observer.unobserve(entry.target);
			this.addRows();
			if (!this.isFullyLoaded) observer.observe(this.targetToObserve);
		}
	};

	addObserver() {
		const obs = new IntersectionObserver(this.obsCallback);
		obs.observe(this.targetToObserve);
		this.observer = obs;
		return obs;
	}

	addRows = end => {
		const rows = tableRows(this, this.lastIndex, end ?? this.lastIndex + this.interval);
		this.querySelector('table>tbody').append(...rows);
		this.dispatchCustomEvent('ldod-table-increased');
	};

	loadSearchStyle() {
		import('./tools.css?inline').then(style => {
			this.querySelector('#table-tools>style').innerHTML = style.default;
		});
	}

	handleSearchInput = async () => {
		if (this.isSearching) return;
		this.isSearching = true;
		loadingPublisher(true);
		await sleep(10);

		if (!this.isFullyLoaded) {
			this.observer.disconnect();
			this.addRows(this.data.length);
		}

		const searchTerm = this.querySelector('input#table-search-field')
			.value?.trim()
			.toLowerCase()
			.toString();

		history.replaceState(searchTerm ? { searchTerm } : {}, {});
		const result = searchTerm
			? this.data
					.filter(row => {
						const search =
							typeof row.search === 'string'
								? row.search.toLowerCase()
								: row.search.toString().toLowerCase();
						return search.includes(searchTerm);
					})
					.map(row => row[this.dataset.searchkey].toString())
			: this.data.map(row => row[this.dataset.searchkey].toString());

		this.querySelectorAll('tbody>tr').forEach(row =>
			row.toggleAttribute('searched', result.indexOf(row.id) !== -1)
		);

		this.dispatchCustomEvent('ldod-table-searched', {
			id: this.id,
			size: result.length,
		});
		loadingPublisher(false);
		this.isSearching = false;
	};

	dispatchCustomEvent = (event, detail) => {
		this.dispatchEvent(new CustomEvent(event, { detail, bubbles: true, composed: true }));
	};
}

!customElements.get('ldod-table') && customElements.define('ldod-table', LdodTable);
