import style from './style.css?inline';
import navigationPanelHtml from './navigation-panel-html';
import { ldodValidator, schema } from './data-schema-validator';
import '@shared/ldod-icons.js';
import '@shared/tooltip.js';

const gridTemplate = document.createElement('template');
gridTemplate.innerHTML = /*html*/ `
<div class="grid-container">
        </div>
`;

const validate = data => ldodValidator.validate(data, schema);

export default class LdodNavigationPanel extends HTMLElement {
	constructor() {
		super();
		this.attachShadow({ mode: 'open' });
		const sheet = new CSSStyleSheet();
		sheet.replaceSync(style);
		this.shadowRoot.adoptedStyleSheets = [sheet];
	}

	get language() {
		return this.getAttribute('language');
	}

	get items() {
		return this.getAttribute('items');
	}

	static get observedAttributes() {
		return ['language'];
	}

	connectedCallback() {
		if (!this.data) return;
		this.shadowRoot.innerHTML = /*html*/ `
        <div class="wrapper">
        ${navigationPanelHtml(this.data[this.language]?.title, this.data[this.language]?.tooltipContent)}
        </div>
        `;
		this.renderData();
	}

	renderData() {
		if (!this.data) return;
		const validator = validate(this.data);
		if (!validator.valid) throw Error(`data entry malformed: ${validator.errors.map(e => e.message).join(',')}`);
		this.shadowRoot.querySelectorAll('.grid-container').forEach(ele => ele.remove());
		this.data[this.language]?.grids?.forEach(row => {
			const gridContainer = gridTemplate.content.firstElementChild.cloneNode(true);
			gridContainer.innerHTML = /*html*/ `
            <div class="caption">${row.gridTitle}</div>
            <div class="grid grid-${this.items || 6}"></div>
            `;
			const grid = gridContainer.querySelector('div.grid');
			row.gridData?.forEach(cell => {
				const div = document.createElement('div');
				if (typeof cell === 'string') div.innerHTML = cell;
				if (cell instanceof Node) div.appendChild(cell);
				grid.appendChild(div);
			});
			this.shadowRoot.querySelector('div.wrapper').appendChild(gridContainer);
		});
	}

	attributeChangedCallback(name, prev, curr) {
		this.changedAttribute[name](prev, curr);
	}

	changedAttribute = {
		language: (prev, curr) => {
			if (prev === curr) return;
			this.shadowRoot.querySelectorAll('[data-key]').forEach(ele => {
				ele.textContent = this.data[this.language][ele.dataset.key];
			});
			this.shadowRoot.querySelectorAll('[data-tooltip-key]').forEach(ele => {
				ele.setAttribute('content', this.data[this.language][ele.dataset.tooltipKey]);
			});
			this.renderData();
		},
	};
}

!customElements.get('ldod-navigation-panel') && customElements.define('ldod-navigation-panel', LdodNavigationPanel);
