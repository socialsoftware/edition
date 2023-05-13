/** @format */

import FragsTable from './components/frags-table.jsx';
import Title from '@src/common/title.jsx';
import constants from './constants.js';
import { loadingPublisher } from '../../events-module.js';
import { sleep } from '../../utils.js';

export class LdodFragments extends HTMLElement {
	constructor() {
		super();
	}

	get ldodTable() {
		return this.querySelector('ldod-table#encodedFragsTable');
	}

	get language() {
		return this.getAttribute('language');
	}
	get wrapper() {
		return this.querySelector('div#encodedFragsWrapper');
	}

	get numberOfFragments() {
		return this.fragments.length;
	}

	static get observedAttributes() {
		return ['language', 'data'];
	}

	getConstants(key, ...args) {
		const constant = constants[this.language][key];
		return args.length ? constant(...args) : constant;
	}

	connectedCallback() {
		this.appendChild(<div id="encodedFragsWrapper"></div>);
		this.render();
	}

	render() {
		if (!this.hasAttribute('data')) return;
		this.wrapper.innerHTML = '';
		this.wrapper.appendChild(
			<>
				<div id="titleNavContainer">
					<Title
						title={this.getConstants('encodedFragments', this.numberOfFragments)}
						args={this.numberOfFragments}
						key="encodedFragments"
					/>
					<div>
						<FragsTable
							node={this}
							constants={{
								headers: constants.headers,
								...constants[this.language],
							}}
						/>
					</div>
				</div>
			</>
		);
		this.addEventListeners();
	}

	attributeChangedCallback(name, oldV, newV) {
		this.handeChangedAttribute[name](oldV, newV);
	}

	handeChangedAttribute = {
		language: (oldV, newV) => {
			if (oldV && oldV !== newV) this.onChangedLanguage();
		},
		data: () => {
			this.render();
		},
	};

	addEventListeners = () => {
		this.addEventListener('ldod-table-searched', this.updateTitle);
		this.addEventListener('ldod-table-increased', this.handleChangedLanguage);
	};

	updateTitle = ({ detail }) => {
		if (this.ldodTable.isFullyLoaded)
			this.querySelector('h3#title').firstChild.textContent = this.getConstants(
				'encodedFragments',
				detail.size
			);
	};

	onChangedLanguage = () => {
		loadingPublisher(true);
		sleep(1).then(() => {
			this.handleChangedLanguage();
			loadingPublisher(false);
		});
	};

	handleChangedLanguage = () => {
		this.querySelectorAll('[data-key]').forEach(node => {
			node.firstChild.textContent =
				node.dataset.args || node.hasAttribute('data-args')
					? this.getConstants(
							node.dataset.key,
							JSON.parse(node.dataset.args || node.hasAttribute('data-args'))
					  )
					: this.getConstants(node.dataset.key);
		});
	};
}
!customElements.get('ldod-fragments') && customElements.define('ldod-fragments', LdodFragments);
