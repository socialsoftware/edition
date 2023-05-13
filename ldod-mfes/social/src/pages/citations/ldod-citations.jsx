/** @format */

import Title from './components/title.jsx';
import constants from './constants.js';
import CitationsTable from './components/citations-table.jsx';
import { dataProxy } from '../../api-requests.js';
import.meta.env.DEV ? await import('@ui/table-dev.js') : await import('@ui/table.js');

export class LdodCitations extends HTMLElement {
	constructor() {
		super();
	}

	get language() {
		return this.getAttribute('language');
	}

	get numberOfCitations() {
		return this.citations?.length;
	}

	static get observedAttributes() {
		return ['data', 'language'];
	}

	getConstants(key, ...args) {
		const constant = constants[this.language][key];
		return args.length ? constant(...args) : constant;
	}

	async connectedCallback() {
		this.citations = await dataProxy.citations;
		if (!this.citations) return;
		this.render();
		this.addEventListeners();
	}

	attributeChangedCallback(name, oldV, newV) {
		this.handleChangeAttribute[name](oldV, newV);
	}

	handleChangeAttribute = {
		language: (oldV, newV) => {
			if (oldV && oldV !== newV) {
				this.querySelectorAll('[data-key]').forEach(
					node =>
						(node.firstChild.textContent = node.dataset.args
							? this.getConstants(node.dataset.key, node.dataset.args)
							: this.getConstants(node.dataset.key))
				);
			}
		},
	};

	addEventListeners = () => {
		this.addEventListener('ldod-table-searched', this.updateTitle);
	};

	updateTitle = ({ detail }) => {
		this.querySelector('h3#title').firstChild.textContent = this.getConstants(
			'title',
			detail.size
		);
	};

	render() {
		this.innerHTML = '';
		if (!this.citations) return;
		this.appendChild(
			<>
				<Title
					citationsTitle={this.getConstants('title', this.numberOfCitations)}
					numberOfCitations={this.numberOfCitations}
				/>
				<CitationsTable node={this} constants={constants} />
			</>
		);
	}
}
!customElements.get('ldod-citations') && customElements.define('ldod-citations', LdodCitations);
