/** @format */

import Title from '@src/common/title.jsx';
import SourcesTable from './components/sources-table';
import constants from './constants.js';

const loadTooltip = () => import('@ui/tooltip.js');
export class LdodSources extends HTMLElement {
	constructor() {
		super();
	}

	get ldodTable() {
		return this.querySelector('ldod-table#interSourcesTable');
	}

	get language() {
		return this.getAttribute('language');
	}
	get wrapper() {
		return this.querySelector('div#interSourcesWrapper');
	}

	get interSourcesLength() {
		return this.interSources.length;
	}

	static get observedAttributes() {
		return ['language', 'data'];
	}

	getConstants(key, ...args) {
		const constant = constants[this.language][key];
		return args.length ? constant(...args) : constant;
	}

	connectedCallback() {
		this.appendChild(<div id="interSourcesWrapper"></div>);
		this.render();
		this.addEventListeners();
	}

	render() {
		if (!this.hasAttribute('data')) return;
		this.wrapper.innerHTML = '';
		this.wrapper.appendChild(
			<>
				<div style={{ display: 'flex', justifyContent: 'center' }}>
					<Title
						title={this.getConstants('interSources', this.interSourcesLength)}
						args={this.interSourcesLength}
						key="interSources"
					/>
					<span id="titleTooltip" class="icon-flex icon-info"></span>
				</div>
				<ldod-tooltip
					data-ref="div#interSourcesWrapper span#titleTooltip"
					data-tooltipkey="titleTooltip"
					placement="top"
					light
					width="250px"
					content={this.getConstants('titleTooltip')}></ldod-tooltip>

				<SourcesTable
					node={this}
					constants={{
						headers: constants.headers,
						...constants[this.language],
					}}></SourcesTable>
			</>
		);
	}

	attributeChangedCallback(name, oldV, newV) {
		this.handeChangedAttribute[name](oldV, newV);
	}

	handeChangedAttribute = {
		language: (oldV, newV) => {
			if (oldV && oldV !== newV) this.handleChangedLanguage();
		},
		data: () => {
			this.render();
			this.addHeadersTooltips();
			this.wrapper.addEventListener('pointerenter', loadTooltip);
		},
	};

	addEventListeners = () => {
		this.addEventListener('ldod-table-searched', this.updateTitle);
		this.addEventListener('ldod-table-increased', this.handleChangedLanguage);
	};

	updateTitle = ({ detail }) => {
		if (this.ldodTable.isFullyLoaded)
			this.querySelector('h3#title').firstChild.textContent = this.getConstants(
				'interSources',
				detail.size
			);
	};

	handleChangedLanguage = () => {
		this.querySelectorAll('[data-key]').forEach(node => {
			return (node.firstChild.textContent = node.dataset.args
				? this.getConstants(node.dataset.key, JSON.parse(node.dataset.args))
				: this.getConstants(node.dataset.key));
		});
		this.querySelectorAll('[data-tooltipkey]').forEach(tooltip =>
			tooltip.setAttribute('content', this.getConstants(tooltip.dataset.tooltipkey))
		);
	};

	addHeadersTooltips = () => {
		this.ldodTable.querySelectorAll('table>thead>tr>th').forEach(header => {
			this.wrapper.appendChild(
				<ldod-tooltip
					data-ref={`thead>tr>th[data-key='${header.dataset.key}']`}
					data-tooltipkey={`${header.dataset.key}Tooltip`}
					placement="top"
					content={this.getConstants(`${header.dataset.key}Tooltip`)}></ldod-tooltip>
			);
		});
	};
}
!customElements.get('ldod-sources') && customElements.define('ldod-sources', LdodSources);
